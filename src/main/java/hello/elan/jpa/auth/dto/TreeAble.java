package hello.elan.jpa.auth.dto;

import hello.elan.jpa.auth.enums.NodeType;

import java.util.Set;

/**
 * 树状结构体标准定义
 * @author ElanZh
 * @date 2019/5/8 11:31
 */
public interface TreeAble<T extends TreeAble> {
    /** ID */
    Integer getId();
    /** 父ID */
    Integer getPId();
    /** 类型：NODE节点；LEAF叶子； */
    NodeType getType();
    /** 设置子节点集合 */
    void setChildren(Set<T> children);
    /** 获取子节点集合 */
    Set<T> getChildren();
}
