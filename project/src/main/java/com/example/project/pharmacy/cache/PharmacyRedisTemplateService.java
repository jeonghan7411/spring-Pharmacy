package com.example.project.pharmacy.cache;

import com.example.project.pharmacy.dto.PharmacyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRedisTemplateService {

    private static final String CACHE_KEY = "PHARMACY"; // 키 값 지정

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private HashOperations<String, String, String> hashOperations;
    //  레디스에서 사용하는 키값 , 약국pk, 약국 데이터 string값으로

    /**
     * @PostConstruct
     * 의존성 주입이 이루어진 후 초기화를 수행하는 메서드.
     * 해당 어노테이션이 붙은 메서드는 빈이 생성되고 의존성 주입이 완료된 직후 자동으로 호출됩니다.
     * 이 메서드는 외부에서 명시적으로 호출하지 않더라도, Spring 컨테이너가 자동으로 호출해 줍니다.
     * 장점 ) 생성자(일반)가 호출 되었을 때, 빈은 아직 초기화 되지 않았다.(주입된 의존성이 없다)
     * 하지만, 해당 어노테이션을 사용하면 빈이 초기화 됨과 동시에 의존성을 확인 할 수 있음
     */
    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    //  redis에 저장하는 메소드
    public void save(PharmacyDto pharmacyDto) {
        if(Objects.isNull(pharmacyDto) || Objects.isNull(pharmacyDto.getId())) {
            log.error("Required Values must not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    pharmacyDto.getId().toString(),
                    serializePharmacyDto(pharmacyDto));
            log.info("[PharmacyRedisTemplateService save success] id: {}", pharmacyDto.getId());
        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService save error] {}", e.getMessage());
        }
    }


    //  전체 조회 메소드
    public List<PharmacyDto> findAll() {

        try {
            List<PharmacyDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                PharmacyDto pharmacyDto = deserializePharmacyDto(value);
                list.add(pharmacyDto);
            }
            return list;

        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 테스트를 위한 삭제 메소드
    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[PharmacyRedisTemplateService delete] : {}", id);
    }


    // redis에 저장을 위한 자료형 변환 메소드
    private String serializePharmacyDto(PharmacyDto pharmacyDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(pharmacyDto);
    }

    private PharmacyDto deserializePharmacyDto(String value) throws JsonProcessingException {
        return  objectMapper.readValue(value, PharmacyDto.class);  // 파라미터값, 변환하고자하는 타입 입력
    }
}
