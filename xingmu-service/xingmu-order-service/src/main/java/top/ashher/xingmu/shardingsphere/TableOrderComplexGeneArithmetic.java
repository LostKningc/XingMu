package top.ashher.xingmu.shardingsphere;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.util.*;

public class TableOrderComplexGeneArithmetic implements ComplexKeysShardingAlgorithm<Long> {


    private static final String SHARDING_COUNT_KEY_NAME = "sharding-count";

    private int shardingCount;

    @Override
    public void init(Properties props) {
        shardingCount = Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY_NAME));
    }

    @Override
    public Collection<String> doSharding(Collection<String> allActualSplitTableNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        Set<String> actualTableNames = new HashSet<>(); // 使用 Set 去重
        String logicTableName = complexKeysShardingValue.getLogicTableName();

        Collection<Long> orderNumberValues = complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("order_number");
        Collection<Long> userIdValues = complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("user_id");

        List<Long> valuesToRoute = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(orderNumberValues)) {
            valuesToRoute.addAll(orderNumberValues);
        } else if (CollectionUtil.isNotEmpty(userIdValues)) {
            valuesToRoute.addAll(userIdValues);
        }

        if (valuesToRoute.isEmpty()) {
            return allActualSplitTableNames; // 全库扫描
        }

        for (Long val : valuesToRoute) {
            String suffix = String.valueOf((shardingCount - 1) & val);
            actualTableNames.add(logicTableName + "_" + suffix);
        }
        return actualTableNames;
    }
}
