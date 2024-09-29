package com.GASB.main.model.dto.statistics;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MainStatisticsDto {

    private List<FileScanDto> fileScanInToday;
    private FileAnalysis fileAnalysis;
    private List<FileHistoryInfo> fileHistoryInfo;
    private List<DailyFileHistory> fileHistoryStatistics;
}
