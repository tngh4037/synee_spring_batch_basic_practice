package com.batch.settlement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 이 애노테이션이 있어야 스케줄러 동작
@SpringBootApplication
public class SettlementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SettlementApplication.class, args);
	}

}

/*
// 애플리케이션 실행 시점 5분 후 job 실행된다고 가정.
1. Application start
2. Bean 생성 (reader(proxy by stepscope)/processor/writer)
3. Scheduler 5분 뒤 실행
4. Job 실행
5. Step 실행
6. Reader → Processor → Writer 실제 동작
*/