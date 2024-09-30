package com.GASB.main.service;

import com.GASB.main.model.dto.dlp.DlpStatistics;
import com.GASB.main.model.dto.dlp.StatisticsByPii;
import com.GASB.main.model.dto.dlp.StatisticsByPolicy;
import com.GASB.main.model.entity.DlpReport;
import com.GASB.main.model.entity.Policy;
import com.GASB.main.repository.DlpReportRepo;
import com.GASB.main.repository.PolicyRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MainDlpStatisticsService {

    private final PolicyRepo policyRepo;
    private final DlpReportRepo dlpReportRepo;

    public MainDlpStatisticsService(PolicyRepo policyRepo, DlpReportRepo dlpReportRepo){
        this.policyRepo = policyRepo;
        this.dlpReportRepo = dlpReportRepo;
    }

    public DlpStatistics getDlpStatistics(long orgId){
        return DlpStatistics.builder()
                .statisticsByPolicies(getStatisticsByPolicy(orgId))
                .statisticsByPiis(getStatisticsByPii(orgId))
                .build();
    }

    private List<StatisticsByPolicy> getStatisticsByPolicy(long orgId){
        List<Policy> policies = policyRepo.findAllByOrgId(orgId);

        return policies.stream()
                .map(this::getPolicy)
                .toList();
    }

    private StatisticsByPolicy getPolicy(Policy policy){
        List<String> piis = getTrueFields(policy);
        Set<DlpReport> dlpReports = policy.getDlpReports();
        List<StatisticsByPii> statisticsByPii = piis.stream().map(pii -> getDetect(pii, dlpReports)).toList();

        return StatisticsByPolicy.builder()
                .policy(policy.getPolicyName())
                .pii(statisticsByPii)
                .build();
    }

    public List<String> getTrueFields(Policy policy) {
        List<String> trueFields = new ArrayList<>();
        if (policy.isIdentify()) {
            trueFields.add("identify");
        }
        if (policy.isPassport()) {
            trueFields.add("passport");
        }
        if (policy.isDrive()) {
            trueFields.add("drive");
        }
        if (policy.isForeigner()) {
            trueFields.add("foreigner");
        }
        return trueFields;
    }

    private StatisticsByPii getDetect(String pii, Set<DlpReport> dlpReports){
        long total = dlpReports != null ? dlpReports.stream()
                .filter(dlpReport -> dlpReport.getInfoCnt() >= 1)
                .filter(dlpReport -> isPiiMatched(dlpReport, pii))
                .count() : 0;

        return StatisticsByPii.builder()
                .pii(pii)
                .total(total)
                .build();
    }

    private boolean isPiiMatched(DlpReport dlpReport, String pii) {
        return dlpReport.getPii() != null && pii.equalsIgnoreCase(dlpReport.getPii().getContent());
    }

    private List<StatisticsByPii> getStatisticsByPii(long orgId) {
        List<DlpReport> dlpReports = dlpReportRepo.findAllDlpReportsByOrg(orgId);

        List<DlpReport> distinctReports = dlpReports.stream()
                .collect(Collectors.toMap(
                        dlpReport -> dlpReport.getStoredFile().getId() + ":" + dlpReport.getPii().getId(),
                        dlpReport -> dlpReport,
                        (existing, replacement) -> existing
                ))
                .values().stream()
                .toList();

        Map<String, Integer> totalByPii = distinctReports.stream()
                .collect(Collectors.groupingBy(
                        dlpReport -> dlpReport.getPii().getContent(),
                        Collectors.summingInt(DlpReport::getInfoCnt)
                ));

        return totalByPii.entrySet().stream()
                .map(entry -> StatisticsByPii.builder()
                        .pii(entry.getKey())
                        .total(entry.getValue())
                        .build())
                .toList();
    }


}
