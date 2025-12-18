package top.ashher.xingmu.service.lua;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.redis.cache.RedisCache;

import java.util.List;

@Slf4j
@Component
public class ProgramDelCacheData {

    @Autowired
    private RedisCache redisCache;

    private DefaultRedisScript<Integer> redisScript;

    @PostConstruct
    public void init(){
        try {
            redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/programDel.lua")));
            redisScript.setResultType(Integer.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }

    public void del(List<String> keys, String[] args){
        redisCache.getInstance().execute(redisScript, keys, (Object[])args);
    }
}
