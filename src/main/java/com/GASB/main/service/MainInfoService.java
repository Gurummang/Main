package com.GASB.main.service;

import com.GASB.main.model.dto.info.AdminInfo;
import com.GASB.main.model.dto.info.MainInfoDto;
import com.GASB.main.model.dto.info.SaaSInfo;
import com.GASB.main.model.entity.AdminUsers;
import com.GASB.main.model.entity.DlpReport;
import com.GASB.main.model.entity.OrgSaaS;
import com.GASB.main.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MainInfoService {

    private final FileUploadRepo fileUploadRepo;
    private final MonitoredUsersRepo monitoredUsersRepo;
    private final AdminUsersRepo adminUsersRepo;
    private final OrgSaaSRepo orgSaaSRepo;
    private final AlertSettingsRepo alertSettingsRepo;

    public MainInfoService(FileUploadRepo fileUploadRepo, MonitoredUsersRepo monitoredUsersRepo, AdminUsersRepo adminUsersRepo, OrgSaaSRepo orgSaaSRepo
    ,AlertSettingsRepo alertSettingsRepo){
        this.fileUploadRepo = fileUploadRepo;
        this.monitoredUsersRepo = monitoredUsersRepo;
        this.adminUsersRepo = adminUsersRepo;
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
                .totalFileSize(totalFileSizeCount(orgId))
                .admin(getAdmin(orgId))
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

    private int totalFileSizeCount(long orgId) {
        return fileUploadRepo.getTotalSizeByOrgId(orgId);
    }
    private int totalDlpCount(long orgId) {
        return fileUploadRepo.countDlpIssuesByOrgId(orgId);
    }

    private int totalUsersCount(long orgId){
        return monitoredUsersRepo.getTotalUserCount(orgId);
    }

    private List<AdminInfo> getAdmin(long orgId){
        List<AdminUsers> adminUsers = adminUsersRepo.findAllByOrgId(orgId);
        return adminUsers.stream()
                .map(this::getAdminInfo)
                .filter(Objects::nonNull)
                .toList();
    }

    private AdminInfo getAdminInfo(AdminUsers adminUsers){
        return AdminInfo.builder()
                .org(adminUsers.getOrg().getOrgName())
                .firstName(adminUsers.getFirstName())
                .lastName(adminUsers.getLastName())
                .email(adminUsers.getEmail())
                .lastLogin(adminUsers.getLastLogin())
                .build();
    }

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
}
