package com.digibo.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long id;
    private String name;
    private String issuerCountry;
    private String personalId;
    private String passportNo;
    private String street;
    private String city;
    private String country;
    private String zip;
    private String phone;
    private String mobile;
    private String fax;
    private String email;
    private String apart;
    private String house;
    private Integer stdQ;
    private String specQ;
    private String answer;
    private Date regDate;
    private Date changeDate;
    private String changeOfficerId;
    private String changeLogin;
    private Long customerId;
    private Integer migrStatus;
    private Integer hasAgreementInGlobus;
}
