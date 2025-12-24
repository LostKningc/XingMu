package top.ashher.xingmu.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import top.ashher.xingmu.threadlocal.BaseParameterHolder;
import top.ashher.xingmu.util.StringUtil;

import java.io.IOException;

import static top.ashher.xingmu.constant.Constant.*;

@Slf4j
public class BaseParameterFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        ServletInputStream sis = request.getInputStream();
        String requestBody  = StringUtil.inputStreamConvertString(sis);
        if (StringUtil.isNotEmpty(requestBody)) {
            requestBody = requestBody.replaceAll(" ", "").replaceAll("\r\n","");
        }
        String traceId = request.getHeader(TRACE_ID);
        String gray = request.getHeader(GRAY_PARAMETER);
        String userId = request.getHeader(USER_ID);
        String code = request.getHeader(CODE);

        try {
            if (StringUtil.isNotEmpty(traceId)) {
                BaseParameterHolder.setParameter(TRACE_ID,traceId);
                MDC.put(TRACE_ID,traceId);
            }
            if (StringUtil.isNotEmpty(gray)) {
                BaseParameterHolder.setParameter(GRAY_PARAMETER,gray);
                MDC.put(GRAY_PARAMETER,gray);
            }
            if (StringUtil.isNotEmpty(userId)) {
                BaseParameterHolder.setParameter(USER_ID,userId);
                MDC.put(USER_ID,userId);
            }
            if (StringUtil.isNotEmpty(code)) {
                BaseParameterHolder.setParameter(CODE,code);
                MDC.put(CODE,code);
            }
            log.info("base userid : {}",BaseParameterHolder.getParameter(USER_ID) == null ? "null" : BaseParameterHolder.getParameter(USER_ID));
            log.info("current api : {} requestBody : {}",request.getRequestURI(), requestBody);
            filterChain.doFilter(request, response);
        }finally {
            BaseParameterHolder.removeParameter(TRACE_ID);
            MDC.remove(TRACE_ID);
            BaseParameterHolder.removeParameter(GRAY_PARAMETER);
            MDC.remove(GRAY_PARAMETER);
            BaseParameterHolder.removeParameter(USER_ID);
            MDC.remove(USER_ID);
            BaseParameterHolder.removeParameter(CODE);
            MDC.remove(CODE);
        }
    }
}
