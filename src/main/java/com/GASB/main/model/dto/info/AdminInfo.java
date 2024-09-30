package com.GASB.main.model.dto.info;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminInfo {

    private String org;
    private String firstName;
    private String lastName;
    private String email;
    private Timestamp lastLogin;
}
