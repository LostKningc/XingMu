package top.ashher.xingmu.shardingsphere;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.util.*;

public class DatabaseOrderComplexGeneArithmetic implements ComplexKeysShardingAlgorithm<Long> {
    private static final String SHARDING_COUNT_KEY_NAME = "sharding-count";

    private static final String TABLE_SHARDING_COUNT_KEY_NAME = "table-sharding-count";

    private int shardingCount;

    private int tableShardingCount;

    @Override
    public void init(Properties props) {
        this.shardingCount = Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY_NAME));
        this.tableShardingCount = Integer.parseInt(props.getProperty(TABLE_SHARDING_COUNT_KEY_NAME));
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> shardingValue) {
        Set<Long> logicValues = new HashSet<>();
        Map<String, Collection<Long>> valueMap = shardingValue.getColumnNameAndShardingValuesMap();

        if (CollectionUtil.isNotEmpty(valueMap.get("order_number"))) {
            logicValues.addAll(valueMap.get("order_number"));
        } else if (CollectionUtil.isNotEmpty(valueMap.get("user_id"))) {
            logicValues.addAll(valueMap.get("user_id"));
        }

        if (logicValues.isEmpty()) return availableTargetNames;

        Set<String> result = new HashSet<>();

        for (Long val : logicValues) {
            long dbIndex = (val / tableShardingCount) % shardingCount;
            String dbIndexStr = String.valueOf(dbIndex);
            for (String target : availableTargetNames) {
                if (target.endsWith("_" + dbIndexStr)) {
                    result.add(target);
                }
            }
        }
        return result;
    }
}
