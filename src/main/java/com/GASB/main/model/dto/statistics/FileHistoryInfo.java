package com.GASB.main.model.dto.statistics;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileHistoryInfo {

    private String saas;
    private long upload;
    private long change;
    private long delete;
    private LocalDate lastActivity;
}
