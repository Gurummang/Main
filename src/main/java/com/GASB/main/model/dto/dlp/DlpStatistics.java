package com.GASB.main.model.dto.dlp;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DlpStatistics {

    private List<StatisticsByPolicy> statisticsByPolicies;
    private List<StatisticsByPii> statisticsByPiis;
}
