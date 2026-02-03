package com.digibo.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelResponse {
    private String wocId;
    private String custId;
    private Integer cdevType;
    private String cdevNum;
    private Long sellerId;
    private Long distribCenterId;
    private Integer level;
    private Integer tmpLevel;
    private String changeOfficer;
    private Integer specRate;
    private Integer info2Bank;
    private Integer dfAccessRight;
}
