package com.example.project.pharmacy.service;

import com.example.project.api.dto.DocumentDto;
import com.example.project.api.dto.KakaoApiResponseDto;
import com.example.project.api.service.KakaoAddressSearchService;
import com.example.project.direction.entity.Direction;
import com.example.project.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    public void recommendPharmacyList(String address) {

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        // 널 값이 거나 Document 부분이 비어 있다면
        if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList] Input address: {}", address);
            return;
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // 공공 데이터 활용
//        List<Direction> directionList = directionService.buildDirectionList(documentDto);
        //
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        directionService.saveAll(directionList);
    }
}
