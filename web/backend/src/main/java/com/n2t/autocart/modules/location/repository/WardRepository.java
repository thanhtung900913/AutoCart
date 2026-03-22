package com.n2t.autocart.modules.location.repository;

import com.n2t.autocart.modules.location.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<Ward> findByDistrict_DistrictId(Integer districtId);
    boolean existsByWardIdAndDistrict_DistrictIdAndProvince_ProvinceId(
            Integer wardId,
            Integer districtId,
            Integer provinceId
    );
}
