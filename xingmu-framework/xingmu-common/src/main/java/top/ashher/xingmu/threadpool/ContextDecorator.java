package top.ashher.xingmu.threadpool;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

/**
 * 任务装饰器：用于在主线程和子线程之间传递上下文
 */
public class ContextDecorator implements TaskDecorator {

    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        // 1. 在【主线程】获取上下文
        // 获取 Web 请求上下文 (Header, Session 等)
        RequestAttributes context = RequestContextHolder.getRequestAttributes();
        // 获取日志 MDC 上下文 (TraceId 等)
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // 2. 在【子线程】设置上下文
                if (context != null) {
                    RequestContextHolder.setRequestAttributes(context);
                }
                if (mdcContext != null) {
                    MDC.setContextMap(mdcContext);
                }

                // 3. 执行真正的任务
                runnable.run();

            } finally {
                // 4. 【子线程】清理上下文，防止内存泄漏和线程复用导致的脏数据
                RequestContextHolder.resetRequestAttributes();
                MDC.clear();
            }
        };
    }
}