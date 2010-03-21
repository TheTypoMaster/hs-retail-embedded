package com.tobacco.pos.util;

public class TreeLeafNode extends TreeNode {
	public TreeLeafNode(int nodeId, String nodeName, int pId, int level, String comment ) {
		this.setId(nodeId);
		this.setName(nodeName);
		this.setPId(pId);
		this.setLevel(level);
		this.setComment(comment);
	}

	// ��ȡҶ�ӽڵ���Ϣ
	protected StringBuffer getNodeInfo() {
		StringBuffer sb = new StringBuffer();

//		// ��ӡ���
//		for (int i = 0; i < this.getLevel(); i++) {
//			sb.append(' ');
//		}
//		sb.append("---");

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

	public String toString() {
		return getNodeInfo().toString();
	}
}