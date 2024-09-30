package com.GASB.main.model.dto.info;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaaSInfo {

    private String saas;
    private String alias;
    private String email;
}
