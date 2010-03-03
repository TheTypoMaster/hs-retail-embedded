package com.tobacco.pos.util;

import java.util.Iterator;
import java.util.List;

public abstract class TreeNode implements Comparable {

	//父节点
	private TreeNode pNode;

	//数据域，节点编号，不能修改
	private int id;

	//数据域，节点名字，能修改
	private String name;

	//节点深度，根默认为0
	private int level;
	
 
	public int pId;
 
	public String comment;

	public TreeNode getPMenuComponent() {
		return pNode;
	}

	public void setPMenuComponent(TreeNode menuComponent) {
		pNode = menuComponent;
	}

	 
	public TreeNode getPNode() {
		return pNode;
	}

	public void setPNode(TreeNode node) {
		pNode = node;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPId() {
		return pId;
	}

	public void setPId(int id) {
		pId = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	//添加子节点 默认不支持，叶子节点不支持此功能
	public void addSubNode(TreeNode menuComponent) {
		throw new UnsupportedOperationException();
	}

	//删除子节点 默认不支持，叶子节点不支持此功能
	public void removeSubNode(TreeNode menuComponent) {
		throw new UnsupportedOperationException();
	}

	//修改节点信息
	public void modiNodeInfo(String nodeName) {
		this.setName(nodeName);
	}

	//获取子节点 默认不支持，叶子节点不支持此功能
	public List getSubNodes() {
		throw new UnsupportedOperationException();
	}

	//打印节点信息
	public String print() {
		throw new UnsupportedOperationException();
	}

	//获取节点信息
	protected abstract StringBuffer getNodeInfo();

	//提供深度迭代器 默认不支持，叶子节点不支持此功能
	public Iterator createDepthOrderIterator() {
		throw new UnsupportedOperationException();
	}

 
	public TreeNode getTreeNode(int treeId) {
		return getNode(this, treeId);
	}

	/**
	 * 使用树的先序遍历递归方式查找指定的节点
	 * 
	 * @param treeNode 查找的起始节点
	 * @param treeId 节点编号
	 * @return
	 */
	protected TreeNode getNode(TreeNode treeNode, int treeId) {
		throw new UnsupportedOperationException();
	}

	public int compareTo(Object o) {

		TreeNode temp = (TreeNode) o;

		return this.getId() > temp.getId() ? 1 : (this.getId() < temp
				.getId() ? -1 : 0);
	}

	public boolean equals(Object menuComponent) {

		if (!(menuComponent instanceof TreeNode)) {
			return false;
		}
		TreeNode menu = (TreeNode) menuComponent;

		// 如果两个节点的nodeID相应则认为是同一节点
		return this.getId() == menu.getId();
	}
}