package com.tobacco.pos.util.tree;

import java.util.ArrayList;

import android.util.Log;

public class TreeNode {

	private int id;
	private int pId;
	private  ArrayList<TreeNode> nodes;
	private TreeNode parent;
	private int level;
	private boolean isExpand;
	private boolean checked;
	private boolean selected;
	private boolean visible;
	private String text;
	private String identifier;
	private CommonNode commonNode;
	
	public TreeNode(String text, int id, int pId,int level,String identifier) {
		super();
		this.text = text;
		this.level = level;
		this.isExpand = false;
		this.selected = false;
		this.visible = false;
		this.id = id;
		this.pId = pId;
		this.identifier = identifier;
		// TODO Auto-generated constructor stub
	}
	public TreeNode(String text, int id, String identifier) {
		super();
		this.text = text;
		this.isExpand = false;
		this.selected = false;
		this.visible = false;
		this.id = id;
		this.identifier = identifier;
		// TODO Auto-generated constructor stub
	}
	public TreeNode(boolean isExpand, String text) {
		super();
		this.text = text;
		this.level = 0;
		this.isExpand = isExpand;
		this.visible = false;
	}
	public ArrayList<TreeNode> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<TreeNode> nodes) {
		this.nodes = nodes;
	}
	public TreeNode getParent() {
		return parent;
	}
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isExpand() {
		return isExpand;
	}
	public void setExpand(boolean isExpand) {
		Log.e("testExpand", this.text+".setExpand:"+isExpand);
		this.isExpand = isExpand;
		if(isExpand)
			expand();
		else
			collapse();
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		Log.e("testExpand", this.text+".setVisible:"+visible);
		this.visible = visible;
		if(commonNode!=null){
			commonNode.setVisible(visible);
		}
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
		Log.e("setChecked", this.text+".setChecked:"+checked);
		commonNode.setChecked(checked);
		checkParentState();
		checkSubState();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public void setNode(CommonNode node) {
		Log.e("testExpand", this.text+".setNode:"+node.toString());
		this.commonNode = node;
	}
	
	public boolean hasChild(){
		if(nodes==null)
			return false;
		if(nodes.size()>0)
			return true;
		return false;
	}
	
	public void expand(){
		Log.e("testExpand", this.text+".setSubVisible:"+isExpand);
		for(TreeNode node:nodes){
			node.setVisible(true);
		}
	}
	
	public void collapse(){
		Log.e("testExpand", this.text+"setSubVisible:"+isExpand);
		for(TreeNode node:nodes){
			node.setVisible(false);
		}
	}
	
	public void addNode(TreeNode subNode){
		if(nodes==null){
			nodes = new ArrayList<TreeNode>();
		}
//		subNode.setLevel(this.level+1);
		nodes.add(subNode);
		subNode.setParent(this);
	}
	
	public void remove() throws Exception{
		if(parent==null){
			throw new Exception("�޷�ɾ�����");
		}
		parent.nodes.remove(this);
	}
	
	public boolean checkParentState(){
		if(parent!=null){
			for(TreeNode node:parent.getNodes()){
				if(node.checked!=this.isChecked())
					return true;
			}
			parent.setChecked(this.isChecked());
			parent.checkParentState();
			return false;
		}
		return false;
	}
	
	public boolean checkSubState(){
		if(nodes!=null)
			for(TreeNode node:nodes){
				if(node.isChecked()!=checked)
					node.setChecked(checked);
			}
		return true;
	}

}
