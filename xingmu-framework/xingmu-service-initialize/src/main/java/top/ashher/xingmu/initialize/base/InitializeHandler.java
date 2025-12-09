package top.ashher.xingmu.initialize.base;

import org.springframework.context.ConfigurableApplicationContext;
/**
 * 初始化处理器
 * */
public interface InitializeHandler {
    /**
     * 初始化执行 类型
     * @return 类型
     * */
    String type();

    /**
     * 执行顺序
     * @return 顺序
     * */
    Integer executeOrder();

    /**
     * 执行逻辑
     * @param context 容器上下文
     * */
    void executeInit(ConfigurableApplicationContext context);

}
