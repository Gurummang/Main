package com.GASB.main.model.dto.statistics;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileAnalysis {

    private long totalCount;
    private long suspiciousAnalysis;
    private long dlpAnalysis;
    private long vtAnalysis;
}
