package com.batch.settlement.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobOperator jobOperator;
    private final Job settlementJob; // "settlementJob" 이라는 이름의 job을 찾아서 넣어 줌

    // 매일 새벽 4시 수행
    // @Scheduled(cron = "0 0 4 * * *")
    @Scheduled(cron = "0 0 4 * * *")
    public void runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("targetDate", LocalDate.now().minusDays(7).toString())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            log.info("스케쥴러 작동! 배치를 실행합니다.");

            jobOperator.start(settlementJob, jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException | InvalidJobParametersException | JobRestartException e) {
            log.error("배치 실행 중 예외가 발생했습니다.", e);
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생");
        }
    }
}

// 참고) time의 역할
// time은 배치를 “항상 새로운 JobInstance로 실행시키기 위한 용도이다.
// Spring Batch에서 Job은 JobParameters가 같으면 같은 JobInstance로 간주된다.
// 따라서 값이 동일한 상태로 다시 실행하면 이미 실행된 JobInstance로 인식해서 JobInstanceAlreadyCompleteException 같은 예외가 발생할 수 있다.

// 그래서 매번 서로 다른 JobParameters가 만들어지고 결과적으로 항상 새로운 JobInstance가 생성되도록 하기 위해 time 을 넣어준다.
// time 이 아닌, 이러한 용도로 사용하기 위한 다른 방법을 써도 된다.
// 만약, "같은 targetDate는 딱 한 번만 실행되게 하고 싶다" 라고 한다면, time을 넣으면 안된다. 강의에서는 여러번 실습해보기 위해 넣어준 것.
