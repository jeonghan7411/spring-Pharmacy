package com.example.project.api.service

import spock.lang.Specification

import java.nio.charset.StandardCharsets

// 테스트를 할 클래스 파일에서 command + shift + t (mac기준) 하면 이 파일을 생성할수 있음

class KakaoUriBuilderServiceTest extends Specification {

    private KakaoUriBuilderService kakaoUriBuilderService

    // 메서드 시작전 먼저 실행할수 있음  setup()
    def setup(){
        kakaoUriBuilderService = new KakaoUriBuilderService()
    }

    def "buildUriByAddressSearch - 한글 파라미터일 경우 정상적으로 인코딩"(){
        given:
        String address = "서울 성북구"
        def charset = StandardCharsets.UTF_8

        when:
        // def 타입은 동적이기 때문에 직접 타입을 명시해도 괜찮고 def를 그냥 사용해도 괜찮음
        def uri = kakaoUriBuilderService.buildUriByAddressSearch(address)
        def decodedResult = URLDecoder.decode(uri.toString(), charset)

        then:
//        println uri
//        println decodedResult
        decodedResult == "https://dapi.kakao.com/v2/local/search/address.json?query=서울 성북구"
    }
}
