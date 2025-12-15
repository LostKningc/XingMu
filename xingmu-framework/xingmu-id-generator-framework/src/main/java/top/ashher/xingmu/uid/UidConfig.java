package top.ashher.xingmu.uid;

import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UidConfig {

    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        // 这个组件负责去数据库 WORKER_NODE 表插数据，拿回 WorkerID
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    public UidGenerator uidGenerator(DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        CachedUidGenerator uidGenerator = new CachedUidGenerator();

        // 设置 WorkerID 分配器
        uidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);

        // 关于以下参数的配置，建议根据并发量调整（以下是官方推荐默认值）
        // 对于 CachedUidGenerator，timeBits, workerBits, seqBits 的含义稍有不同
        // 这里的配置决定了你的 ID 能用多少年，支持多少机器，每秒能生成多少 ID

        // 比如：
        uidGenerator.setTimeBits(29);
        uidGenerator.setWorkerBits(21);
        uidGenerator.setSeqBits(13);
        uidGenerator.setEpochStr("2025-01-01"); // 初始时间，设为项目启动年份

        // RingBuffer 环形数组相关配置（核心性能参数）
        uidGenerator.setBoostPower(3);
        uidGenerator.setScheduleInterval(60);

        return uidGenerator;
    }
}