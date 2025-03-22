package tree;
import java.util.*;

//Internal and leaf nodes
class BPlusTreeNode {
	
   boolean isleaf; // is true for leaf nodes and false for internal nodes
   
   List<Integer> keys; //keys stored in this node
   List<BPlusTreeNode> children; // children nodes for internal nodes
   
   BPlusTreeNode next; //point to the next leaf node (only for the leaf node)
   
   // Constructor to initialize a node
   public BPlusTreeNode(boolean isLeaf) {
       this.isleaf = isLeaf;
       this.keys = new ArrayList<>();
       this.children = new ArrayList<>();
       this.next = null;
   }
   
}
