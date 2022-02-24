package com.citizen.springbatch;

import static org.assertj.core.api.Assertions.assertThat;

import com.citizen.springbatch.config.TestBatchConfig;
import com.citizen.springbatch.domain.Product;
import com.citizen.springbatch.domain.PurchaseOrder;
import com.citizen.springbatch.jobs.EntityContextConfiguration;
import com.citizen.springbatch.repository.PurchaseOrderRepository;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * author : citizen103
 */
@ActiveProfiles("mariadb")
@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {EntityContextConfiguration.class, TestBatchConfig.class})
public class EntityContextTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    private void initPurchaseOrder() {
        final int size = 100;

        for (int i = 0; i < size; i++) {
            purchaseOrderRepository.save(
                PurchaseOrder.builder()
                    .memo("Memo")
                    .productList(
                        Arrays.asList(
                            Product.builder().name("마우스").amount(10_000L).build(),
                            Product.builder().name("키보드").amount(30_000L).build()
                        )
                    )
                    .build()
            );
        }
    }

    @Test
    void reader_processor_entity의_영속성유지() throws Exception {
        // given
        initPurchaseOrder();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(purchaseOrderRepository.findAll().size())
            .isEqualTo(100);

        assertThat(jobExecution.getStatus())
            .isEqualTo(BatchStatus.COMPLETED);
    }

}
