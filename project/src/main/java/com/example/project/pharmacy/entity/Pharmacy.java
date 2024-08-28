package com.example.project.pharmacy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// 데이터베이스와 매핑될 클래스
@Entity(name = "pharmacy") // 매핑될 테이블
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //키값 생성을 데이터베이스에서 pk을 생성
    private Long id;

    private String pharmacyName;
    private String pharmacyAddress;
    private double latitude;
    private double longitude;

    public void changePharmacyAddress(String address) {
        this.pharmacyAddress = address;
    }
}
