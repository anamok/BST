package Binary_Search_Tree;
import java.io.*;
import java.util.*;

public class BST<T extends Comparable<T>> implements BST_Inter<T> {

	public BTNode<T> root;

	public BST() {
		// initiating an empty BST
		this.root = null;
	}

	public void put(T key) {
		// calling the recursive method
		this.root = put_REC(root, key);
	}
	
	private BTNode<T> put_REC(BTNode<T> node, T key) {
		// either root is null or we reached the leaf node
		if (node == null)
			return new BTNode<T>(key);
		// if the key is less than current node's data,
		// we go to the left branch
		else if (key.compareTo(node.getKey()) < 0) {
			node.setLeft(this.put_REC(node.getLeft(), key));
		}
		// if we are here, the key is greater than current
		// node's data, so we go to the right branch
		else {
			node.setRight(this.put_REC(node.getRight(), key));
		}
		// returning the new node pointer
		return node;
	} 

	public boolean contains(T key) {
		// setting up a cursor to traverse the tree node by node
		BTNode<T> cur = this.root;

		// looping through the BST and comparing the key to nodes
		// until the cursor points either at the node with the 
		// right key or null
		while (cur != null) {
			if (key.compareTo(cur.getKey()) == 0)
				return true;
			else if (key.compareTo(cur.getKey()) < 0) {
				cur = cur.getLeft();
			}
			else {
				cur = cur.getRight();
			}
		}
		//if we are here, the key is not in the BST
		return false;
	}

	public void delete(T key) {
		// calling the recursive function
		this.root = this.delete_REC(root, key);
	}

	private BTNode<T> delete_REC(BTNode<T> node, T key) {
		// base case: empty tree
		if (node == null)
			return node;
		// searching for the node to be deleted recursively
		if (key.compareTo(node.getKey()) < 0) {
			node.setLeft(delete_REC(node.getLeft(), key));
		}
		else if (key.compareTo(node.getKey()) > 0) {
			node.setRight(delete_REC(node.getRight(), key));
		}
		else {
			// if we are here, the node either has 1 child
			// or no children
			if (node.getLeft() == null && node.getRight() == null){
				return null;
			}
			else if (node.getLeft() == null) {
				return node.getRight();
			}
			else if (node.getRight() == null) {
				return node.getLeft();
			}
			
			// if we are here, the node has 2 children, so
			// we need to find the largest successor in the 
			// node's left subtree
			BTNode<T> cur = node.getLeft();
			while (cur.getRight() != null) {
				cur = cur.getRight();
			}
			// setting the node's key to the largest successor's value
			node.setKey(cur.getKey());

			// deleting the largest successor via a recursive function			
			node.setLeft(this.deleteLargest(node.getLeft()));
		}

		return node;
	}

	private BTNode<T> deleteLargest(BTNode<T> node) {
		// if there's no right child, this is the largest node
		if (node.getRight() == null) {
			return node.getLeft();
		}
		else {
			// traversing the BST further until we find
			// the largest node
			node.setRight(this.deleteLargest(node.getRight()));
			return node;
		}
	}

	public int height() {
		// calling the recursive function
		return height_REC(this.root);
	}
	
	private int height_REC(BTNode<T> node) {
		//if we reach a null node, we skip it
		if (node == null)
			return 0;
		else {
			//traversing the tree left to right recursively
			int leftHeight = height_REC(node.getLeft());
			int rightHeight = height_REC(node.getRight());

			//choosing the height of the bigger branch to report
			if (rightHeight > leftHeight)
				return (rightHeight + 1);
			else
				return (leftHeight + 1);
		}
	}

	public boolean isBalanced() {
		// calling the recursive function
		return isBalanced_REC(this.root);
	}

	private boolean isBalanced_REC(BTNode<T> node) {
		// base case: node is null
		if (node == null)
			return true;

		// finding the height of the left and the right 
		// subtrees recursively
		int leftHeight = height_REC(node.getLeft());
		int rightHeight = height_REC(node.getRight());

		// checking if the tree is balanced (the left/right heights
		// differ by no more than 1) and if so return true, otherwise 
		// return false
		if (Math.abs(leftHeight - rightHeight) <= 1) 
			return true;

		return false;
	}

	public String inOrderTraversal() {
		// calling the recursive method
		String str = inOrderTraversal_REC(this.root);
		if (str.length() > 0)
			str = str.substring(0, str.length() - 1);
		return str;
	}

	private String inOrderTraversal_REC(BTNode<T> node) {
		String keys = "";
		// traversing the tree from the smallest element 
		// in the left to the greatest element in the right
		if (node != null) {
			keys = keys + this.inOrderTraversal_REC(node.getLeft());
			keys = keys + node.getKey() + ":";
			keys = keys + this.inOrderTraversal_REC(node.getRight());
		}
		
		return keys;
	}

	public String serialize() {
		// base case, tree is empty
		if (this.root == null) {
			return "R(NULL)";
		}
		// if tree is non-empty, calling the recursive function
		else return this.serialize_REC(this.root, this.root);
	}

	private String serialize_REC(BTNode<T> node, BTNode<T> parent) {
		StringBuilder sb = new StringBuilder();
		// checking if the node's parent only has one node (if it's a X type)
		if (node == null && (parent.getLeft() != null || parent.getRight() != null)) {
			return ",X(NULL)";
		}
		// checking if the node's parent is a leaf
		else if (node == null) {
			return "";
		}
		// checking if the node is the root (if it's a R type)
        else if (node == this.root) {
        	sb.append("R(" + node.getKey() + ")");
        }
        // checking if the node is a leaf (if it's a L type)
        else if (node.getLeft() == null && node.getRight() == null) {
        	sb.append(",L(" + node.getKey() + ")");
        }
        // if we are here, the node is an interior node (I type)
        else {
        	sb.append(",I(" + node.getKey() + ")");
        }
        // appending the return string recursively
      	sb.append(serialize_REC(node.getLeft(), node));
       	sb.append(serialize_REC(node.getRight(), node));
        return sb.toString();
	}

	public BST_Inter<T> reverse() { 
		// initializing a new BST (deep copy)
		BST newBST = new BST();
		newBST.root = new BTNode<T>(this.root.getKey());

		// creating the deep copy 
		rev_copy(this.root, newBST.root);

		// calling the recursive reverse method
		reverse_REC(newBST.root);

		return newBST;
	}

	private void rev_copy(BTNode<T> oldRoot, BTNode<T> newRoot) {
		// setting the root of the new BST to the old root
		newRoot.setKey(oldRoot.getKey());

		// copying the left node's data
		if (oldRoot.getLeft() != null) {
			newRoot.setLeft(new BTNode<T>(oldRoot.getLeft().getKey()));
			rev_copy(oldRoot.getLeft(), newRoot.getLeft());
		}

		// copying the right node's data
		if (oldRoot.getRight() != null) {
			newRoot.setRight(new BTNode<T>(oldRoot.getRight().getKey()));
			rev_copy(oldRoot.getRight(), newRoot.getRight());
		}
	}

  	private BTNode<T> reverse_REC(BTNode<T> node) {
  		// base case node is null
    	if (node == null)
      		return node;

     	// calling the recursive methods to traverse the tree
    	BTNode<T> left = reverse_REC(node.getLeft());
    	BTNode<T> right = reverse_REC(node.getRight());

    	// swapping the node pointers
    	node.setLeft(right); 
    	node.setRight(left); 

    	return node; 
  	}
}