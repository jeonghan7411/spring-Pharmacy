package com.example.project.direction.service

import spock.lang.Specification

class Base62ServiceTest extends Specification {

    private Base62Service base62Service

    def setup() {
        base62Service = new Base62Service()
    }


    def "check base62 encoder/decoder"() {
        given:
        long num = 5

        when:
        def encodedId = base62Service.encodeDriectionId(num)

        then:
        def decodedId = base62Service.decondeDriectionId(encodedId)

        then:
        num == decodedId
    }
}
