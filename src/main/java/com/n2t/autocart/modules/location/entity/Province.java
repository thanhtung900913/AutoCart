package com.n2t.autocart.modules.address.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "provinces")
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "province_name", nullable = false)
    private String provinceName;

    @Column(name = "province_name_en", nullable = false)
    private String provinceNameEn;

    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "province")
    private Set<District> districts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "province")
    private Set<Ward> wards = new LinkedHashSet<>();

    @OneToMany(mappedBy = "province")
    private Set<Address> addresses = new LinkedHashSet<>();
}
