package com.GASB.main.model.dto.info;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MainInfoDto {

    private List<SaaSInfo> saas;
    private int totalSaaS;
    private int totalAlert;
    private int totalDlp;
    private int totalUser;
    private int totalFile;
    private int dailyIncreaseFileCount;
    private long totalFileSize;
    private long dailyIncreaseFileSize;
    // private List<AdminInfo> admin;
}
