package com.GASB.main.service;

import com.GASB.main.model.dto.info.FileSizeBySaaS;
import com.GASB.main.model.dto.info.FileUploadBySaaS;
import com.GASB.main.model.dto.info.MainInfoDto;
import com.GASB.main.model.dto.info.SaaSInfo;
import com.GASB.main.model.entity.OrgSaaS;
import com.GASB.main.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MainInfoService {

    private final FileUploadRepo fileUploadRepo;
    private final MonitoredUsersRepo monitoredUsersRepo;
    private final OrgSaaSRepo orgSaaSRepo;
    private final AlertSettingsRepo alertSettingsRepo;

    public MainInfoService(FileUploadRepo fileUploadRepo, MonitoredUsersRepo monitoredUsersRepo, OrgSaaSRepo orgSaaSRepo, AlertSettingsRepo alertSettingsRepo){
        this.fileUploadRepo = fileUploadRepo;
        this.monitoredUsersRepo = monitoredUsersRepo;
        this.orgSaaSRepo = orgSaaSRepo;
        this.alertSettingsRepo = alertSettingsRepo;
    }

    public MainInfoDto getInfo(long orgId){
        return MainInfoDto.builder()
                .saas(getSaaS(orgId))
                .totalSaaS(totalSaaSCount(orgId))
                .totalAlert(totalAlertSettingsCount(orgId))
                .totalDlp(totalDlpCount(orgId))
                .totalUser(totalUsersCount(orgId))
                .totalFile(totalFilesCount(orgId))
                .dailyFileCountDifference(increasedFile(orgId))
                .totalFileSize(totalFileSizeCount(orgId))
                .dailyFileSizeDifference(increasedFileSize(orgId))
                .fileSizeBySaaS(getFileSizeBySaaS(orgId))
                .fileUploadBySaaS(getFileUploadBySaaS(orgId))
                .build();
    }

    private int totalSaaSCount(long orgId){
        return orgSaaSRepo.countSaaSByOrgId(orgId);
    }

    private int totalAlertSettingsCount(long orgId){
        return alertSettingsRepo.countSettingByOrgId(orgId);
    }

    private int totalFilesCount(long orgId) {
        return fileUploadRepo.countFileByOrgId(orgId);
    }

//    public int increasedFile(long orgId) {
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1);
//
//        int countUntilYesterday = fileUploadRepo.getTotalCountUntil(orgId, yesterday);
//        int countUntilToday = fileUploadRepo.getTotalCountUntil(orgId, today);
//
//        return countUntilToday - countUntilYesterday;
//    }

    private long totalFileSizeCount(long orgId) {
        return fileUploadRepo.getTotalSizeByOrgId(orgId);
    }

//    public long increasedFileSize(long orgId) {
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1);
//
//        long sizeUntilYesterday = fileUploadRepo.getTotalSizeUntil(orgId, yesterday);
//        long sizeUntilToday = fileUploadRepo.getTotalSizeUntil(orgId, today);
//
//        return sizeUntilToday - sizeUntilYesterday;
//    }

    private int totalDlpCount(long orgId) {
        return fileUploadRepo.countDlpIssuesByOrgId(orgId);
    }

    private int totalUsersCount(long orgId){
        return monitoredUsersRepo.getTotalUserCount(orgId);
    }

//    private List<AdminInfo> getAdmin(long orgId){
//        List<AdminUsers> adminUsers = adminUsersRepo.findAllByOrgId(orgId);
//        return adminUsers.stream()
//                .map(this::getAdminInfo)
//                .filter(Objects::nonNull)
//                .toList();
//    }

//    private AdminInfo getAdminInfo(AdminUsers adminUsers){
//        return AdminInfo.builder()
//                .org(adminUsers.getOrg().getOrgName())
//                .firstName(adminUsers.getFirstName())
//                .lastName(adminUsers.getLastName())
//                .email(adminUsers.getEmail())
//                .lastLogin(adminUsers.getLastLogin())
//                .build();
//    }

    private List<SaaSInfo> getSaaS(long orgId){
        List<OrgSaaS> orgSaaSList = orgSaaSRepo.findAllByOrgId(orgId);
        return orgSaaSList.stream()
                .map(this::getSaaSInfo)
                .filter(Objects::nonNull)
                .toList();
    }

    private SaaSInfo getSaaSInfo(OrgSaaS orgSaaS){
        return SaaSInfo.builder()
                .saas(orgSaaS.getSaas().getSaasName())
                .alias(orgSaaS.getConfig().getAlias())
                .email(orgSaaS.getConfig().getSaasAdminEmail())
                .build();
    }

    private List<FileSizeBySaaS> getFileSizeBySaaS(long orgId){
        List<String> saasNameList = orgSaaSRepo.findDistinctSaaSByOrgId(orgId);

        return saasNameList.stream()
                .map(saasName -> getFileSize(saasName, orgId))
                .filter(Objects::nonNull)
                .toList();
    }

