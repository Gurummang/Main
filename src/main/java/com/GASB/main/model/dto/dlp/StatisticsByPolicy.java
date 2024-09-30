package com.GASB.main.model.dto.dlp;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsByPolicy {

    private String policy;
    private List<StatisticsByPii> pii;
}
