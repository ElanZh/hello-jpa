package hello.elan.jpa.auth;

import hello.elan.jpa.auth.dto.ButtonDto;
import hello.elan.jpa.auth.enums.NodeType;
import hello.elan.jpa.auth.dto.TreeAble;
import hello.elan.jpa.auth.entity.Resource;
import hello.elan.jpa.auth.entity.Role;
import hello.elan.jpa.auth.enums.OpenStatus;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author ElanZh
 * @date 2019/5/8 11:20
 * @Package com.hello.ocean.jpa.tool
 * @Description 数据构建工具
 */
public class DataBuilder {

    /**
     * 将传入的扁平数据变成一颗树
     *
     * @param root 扁平数据中谁是根节点
     * @param list 扁平数据
     * @return root组装好的树
     * @author ElanZh
     * @date 2019/5/8 11:25
     */
    public <T extends TreeAble> T buildTree(@NotNull T root, @NotNull List<T> list) {
        // 移除原始集合中的根节点
        list.remove(root);
        Set<T> parent = new HashSet<>(1);
        parent.add(root);
        Queue<T> queue = new PriorityQueue<>(list);
        // 只要队列不为空，并且还能继续找到子节点，则继续找下个层级的子节点
        do {
            parent = findChildren(parent, queue);
        } while (queue.size() != 0 && parent.size() != 0);
        return root;
    }

    /**
     * 寻找并组装指定parents的子节点
     * 广度优先
     *
     * @param parents 父节点
     * @param q       原始数据
     * @return parents 下各层级的子节点
     * @author ElanZh
     * @date 2019/5/8 11:39
     */
    public <T extends TreeAble> Set<T> findChildren(@NotNull Set<T> parents, @NotNull Queue<T> q) {
        // 返回的子节点个数不会超过原始数据
        Set<T> children = new HashSet<>(q.size());
        if (parents.size() != 0 && q.size() != 0) {
            // 遍历父节点list
            parents.forEach(parent -> {
                // 初始化该parent的子集合
                Set<T> listChild = new HashSet<>(q.size());
                // 寻找父节点的所有子节点，循环次数为原始数据长度
                for (int i = 0; i < q.size(); i++) {
                    // 队列出队
                    T child = q.poll();
                    // child为parent的子 时
                    if (child.getPId().equals(parent.getId())) {
                        listChild.add(child);
                        // 如果child的类型为NODE，则加入出参
                        if (child.getType() == NodeType.NODE) {
                            children.add(child);
                        }
                    }
                    // 如果不是parent的子，则入队
                    else {
                        q.add(child);
                    }
                }
                // 设置parent的子集合
                parent.setChildren(listChild);
            });
        }
        return children;
    }

    /**
     * 1、页面需要找每一个菜单的buttons节点，用来存放下级菜单中所有的按钮，
     * 2、并且children节点中不能有按钮，
     * 3、需要subjectRoles节点，存放该菜单隶属哪些角色名
     *
     * @param parent 只有一个元素，root菜单
     * @return 空集合
     */
    public static void buildMenu(List<Resource> parent) {
        List<Resource> result = new ArrayList<>();
        parent.forEach(parentX -> {
            // 过滤掉关闭状态的子菜单和子按钮
            parentX.setChildren(parentX.getChildren().stream().filter(resource -> resource.getStatus() == OpenStatus.OPEN).collect(Collectors.toSet()));
            // 设置所属角色名
            parentX.setSubjectRoles(parentX.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
            // 设置菜单下的所有按钮
            List<ButtonDto> buttons = new ArrayList<>(parentX.getChildren().size());
            parentX.getChildren().forEach(child -> {
                if (child.getType() == NodeType.LEAF) {
                    buttons.add(new ButtonDto(
                            child.getId(),
                            child.getName(),
                            child.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList())
                    ));
                }
            });
            parentX.setButtons(buttons);
            // 将下级菜单中的按钮过滤掉, 并将纳入其返回集合，以便递归
            Set<Resource> children = parentX.getChildren().stream().filter(resource -> resource.getType() == NodeType.NODE).collect(Collectors.toSet());
            // 使用TreeSet自动按sort字段排序
            parentX.setChildren(children);
            parentX.sortChildren();
            result.addAll(children);
        });
        // 还能找到下级菜单（非按钮），则递归
        if (!CollectionUtils.isEmpty(result)) {
            buildMenu(result);
        }
    }

    /**
     * 遍历一个树，并执行传入操作
     * @param function 执行的操作，lambda：node -> {}
     * @param treeRoot 传入的树
     * @return 空集合
     * @author ElanZh
     * @date 2019/5/11 18:51
     */
    public static <T extends TreeAble> void traverseTree(List<T> treeRoot, Consumer<T> function){
        Objects.requireNonNull(function);
        List<T> nextRoot = new ArrayList<>();
        for (T treeAble : treeRoot) {
            // 执行传入操作
            function.accept(treeAble);
            // 下级节点将纳入其返回集合，以便递归
            nextRoot.addAll(treeAble.getChildren());
        }
        // 还能找到下级节点，则递归
        if (!CollectionUtils.isEmpty(nextRoot)){
            traverseTree(nextRoot, function);
        }
    }


    /**
     * 遍历一个树，并执行传入操作
     * @param function 执行的操作，lambda：node -> {}
     * @param root 树根
     * @author ElanZh
     * @date 2019/5/11 18:51
     */
    public static <T extends TreeAble> void traverseTree(T root, Consumer<T> function){
        ArrayList<T> param = new ArrayList<>(1);
        param.add(root);
        traverseTree(param, function);
    }

}
