package com.work.daily;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @DataJpaTest는 JPA와 관련된 설정들만을 로드한다.
 * 즉, QueryDSL를 활용하기 위한  JPAQueryFactory가 Bean으로 로드되지 않는다.
 * @TestConfiguration로 테스트에서만 활용할 수 있는 JPAQueryFactory를 Bean으로 등록한다.
 *
 */
@TestConfiguration
public class TestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
