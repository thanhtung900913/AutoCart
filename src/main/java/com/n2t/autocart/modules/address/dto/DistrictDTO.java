package com.n2t.autocart.modules.address.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DistrictDTO {

    private Integer districtId;

    private String districtName;

    private String districtNameEn;

    private String type;
}
