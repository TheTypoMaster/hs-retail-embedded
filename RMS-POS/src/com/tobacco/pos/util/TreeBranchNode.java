package com.tobacco.pos.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class TreeBranchNode extends TreeNode {

	//�洢�ӽڵ�
	List subNodesList = new ArrayList();

	public TreeBranchNode(int nodeId, String nodeName, int pId, int level, String comment) {
		this.setId(nodeId);
		this.setName(nodeName);
		this.setPId(pId);
		this.setLevel(level);
		this.setComment(comment);
	}

	//����ӽڵ�
	public void addSubNode(TreeNode menuComponent) {
		// ���ø��ڵ�
		menuComponent.setPMenuComponent(this);

		// ���ýڵ�����
		menuComponent.setLevel(this.getLevel() + 1);
		subNodesList.add(menuComponent);
	}

	//ɾ��һ���ӽڵ�
	public void removeSubNode(TreeNode menuComponent) {
		subNodesList.remove(menuComponent);
	}

	//��ȡ�ӽڵ�
	public List getSubNodes() {
		return subNodesList;
	}

	//��ӡ�ڵ���Ϣ���������ʽչʾ���������(�������ӽڵ���Ϣ
	public String print() {
		
//		System.out.println(this.getNodeInfo());
		return this.getNodeInfo().toString();
	}

	//��ӡ�ڵ㱾����Ϣ�����ݹ��ӡ�ӽڵ���Ϣ
	public String toString() {
		return getSefNodeInfo().toString();
	}

	// �ݹ��ӡ�ڵ���Ϣʵ��
	protected StringBuffer getNodeInfo() {

		StringBuffer sb = getSefNodeInfo();
		sb.append(System.getProperty("line.separator"));
		//������ӽڵ�
		for (Iterator iter = subNodesList.iterator(); iter.hasNext();) {
			TreeNode node = (TreeNode) iter.next();
			//�ݹ��ӡ�ӽڵ���Ϣ
			sb.append(node.getNodeInfo());

			if (iter.hasNext()) {
				sb.append(System.getProperty("line.separator"));
			}

		}
		return sb;
	}

	//�ڵ㱾����Ϣ�������ӽڵ���Ϣ
	private StringBuffer getSefNodeInfo() {
		StringBuffer sb = new StringBuffer();

		sb.append("[nodeId=");
		sb.append(this.getId());
		sb.append(" nodeName=");

		sb.append(this.getName());
		sb.append(" pId=");
		sb.append(this.getPId());
		sb.append(" level=");
		sb.append(this.getLevel());
		sb.append(" comment=");
		sb.append(this.getComment());
		sb.append(']');
		return sb;
	}

	//Ϊ����ṩ������Ͻṹ�ĵ����
	public Iterator createDepthOrderIterator() {
		return new TreeOutOrder.DepthOrderIterator(this);
	}


	/**
	 * ʹ������������ݹ鷽ʽ����ָ���Ľڵ�
	 * 
	 * @param treeNode ���ҵ���ʼ�ڵ�
	 * @param treeId �ڵ���
	 * @return
	 */
	protected TreeNode getNode(TreeNode treeNode, int treeId) {

		//����ҵ�����ֹͣ����������Ѳ��ҵ��Ľڵ㷵�ظ��ϲ������
		if (treeNode.getId() == treeId) {//1�����븸�ڵ�ȶ�
			return treeNode;
		}

		TreeNode tmp = null;

		//���Ϊ��֧�ڵ㣬������ӽڵ�
		if (treeNode instanceof TreeBranchNode) {

			for (int i = 0; i < treeNode.getSubNodes().size(); i++) {//2�������ӽڵ�ȶ�
				tmp = getNode((TreeNode) treeNode.getSubNodes().get(i), treeId);
				if (tmp != null) {//�����ҵ����򷵻��ϲ������
					return tmp;
				}
			}
		}

		//���û���ҵ��������ϲ������
		return null;
	}
}