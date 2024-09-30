package com.GASB.main.model.dto.dlp;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsByPii {

    private String pii;
    private long total;
}
