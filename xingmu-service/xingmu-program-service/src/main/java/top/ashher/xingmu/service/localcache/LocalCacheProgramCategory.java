package top.ashher.xingmu.service.localcache;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.entity.ProgramCategory;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.function.Function;

@Component
public class LocalCacheProgramCategory {

    /**
     * 本地缓存
     * */
    private Cache<String, ProgramCategory> localCache;

    @PostConstruct
    public void localLockCacheInit(){
        localCache = Caffeine.newBuilder().build();
    }

    /**
     * 获得锁，Caffeine的get是线程安全的
     * */
    public ProgramCategory get(String id, Function<String, ProgramCategory> function){
        return localCache.get(id,function);
    }
}