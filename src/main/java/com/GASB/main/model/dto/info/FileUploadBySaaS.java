package com.GASB.main.model.dto.info;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadBySaaS {

    private String saas;
    private int upload;
    private int dailyDifference;
}
