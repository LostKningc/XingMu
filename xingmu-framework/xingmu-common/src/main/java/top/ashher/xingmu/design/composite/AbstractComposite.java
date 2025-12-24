package top.ashher.xingmu.design.composite;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 抽象组合节点，定义了组合模式的基本行为和结构
 * @param <T> 执行时传入的参数类型
 */
@Getter
public abstract class AbstractComposite<T> {

    /**
     * 建议改为 private，通过 getter 获取，保护内部结构
     */
    protected final List<AbstractComposite<T>> children = new ArrayList<>();

    /**
     * 对外暴露的统一执行入口（模板方法）
     * 该方法定义了标准流程：先执行自己，再执行子节点
     */
    public final void execute(T param) {
        // 1. 执行当前节点的业务逻辑
        this.doExecute(param);

        for (AbstractComposite<T> child : children) {
            child.execute(param);
        }
    }

    /**
     * 具体的业务逻辑，交由子类实现
     * (对应原来的 execute 方法)
     */
    protected abstract void doExecute(T param);

    /**
     * 添加子节点，并自动维护排序
     * 解决“顺序失效”问题
     */
    public void add(AbstractComposite<T> child) {
        this.children.add(child);
        this.children.sort(Comparator.comparingInt(AbstractComposite::executeOrder));
    }



    public abstract String type();

    // 定义父节点的执行顺序标识
    public abstract Integer executeParentOrder();

    // 定义当前节点的层级顺序标识
    public abstract Integer executeTier();

    // 定义当前节点的执行顺序标识
    public abstract Integer executeOrder();

}
