package com.GASB.main.service;

import com.GASB.main.model.dto.statistics.*;
import com.GASB.main.model.entity.*;
import com.GASB.main.repository.ActivitiesRepo;
import com.GASB.main.repository.DlpReportRepo;
import com.GASB.main.repository.FileUploadRepo;
import com.GASB.main.repository.OrgSaaSRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class MainStatisticsService {

    private static final String UNKNOWN = "Unknown";

    private final FileUploadRepo fileUploadRepo;
    private final DlpReportRepo dlpReportRepo;
    private final ActivitiesRepo activitiesRepo;
    private final OrgSaaSRepo orgSaaSRepo;

    public MainStatisticsService(FileUploadRepo fileUploadRepo, DlpReportRepo dlpReportRepo, ActivitiesRepo activitiesRepo, OrgSaaSRepo orgSaaSRepo){
        this.fileUploadRepo = fileUploadRepo;
        this.dlpReportRepo = dlpReportRepo;
        this.activitiesRepo = activitiesRepo;
        this.orgSaaSRepo = orgSaaSRepo;
    }

    public MainStatisticsDto getStatistics(long orgId){
        return MainStatisticsDto.builder()
                .fileScanInToday(getFileScan(orgId))
                .fileAnalysis(getFileAnalysis(orgId))
                .fileHistoryInfo(getFileHistoryInfo(orgId))
                .fileHistoryStatistics(getLast12Months(orgId))
                .build();
    }

    private List<FileScanDto> getFileScan(long orgId) {
        LocalDate today = LocalDate.now();
        List<FileUpload> fileUploads = fileUploadRepo.findAllByOrgIdAndDate(orgId, today);
        List<DlpReport> allDlpReports = dlpReportRepo.findAllDlpReportsByOrgId(orgId);

        Map<Long, List<DlpReport>> dlpReportsMap = allDlpReports.stream()
                .collect(Collectors.groupingBy(report -> report.getStoredFile().getId()));

        return fileUploads.stream()
                .map(fileUpload -> createFileListDto(fileUpload, dlpReportsMap.get(fileUpload.getStoredFile().getId()))) // fileUpload.getStoredFile().getId()로 가져오기
                .filter(Objects::nonNull)
                .toList();
    }

    private FileScanDto createFileListDto(FileUpload fileUpload, List<DlpReport> dlpReports) {
        StoredFile storedFile = fileUpload.getStoredFile();

        Activities activities = getActivities(fileUpload.getSaasFileId(), fileUpload.getTimestamp());
        if (activities == null) {
            log.debug("No Activities found for fileUpload id: {}", fileUpload.getId());
        }

        String creator = Optional.ofNullable(activities)
                .map(Activities::getUser)
                .map(MonitoredUsers::getUserName)
                .orElse(UNKNOWN);

        return FileScanDto.builder()
                .saas(fileUpload.getOrgSaaS().getSaas().getSaasName())
                .fileName(activities != null ? activities.getFileName() : UNKNOWN)
                .suspicious(isSuspicious(fileUpload, storedFile))
                .vt(isMalware(storedFile))
                .dlp(isSensitive(storedFile, dlpReports))
                .creator(creator)
                .eventTs(fileUpload.getTimestamp())
                .build();
    }

    private int isMalware(StoredFile storedFile) {
        if(storedFile.getFileStatus().getVtStatus() == 1) {
            VtReport vtReport = storedFile.getVtReport();
            if(vtReport != null && !"none".equals(vtReport.getThreatLabel())){
                return 2;
            }
        }
        return storedFile.getFileStatus().getVtStatus();
    }

    private int isSuspicious(FileUpload fileUpload, StoredFile storedFile){
        if(storedFile.getFileStatus().getGscanStatus() == 1 && (fileUpload.getTypeScan().getCorrect().equals(false) || storedFile.getScanTable().isDetected())){
            return 2;
        }
        return storedFile.getFileStatus().getGscanStatus();
    }

    private int isSensitive(StoredFile storedFile, List<DlpReport> dlpReports) {
        if (storedFile.getFileStatus().getDlpStatus() == 1) {
            long storedFileId = storedFile.getId();

            List<DlpReport> relatedReports = dlpReports.stream()
                    .filter(dlpReport -> dlpReport.getStoredFile().getId() == storedFileId)
                    .toList();

            boolean hasSensitiveInfo = relatedReports.stream()
                    .anyMatch(dlpReport -> dlpReport.getInfoCnt() >= 1);

            if (hasSensitiveInfo) {
                return 2;
            }
        }

        return storedFile.getFileStatus().getGscanStatus();
    }

    private Activities getActivities(String saasFileId, LocalDateTime timestamp) {
        Activities activities = activitiesRepo.findAllBySaasFileIdAndTimeStamp(saasFileId, timestamp);
        if (activities == null) {
            log.info("No activities found for saasFileId: {} and timestamp: {}", saasFileId, timestamp);
        }
        return activities;
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

    private List<DailyFileHistory> getLast12Months(long orgId) {
        List<Activities> activities = activitiesRepo.findByOrgId(orgId);
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        List<String> last12Months = IntStream.range(0, 12)
                .mapToObj(currentMonth::minusMonths)
                .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .toList();

        Map<String, Long> monthlyTotalMap = activities.stream()
                .collect(Collectors.groupingBy(
                        activity -> activity.getEventTs().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.counting()
                ));

        return last12Months.stream()
                .map(month -> new DailyFileHistory(month, monthlyTotalMap.getOrDefault(month, 0L)))
                .toList();
    }
}