//    private FileSizeBySaaS getFileSize(String saasName, long orgId){
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1);
//
//        long totalSizeUntilYesterday = fileUploadRepo.getTotalSizeUntilByOrgAndSaaS(orgId, saasName, yesterday);
//        long totalSizeUntilToday = fileUploadRepo.getTotalSizeUntilByOrgAndSaaS(orgId, saasName, today);
//
//        long dailyDifference = totalSizeUntilToday - totalSizeUntilYesterday;
//
//        return FileSizeBySaaS.builder()
//                .saas(saasName)
//                .size(totalSizeUntilToday)
//                .dailyDifference(dailyDifference)
//                .build();
//    }

    private List<FileUploadBySaaS> getFileUploadBySaaS(long orgId){
        List<String> saasNameList = orgSaaSRepo.findDistinctSaaSByOrgId(orgId);

        return saasNameList.stream()
                .map(saasName -> getFileUpload(saasName, orgId))
                .filter(Objects::nonNull)
                .toList();
    }

//    private FileUploadBySaaS getFileUpload(String saasName, long orgId){
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1);
//
//        int totalUploadUntilYesterday = fileUploadRepo.getTotalUploadUntilByOrgAndSaaS(orgId, saasName, yesterday);
//        int totalUploadUntilToday = fileUploadRepo.getTotalUploadUntilByOrgAndSaaS(orgId, saasName, today);
//
//        int dailyDifference = totalUploadUntilToday - totalUploadUntilYesterday;
//
//        return  FileUploadBySaaS.builder()
//                .saas(saasName)
//                .upload(totalUploadUntilToday)
//                .dailyDifference(dailyDifference)
//                .build();
//
//    }

    public int increasedFile(long orgId) {
        LocalDateTime endOfYesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MAX); // 어제 23:59:59
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX); // 오늘 23:59:59

        int countUntilYesterday = fileUploadRepo.getTotalCountUntil(orgId, endOfYesterday);
        int countUntilToday = fileUploadRepo.getTotalCountUntil(orgId, endOfToday);

        return countUntilToday - countUntilYesterday;
    }

    public long increasedFileSize(long orgId) {
        LocalDateTime endOfYesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MAX); // 어제 23:59:59
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX); // 오늘 23:59:59

        long sizeUntilYesterday = fileUploadRepo.getTotalSizeUntil(orgId, endOfYesterday);
        long sizeUntilToday = fileUploadRepo.getTotalSizeUntil(orgId, endOfToday);

        return sizeUntilToday - sizeUntilYesterday;
    }

    private FileSizeBySaaS getFileSize(String saasName, long orgId) {
        LocalDateTime endOfYesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MAX); // 어제 23:59:59
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX); // 오늘 23:59:59

        long totalSizeUntilYesterday = fileUploadRepo.getTotalSizeUntilByOrgAndSaaS(orgId, saasName, endOfYesterday);
        long totalSizeUntilToday = fileUploadRepo.getTotalSizeUntilByOrgAndSaaS(orgId, saasName, endOfToday);

        long dailyDifference = totalSizeUntilToday - totalSizeUntilYesterday;

        return FileSizeBySaaS.builder()
                .saas(saasName)
                .size(totalSizeUntilToday)
                .dailyDifference(dailyDifference)
                .build();
    }

    private FileUploadBySaaS getFileUpload(String saasName, long orgId) {
        LocalDateTime endOfYesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MAX); // 어제 23:59:59
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX); // 오늘 23:59:59

        int totalUploadUntilYesterday = fileUploadRepo.getTotalUploadUntilByOrgAndSaaS(orgId, saasName, endOfYesterday);
        int totalUploadUntilToday = fileUploadRepo.getTotalUploadUntilByOrgAndSaaS(orgId, saasName, endOfToday);

        int dailyDifference = totalUploadUntilToday - totalUploadUntilYesterday;

        return FileUploadBySaaS.builder()
                .saas(saasName)
                .upload(totalUploadUntilToday)
                .dailyDifference(dailyDifference)
                .build();
    }

}
