package top.ashher.xingmu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class BaseDataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseDataServiceApplication.class, args);
    }

}
