package com.n2t.autocart.modules.location.repository;

import com.n2t.autocart.modules.location.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByProvince_ProvinceId(Integer provinceId);
}
