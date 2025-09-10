package com.denizcanbank.accounts.mapper;

import com.denizcanbank.accounts.application.domain.valueObject.SecurityNumber;
import com.denizcanbank.accounts.dto.SecurityNumberRequestDto;
import org.springframework.stereotype.Component;

@Component
public class SecurityNumberEntityMapper implements EntityMapper<SecurityNumber, SecurityNumberRequestDto> {
    @Override
    public SecurityNumber toEntity(SecurityNumberRequestDto dto) {
        return SecurityNumber.of(dto.securityNumber());
    }
}
