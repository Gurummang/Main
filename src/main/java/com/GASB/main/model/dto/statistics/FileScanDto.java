package com.GASB.main.model.dto.statistics;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileScanDto {

    private String fileName;
    private int suspicious;
    private int dlp;
    private int vt;
    private String creator;
    private LocalDateTime createdAt;

}
