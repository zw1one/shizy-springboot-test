package com.shizy.utils.tree;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return 度为2的节点list
     */
    protected static List<TreeNode> build(List<TreeNode> treeNodes) {

        List<TreeNode> degree2Nodes = new ArrayList<>();//返回的度为2的节点list

        for (TreeNode treeNode : treeNodes) {

            //步骤A：找到度为2的节点 用于返回展示
            if ("-1".equals(treeNode.getParentId())) {
                degree2Nodes.add(treeNode);
            }

            //步骤B与步骤A没有联系
            //步骤B：给传入的list中，每个节点设置好自己的仅下一层的子节点。
            for (TreeNode it : treeNodes) {
                if (it.getParentId().equals(treeNode.getId())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
        }
        return degree2Nodes;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes 传入的树节点列表
     * @return 度为2的节点list
     */
    protected static List<TreeNode> buildByRecursive(List<TreeNode> treeNodes) {
        List<TreeNode> degree2Nodes = new ArrayList<>();
        for (TreeNode treeNode : treeNodes) {
            //1、找到度为2的节点
            if ("-1".equals(treeNode.getParentId())) {
                //2、递归找到度为2的节点的所有子节点 add
                degree2Nodes.add(findChildren(treeNode, treeNodes));
            }
        }
        return degree2Nodes;
    }

    /**
     * 递归查找子节点
     */
    private static TreeNode findChildren(TreeNode treeNode, List<TreeNode> treeNodes) {
        treeNode.setChildren(new ArrayList<TreeNode>());

        for (TreeNode it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<TreeNode>());
                }
                treeNode.getChildren().add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }

    /**
     * @param data        传入的item list
     * @param getId       item中对应为nodeid的值的get方法
     * @param getParentId item中对应为parentid的值的get方法
     * @return 度为2的节点list node中有item
     */
    private static <T> List<TreeNode> buildTree0(List<T> data, Method getId, Method getParentId, Method getKey, Method getTitle)
            throws InvocationTargetException, IllegalAccessException {

        List<TreeNode> treeNodeList = new ArrayList<>();

        //把item list包装成node list
        for (int i = 0; i < data.size(); i++) {
            T item = data.get(i);
            TreeNode node = new TreeNode(
                    getId.invoke(item).toString(),
                    getParentId.invoke(item).toString(),
                    item,
                    getKey.invoke(item).toString(),
                    getTitle.invoke(item).toString()
            );
            treeNodeList.add(node);
        }
        //build
        return TreeUtil.build(treeNodeList);
    }

    public static <T> List<TreeNode> buildTree(List<T> data, Method getId, Method getParentId, Method getKey, Method getTitle) {
        try {
            return buildTree0(data, getId, getParentId, getKey, getTitle);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {

        List<Perm> data = new ArrayList();
        data.add(new Perm("008", "小奕秘籍", "007", "all"));
        data.add(new Perm("002", "意见反馈", "-1", "all"));
        data.add(new Perm("001", "数据统计", "-1", "all"));
        data.add(new Perm("003", "提问记录", "-1", "all"));
        data.add(new Perm("007", "卡片输入", "005", "all"));
        data.add(new Perm("004", "用户管理", "-1", "all"));
        data.add(new Perm("010", "账号创建", "009", "all"));
        data.add(new Perm("005", "学校管理", "-1", "all"));
        data.add(new Perm("006", "欢迎语", "005", "all"));
        data.add(new Perm("009", "账号管理", "-1", "all"));
        data.add(new Perm("011", "角色设置", "009", "all"));


        List<TreeNode> result = null;
        try {
            result = TreeUtil.buildTree(
                    data,
                    Perm.class.getMethod("getPerm_id"),//for tree id
                    Perm.class.getMethod("getPrevious_perm_id"),//for tree parentId
                    Perm.class.getMethod("getPerm_id"),//for UI key
                    Perm.class.getMethod("getPerm_name")//for UI title
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSON(result).toString());
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Perm {
    private String perm_id;
    private String perm_name;
    private String previous_perm_id;
    private String school_id;
}