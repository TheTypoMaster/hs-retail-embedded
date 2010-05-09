package com.tobacco.pos.util;

import java.util.Iterator;
import java.util.List;

public abstract class TreeNode implements Comparable {

	//���ڵ�
	private TreeNode pNode;

	//����򣬽ڵ��ţ������޸�
	private int id;

	//����򣬽ڵ����֣����޸�
	private String name;

	//�ڵ���ȣ���Ĭ��Ϊ0
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

	//����ӽڵ� Ĭ�ϲ�֧�֣�Ҷ�ӽڵ㲻֧�ִ˹���
	public void addSubNode(TreeNode menuComponent) {
		throw new UnsupportedOperationException();
	}

	//ɾ���ӽڵ� Ĭ�ϲ�֧�֣�Ҷ�ӽڵ㲻֧�ִ˹���
	public void removeSubNode(TreeNode menuComponent) {
		throw new UnsupportedOperationException();
	}

	//�޸Ľڵ���Ϣ
	public void modiNodeInfo(String nodeName) {
		this.setName(nodeName);
	}

	//��ȡ�ӽڵ� Ĭ�ϲ�֧�֣�Ҷ�ӽڵ㲻֧�ִ˹���
	public List getSubNodes() {
		throw new UnsupportedOperationException();
	}

	//��ӡ�ڵ���Ϣ
	public String print() {
		throw new UnsupportedOperationException();
	}

	//��ȡ�ڵ���Ϣ
	protected abstract StringBuffer getNodeInfo();

	//�ṩ��ȵ���� Ĭ�ϲ�֧�֣�Ҷ�ӽڵ㲻֧�ִ˹���
	public Iterator createDepthOrderIterator() {
		throw new UnsupportedOperationException();
	}

 
	public TreeNode getTreeNode(int treeId) {
		return getNode(this, treeId);
	}

	/**
	 * ʹ������������ݹ鷽ʽ����ָ���Ľڵ�
	 * 
	 * @param treeNode ���ҵ���ʼ�ڵ�
	 * @param treeId �ڵ���
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

		// ���}��ڵ��nodeID��Ӧ����Ϊ��ͬһ�ڵ�
		return this.getId() == menu.getId();
	}
}