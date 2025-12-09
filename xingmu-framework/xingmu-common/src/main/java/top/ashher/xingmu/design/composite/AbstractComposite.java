package top.ashher.xingmu.design.composite;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public abstract class AbstractComposite<T> {

    // 提供 getter 给子类或外部在必要时访问子节点（只读视图更好）
    /**
     * 建议改为 private，通过 getter 获取，保护内部结构
     */
    private final List<AbstractComposite<T>> children = new ArrayList<>();

    /**
     * 对外暴露的统一执行入口（模板方法）
     * 该方法定义了标准流程：先执行自己，再执行子节点
     */
    public final void execute(T param) {
        // 1. 执行当前节点的业务逻辑
        this.doExecute(param);

        // 2. 递归执行子节点 (标准组合模式逻辑)
        // 如果这里需要支持“一旦出错就停止”，可以在这里加 try-catch 或判断
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
        // 每次添加时重新排序，确保 list 永远是有序的
        // 假设构建是一次性的，执行是多次的，这种开销是可以接受的
        this.children.sort(Comparator.comparingInt(AbstractComposite::executeOrder));
    }

    // --- 下面是元数据定义 ---

    public abstract String type();

    /**
     * 如果只有构建树的时候用到这两个方法，
     * 甚至可以考虑把它们移出这个类，只在 Bean 定义或注解中存在。
     * 但保留在这里作为契约也是可以的。
     */
    public abstract Integer executeParentOrder();

    public abstract Integer executeTier();

    public abstract Integer executeOrder();

}
