package com.example.project.pharmacy.service;

import com.example.project.pharmacy.entity.Pharmacy;
import com.example.project.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRepositoryService {
    private final PharmacyRepository pharmacyRepository;


    // ====================================
    // 외부에서 bar 라는 메소드를 호출하고 foo에 pharmacyList 전달

    //self invocation test
    public void bar(List<Pharmacy> pharmacyList) {
        log.info("bar CurrentTransactionName : " + TransactionSynchronizationManager.getCurrentTransactionName());
        foo(pharmacyList);
    }

    // self invocation test
    // 프록시 기반의 aop 어노테이션을 사용할때  self invocation 이 발생하면 부가 기능들이 동잭하지 않는다.
    // 내부에서 내부를 호출 할때는 프록시 기반의 어노테이션은 동작하지 않는다.
    // 해당 메소드의 의도는 데이터베이스에 저장이 롤백이 되어야하지만 내부에서 내부로 호출 하였기 때문에 의도대로 동작하지 않는다.
    @Transactional
    public void foo(List<Pharmacy> pharmacyList) {
        log.info("foo CurrentTransactionName : " + TransactionSynchronizationManager.getCurrentTransactionName());
        pharmacyList.forEach(pharmacy -> {
            pharmacyRepository.save(pharmacy);
            throw new RuntimeException("error"); // 예외 발생
            //spring 에서 제공하는 @Transactional 어노테이션은 디폴트로 RuntimeException 발생시 롤백 정책으로 설정 되어 있다.
        });
    }

    // read only test
    @Transactional(readOnly = true)  // 읽기 전용으로 설정
    // 약간의 성능 향상
    // JPA 관점에서 스냅샷 저장 및 Dirty Checking 작업을 수행하지 않기 때문에 성능적으로 이점
    // 따라서 Dirty Checking 불가
    public void startReadOnlyMethod(Long id) {
        pharmacyRepository.findById(id).ifPresent(pharmacy ->
                pharmacy.changePharmacyAddress("서울 특별시 광진구"));
    }


    @Transactional
    public List<Pharmacy> saveAll(List<Pharmacy> pharmacyList) {
        if(CollectionUtils.isEmpty(pharmacyList)) return Collections.emptyList();
        return pharmacyRepository.saveAll(pharmacyList);
    }

    // ====================================

    @Transactional
    public void updateAddress(Long id, String address) {
        Pharmacy entity = pharmacyRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)) {
            log.info("[PharmacyRepositoryService updateAddress] not found id : {}", id);
            return;
        }

        entity.changePharmacyAddress(address);
    }

    //for test
    public void updateAddressWithoutTransactional(Long id, String address) {
        Pharmacy entity = pharmacyRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)) {
            log.info("[PharmacyRepositoryService updateAddress] not found id : {}", id);
            return;
        }

        entity.changePharmacyAddress(address);
    }

    @Transactional(readOnly = true)
    public List<Pharmacy> findAll(){
        return pharmacyRepository.findAll();
    }
}
