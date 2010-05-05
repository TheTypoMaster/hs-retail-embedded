package com.tobacco.pos.util.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack; 

import android.util.Log;


public class TreeOutOrder {
 
	public static class DepthOrderIterator implements Iterator {
		//ջ,������ȱ������ڵ�,�Ա����
		Stack stack = new Stack();

		public DepthOrderIterator(TreeNode rootNode) {
			ArrayList list = new ArrayList();
			list.add(rootNode);

			// ����ڵ�������ջ
			stack.push(list.iterator());
		}

		//�Ƿ�����һԪ��
		public boolean hasNext() {
			// ���ջΪ���򷵻�,֤��û�пɱ����Ԫ��
			if (stack.empty()) {
				return false;
			} else {
				// ���ջ��Ϊ��,��ȡ��ջ��Ԫ��(�����)
				Iterator iterator = (Iterator) stack.peek();

				// ����ʹ�ü�Ԫ��(���������е�Ԫ��,������״�ṹ��Ԫ��)�ķ�ʽ������
				if (!iterator.hasNext()) {
					// ���ȡ��������Ѿ��������,�򵯳������,�Ա���˵���һ(��)�������������������ȷ�ʽ����
					stack.pop();

					// ͨ��ݹ鷽ʽ�������������δ����Ľڵ�Ԫ��
					return hasNext();
				} else {
					// ����ҵ�����һ��Ԫ��,����true
					return true;
				}
			}
		}

		// ȡ��һԪ��
		public Object next() {
			// �������һ��Ԫ��,����ȡ����Ԫ�����Ӧ�ĵ��������,�Ա�ȡ�øýڵ�Ԫ��
			if (hasNext()) {
				Iterator iterator = (Iterator) stack.peek();
				// ��ȡ�ýڵ�Ԫ��
				TreeNode node = (TreeNode) iterator.next();

				//ֻ�з�֧�ڵ����һ�����ӽڵ���е��
				if (node.hasChild()) {
					stack.push(node.getNodes().iterator());
				}

				// ���ر���õ��Ľڵ�
				Log.e("tree", node.getText());
				return node;
			} else {
				// ���ջΪ��
				return null;
			}
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
	}

	
}