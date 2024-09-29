package com.GASB.main.model.dto.statistics;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyFileHistory {

    private String month;
    private long total;
}
