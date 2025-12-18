package top.ashher.xingmu.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ashher.xingmu.dto.ParentProgramCategoryDto;
import top.ashher.xingmu.dto.ProgramCategoryAddDto;
import top.ashher.xingmu.dto.ProgramCategoryDto;
import top.ashher.xingmu.entity.ProgramCategory;
import top.ashher.xingmu.mapper.ProgramCategoryMapper;
import top.ashher.xingmu.redis.cache.RedisCache;
import top.ashher.xingmu.redis.key.RedisKeyBuild;
import top.ashher.xingmu.redis.key.RedisKeyManage;
import top.ashher.xingmu.redisson.lockinfo.LockType;
import top.ashher.xingmu.redisson.servicelock.annotion.ServiceLock;
import top.ashher.xingmu.vo.ProgramCategoryVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.PROGRAM_CATEGORY_LOCK;

@Service
public class ProgramCategoryService extends ServiceImpl<ProgramCategoryMapper, ProgramCategory> {

    @Autowired
    private ProgramCategoryMapper programCategoryMapper;

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询所有节目类型
     * */
    public List<ProgramCategoryVo> selectAll(){
        QueryWrapper<ProgramCategory> lambdaQueryWrapper = Wrappers.emptyWrapper();
        List<ProgramCategory> programCategoryList = programCategoryMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(programCategoryList,ProgramCategoryVo.class);
    }

    public List<ProgramCategoryVo> selectByType(ProgramCategoryDto programCategoryDto) {
        LambdaQueryWrapper<ProgramCategory> lambdaQueryWrapper = Wrappers.lambdaQuery(ProgramCategory.class)
                .eq(ProgramCategory::getType, programCategoryDto.getType());
        List<ProgramCategory> programCategories = programCategoryMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(programCategories,ProgramCategoryVo.class);
    }

    public List<ProgramCategoryVo> selectByParentProgramCategoryId(ParentProgramCategoryDto parentProgramCategoryDto) {
        LambdaQueryWrapper<ProgramCategory> lambdaQueryWrapper = Wrappers.lambdaQuery(ProgramCategory.class)
                .eq(ProgramCategory::getParentId, parentProgramCategoryDto.getParentProgramCategoryId());
        List<ProgramCategory> programCategories = programCategoryMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(programCategories,ProgramCategoryVo.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @ServiceLock(lockType= LockType.Write,name = PROGRAM_CATEGORY_LOCK,keys = {"all"})
    public void saveBatch(final List<ProgramCategoryAddDto> programCategoryAddDtoList) {
        List<ProgramCategory> programCategoryList = programCategoryAddDtoList.stream().map((programCategoryAddDto) -> {
            ProgramCategory programCategory = new ProgramCategory();
            BeanUtil.copyProperties(programCategoryAddDto, programCategory);
            programCategory.setId(uidGenerator.getUID());
            return programCategory;
        }).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(programCategoryList)) {
            this.saveBatch(programCategoryList);
            Map<String, ProgramCategory> programCategoryMap = programCategoryList.stream().collect(
                    Collectors.toMap(p -> String.valueOf(p.getId()), p -> p, (v1, v2) -> v2));
            redisCache.putHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_CATEGORY_HASH),programCategoryMap, 30 , TimeUnit.DAYS);
        }

    }

    public ProgramCategory getProgramCategory(Long programCategoryId){
        RedisKeyBuild key = RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_CATEGORY_HASH);
        ProgramCategory programCategory = redisCache.getForHash(key, String.valueOf(programCategoryId), ProgramCategory.class);
        if (!Objects.isNull(programCategory)) {
            return programCategory;
        }
        if (redisCache.hasKey(key)) {
            // Redis 既然有这个 Hash 结构，说明已经加载过了。
            // 现在 Hash 里没有这个 field，证明数据库里真没有。
            return null; // 直接返回，不要去 reload！
        }
        Map<String, ProgramCategory> map = programCategoryRedisDataInit();
        return map.get(String.valueOf(programCategoryId));
    }

    @ServiceLock(lockType= LockType.Write,name = PROGRAM_CATEGORY_LOCK,keys = {"#all"})
    public Map<String, ProgramCategory> programCategoryRedisDataInit(){
        RedisKeyBuild key = RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_CATEGORY_HASH);
        Map<String, ProgramCategory> cachedMap = redisCache.getAllMapForHash(key, ProgramCategory.class);
        if (CollectionUtil.isNotEmpty(cachedMap)) {
            return cachedMap;
        }
        Map<String, ProgramCategory> programCategoryMap = new HashMap<>(64);
        QueryWrapper<ProgramCategory> lambdaQueryWrapper = Wrappers.emptyWrapper();
        List<ProgramCategory> programCategoryList = programCategoryMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(programCategoryList)) {
            programCategoryMap = programCategoryList.stream().collect(
                    Collectors.toMap(p -> String.valueOf(p.getId()), p -> p, (v1, v2) -> v2));
            redisCache.putHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_CATEGORY_HASH),programCategoryMap, 30 , TimeUnit.DAYS);
        }
        return programCategoryMap;
    }
}
