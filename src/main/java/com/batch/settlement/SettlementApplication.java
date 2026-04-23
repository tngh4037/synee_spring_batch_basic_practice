package com.batch.settlement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// @EnableScheduling // 이 애노테이션이 있어야 스프링 스케줄러 동작, 해당 배치를 실행시키는 주체가 스프링일때만 적용. (배치 실행 주체가 Jenkins 라면 해당 애노테이션 제거) => @EnableScheduling은 스프링 자체의 스케줄링 기능을 활성화. Jenkins와 같은 외부 스케줄러가 잡 실행을 담당할 때는 중복 및 충돌을 피하기 위해 제거
@SpringBootApplication
public class SettlementApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(SettlementApplication.class, args))); // 스프링 배치가 실행된 결과(성공/실패)를 운영체제나 외부 스케줄러(Jenkins, Airflow 등)에게 정확하게 알려주기 위해서 작성하는 표준 패턴
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


// ==================================

// Jenkins 연동시 @EnableScheduling 제거
// `@EnabledScheduling` 어노테이션을 제거하는 이유는, 우리가 이 배치의 실행 주도권을 스프링 부트에서 젠킨스로 넘기기 때문.
// 기존의 방식이 24시간 동안 서버를 켜놓고 해당 시간이 되면 배치를 돌려주는 방식이라면, 젠킨스는 해당 시간에 자바 프로그램만 잠시 실행했다가 꺼주기 때문에, 서버 자원을 보다 효율적으로 관리할 수 있다.
