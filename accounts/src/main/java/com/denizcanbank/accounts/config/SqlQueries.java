package com.denizcanbank.accounts.config;

import jakarta.validation.executable.ValidateOnExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:sql.yaml", factory = YamlPropertySourceFactory.class)
public class SqlQueries {
    @Value("${sql.select.account_number}")
    public String selectAccountNumber;

    @Value("${sql.select.security_number}")
    public String selectSecurityNumber;

    @Value("${sql.insert}")
    public String insert;

    @Value("${sql.update}")
    public String update;

    @Value("${sql.delete.account_number}")
    public String deleteAccountNumber;

    @Value("${sql.delete.security_number}")
    public String deleteSecurityNumber;
}
