package com.example.project;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;



//생성 날짜나 수정날짜의 자동관리를 위해서
@Getter
@MappedSuperclass //공통 매핑 정보가 필요할 때, 부모 클래스에 선언하고 속성만 상속 받아서 사용하고 싶을 때 @MappedSuperclass를 사용한다.
@EntityListeners(AuditingEntityListener.class)   //DB에 엔티티를 저장하기 전에 특정 행위를 할 수 있게 해준다.
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
