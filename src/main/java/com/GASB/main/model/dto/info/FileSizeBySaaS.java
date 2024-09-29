package com.GASB.main.model.dto.info;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileSizeBySaaS {
    private String saas;
    private long size;
    private long dailyDifference;
}
