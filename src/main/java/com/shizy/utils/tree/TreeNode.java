package com.shizy.utils.tree;

import lombok.Data;

import java.util.List;

@Data
public class TreeNode<T> {

    private String id;

    //for tree
    private String parentId;
    private List<TreeNode> children;//for tree and UI tree
    private T item;

    //for data
    private String key;

    //for UI tree
    private String title;

    public TreeNode(String id, String parentId, T item, String key, String title) {
        this.id = id;
        this.parentId = parentId;
        this.item = item;
        this.key = key;
        this.title = title;
    }

}
