package com.GASB.main.service;

import com.GASB.main.model.dto.statistics.FileAnalysis;
import com.GASB.main.model.dto.statistics.FileHistoryInfo;
import com.GASB.main.model.dto.statistics.MainStatisticsDto;
import com.GASB.main.model.entity.Activities;
import com.GASB.main.repository.ActivitiesRepo;
import com.GASB.main.repository.FileUploadRepo;
import com.GASB.main.repository.OrgSaaSRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MainStatisticsService {

    private final FileUploadRepo fileUploadRepo;
    private final ActivitiesRepo activitiesRepo;
    private final OrgSaaSRepo orgSaaSRepo;

    public MainStatisticsService(FileUploadRepo fileUploadRepo, ActivitiesRepo activitiesRepo, OrgSaaSRepo orgSaaSRepo){
        this.fileUploadRepo = fileUploadRepo;
        this.activitiesRepo = activitiesRepo;
        this.orgSaaSRepo = orgSaaSRepo;
    }

    public MainStatisticsDto getStatistics(long orgId){
        return MainStatisticsDto.builder()
                .fileScanInToday(new ArrayList<>())
                .fileAnalysis(getFileAnalysis(orgId))
                .fileHistoryInfo(getFileHistoryInfo(orgId))
                .fileHistoryStatistics(new ArrayList<>())
                .build();
    }

    private FileAnalysis getFileAnalysis(long orgId){
        int totalCount = fileUploadRepo.countFileByOrgId(orgId);
        int suspiciousCount = fileUploadRepo.countSuspiciousFileByOrgId(orgId);
        int dlpCount = fileUploadRepo.countDlpFileByOrgId(orgId);
        int vtCount = fileUploadRepo.countVtFileByOrgId(orgId);

        return FileAnalysis.builder()
                .totalCount(totalCount)
                .suspiciousAnalysis(suspiciousCount)
                .dlpAnalysis(dlpCount)
                .vtAnalysis(vtCount)
                .build();
    }

    private List<FileHistoryInfo> getFileHistoryInfo(long orgId){
        List<String> saasNameList = orgSaaSRepo.findDistinctSaaSByOrgId(orgId);

        return saasNameList.stream()
                .map(saas -> getFileHistory(saas, orgId))
                .filter(Objects::nonNull)
                .toList();
    }

    private FileHistoryInfo getFileHistory(String saas, long orgId){
        long upload = activitiesRepo.countUploadByOrgAndSaaS(orgId, saas);
        long change = activitiesRepo.countChangeByOrgAndSaaS(orgId, saas);
        long delete = activitiesRepo.countDeleteByOrgAndSaaS(orgId, saas);
        LocalDate lastActivity = activitiesRepo.findLastActivityByOrgAndSaaS(orgId, saas);

        return FileHistoryInfo.builder()
                .saas(saas)
                .upload(upload)
                .change(change)
                .delete(delete)
                .lastActivity(lastActivity)
                .build();
    }
}
