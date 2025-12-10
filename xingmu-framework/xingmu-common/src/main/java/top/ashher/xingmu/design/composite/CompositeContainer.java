package top.ashher.xingmu.design.composite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CompositeContainer<T> implements SmartInitializingSingleton, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Map<String, AbstractComposite<T>> rootMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void afterSingletonsInstantiated() {
        // Spring 的 getBeansOfType 只能传 Class，不能传泛型，所以拿回来的是原始类型 Map
        Map<String, AbstractComposite> rawBeans = applicationContext.getBeansOfType(AbstractComposite.class);

        if (CollectionUtils.isEmpty(rawBeans)) {
            return;
        }

        Map<String, AbstractComposite<T>> beans = (Map) rawBeans;

        Map<String, List<AbstractComposite<T>>> groupedByType = beans.values().stream()
                .collect(Collectors.groupingBy(AbstractComposite::type));

        groupedByType.forEach((type, components) -> {
            AbstractComposite<T> root = buildTree(components);
            if (root != null) {
                rootMap.put(type, root);
            }
        });
    }

    public void execute(String type, T param) {
        AbstractComposite<T> root = rootMap.get(type);
        if (root == null) {
            throw new RuntimeException("Composite not found for type: " + type); // 建议自定义异常
        }
        root.execute(param);
    }

    private AbstractComposite<T> buildTree(List<AbstractComposite<T>> components) {
        if (CollectionUtils.isEmpty(components)) {
            return null;
        }
        Map<Integer, AbstractComposite<T>> nodeMap = components.stream()
                .collect(Collectors.toMap(AbstractComposite::executeOrder, Function.identity()));
        List<AbstractComposite<T>> roots = new ArrayList<>();
        for (AbstractComposite<T> node : components) {
            Integer parentId = node.executeParentOrder();
            if (parentId == null || parentId == 0) {
                roots.add(node);
                continue;
            }
            AbstractComposite<T> parent = nodeMap.get(parentId);
            if (parent != null) {
                parent.add(node);
            } else {
                log.warn("Parent not found for node with order {}. Treating as root.", node.executeOrder());
                roots.add(node);
            }
        }

        roots.sort(Comparator.comparingInt(AbstractComposite::executeTier));

        if (roots.size() == 1) {
            return roots.get(0);
        }

        return new VirtualRoot<>(roots);
    }

    private static class VirtualRoot<T> extends AbstractComposite<T> {

        public VirtualRoot(List<AbstractComposite<T>> roots) {
            roots.forEach(this::add);
        }

        @Override
        protected void doExecute(T param) { }
        @Override
        public String type() { return "VIRTUAL_ROOT"; }
        @Override
        public Integer executeOrder() { return -1; } // 排序最前
        @Override
        public Integer executeParentOrder() { return 0; }
        @Override
        public Integer executeTier() { return 0; }
    }
}


