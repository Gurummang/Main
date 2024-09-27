package com.GASB.main.controller;

import com.GASB.main.annotation.JWT.ValidateJWT;
import com.GASB.main.exception.InvalidJwtException;
import com.GASB.main.model.dto.ResponseDto;
import com.GASB.main.model.dto.info.MainInfoDto;
import com.GASB.main.model.entity.AdminUsers;
import com.GASB.main.repository.AdminUsersRepo;
import com.GASB.main.service.MainInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/main")
public class MainController {

    private static final String EMAIL = "email";
    private static final String EMAIL_NOT_FOUND = "Admin not found with email: ";
    private static final String INVALID_JWT_MSG = "Invalid JWT: email attribute is missing.";
    private final AdminUsersRepo adminUsersRepo;

    private final MainInfoService mainInfoService;

    public MainController(AdminUsersRepo adminUsersRepo, MainInfoService mainInfoService){
        this.adminUsersRepo = adminUsersRepo;
        this.mainInfoService = mainInfoService;
    }

    private Optional<AdminUsers> getAdminUser(HttpServletRequest servletRequest) {
        String email = (String) servletRequest.getAttribute(EMAIL);
        if (email == null) {
            throw new InvalidJwtException(INVALID_JWT_MSG);
        }
        return adminUsersRepo.findByEmail(email);
    }

    @ValidateJWT
    @GetMapping("/info")
    public ResponseDto<MainInfoDto> getInfo(HttpServletRequest servletRequest){
        try {
            Optional<AdminUsers> adminOptional = getAdminUser(servletRequest);
            if (adminOptional.isEmpty()) {
                return ResponseDto.ofFail(EMAIL_NOT_FOUND);
            }

            long orgId = adminOptional.get().getOrg().getId();
            MainInfoDto result = mainInfoService.getInfo(orgId);
            return ResponseDto.ofSuccess(result);
        } catch (RuntimeException e){
            return ResponseDto.ofFail(e.getMessage());
        }
    }
}
