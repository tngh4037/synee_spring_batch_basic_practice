package com.batch.settlement.job;

import com.batch.settlement.domain.Orders;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Configuration
@StepScope // jobparameters 사용
public class SettlementJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final EntityManagerFactory entityManagerFactory;

    // ItemReader
    @Bean
    public JpaPagingItemReader<Orders> ordersReader( // jpa를 사용한다면 ItemReader 로 JpaPagingItemReader 를 사용해야 한다.
            @Value("#{jobparameters['targetDate']}") String targetDate) // 외부에서 날짜 주입
    {
        log.info("[Reader] 정산 집계 대상 날짜: {}", targetDate);

        return new JpaPagingItemReaderBuilder<Orders>()
                .name("ordersReader")
                .entityManagerFactory(entityManagerFactory) // jpa 를 통해 읽기에 emf를 넣어주어야 한다.
                .pageSize(1_000) // 1000개씩 조회 (default: 10)
                .queryString("SELECT o FROM Orders o WHERE o.orderDate = :targetDate ORDER BY o.id")
                .parameterValues(Collections.singletonMap("targetDate", LocalDate.parse(targetDate)))
                .build();
    }



}
