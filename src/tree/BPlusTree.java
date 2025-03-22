package tree;
import java.util.*;

public class BPlusTree {

    private BPlusTreeNode root; // root node of the tree
    private final int order;    // Maximum number of keys per node (4 keys, 5 children)
    private boolean printTreeEnabled = true; // Toggle for printing
    
    // Constructor: B+ Tree of a given order (max children = order)
    public BPlusTree(int order) {
        if (order != 5) {
            throw new IllegalArgumentException("Order must be 5.");
        }
        //create an initial root which is a leaf 
        this.root = new BPlusTreeNode(true); //start with a leaf node as the root
        this.order = order;
    }

    // Insert a key into the B+ Tree (skip duplicates)
    public void insert(int key) {
    	//locates the correct leaf node for the key
        BPlusTreeNode leaf = findLeaf(key);

        // Insert into leaf only if the key is not already present (avoids duplicate)
        insertIntoLeaf(leaf, key);

        // If leaf is full, split
        if (leaf.keys.size() > (order - 1)) {
            splitLeaf(leaf);
        }
        
     // Print the tree only if enabled
        if (printTreeEnabled) {
            printTree();
        }
    }

    // Search for a key in the B+ Tree
    public boolean search(int key) {
        BPlusTreeNode node = findLeaf(key);
        int pos = Collections.binarySearch(node.keys, key);
        if(pos>=0) {
        	return true;
        }
        return false;
    }

    // Print the B+ Tree structure
    public void printTree() {
        printNode(root, 0);
        System.out.print("Number of leaf nodes: " + getLeafCount() + "\n");
    }
    
    // Toggle printing ON/OFF
    public void setPrintTreeEnabled(boolean status) {
        this.printTreeEnabled = status;
    }

    // ---------------------- Private helper methods ----------------------

    // Find the appropriate leaf node for a given key
    private BPlusTreeNode findLeaf(int key) {
        BPlusTreeNode node = root;
        //traverse the internal nodes
        while (!node.isleaf) {
            int i = 0;
            //find the correct child pointer
            while (i < node.keys.size() && key >= node.keys.get(i)) {
                i++;
            }
            //move to next node
            node = node.children.get(i);
        }
        //returns the leaf node where the key should be inserted
        return node;
    }

    // Insert a key into a leaf node (with duplicate check)
    private void insertIntoLeaf(BPlusTreeNode leaf, int key) {
        // Binary search to find correct position
        int pos = Collections.binarySearch(leaf.keys, key);
        if (pos >= 0) {
            // Key already exists, skip insertion
            System.out.println("Duplicate key " + key + " not inserted.");
            return;
        }
        // Compute insertion point
        pos = -(pos + 1);
        leaf.keys.add(pos, key);
    }

    // Split a leaf node that has become full
    private void splitLeaf(BPlusTreeNode leaf) {
        // 'order' = max children; so max keys = order - 1. full means we have order keys.
        // We choose a midpoint so each side has at least floor((order-1)/2) keys.
        int mid = (order) / 2;  
        // For order=5 => mid=2 => left keeps 2 keys, right gets 3 keys (if leaf has 5 keys).

        BPlusTreeNode newLeaf = new BPlusTreeNode(true); //create a new leaf node

        // Move right half of the keys into newLeaf
        newLeaf.keys.addAll(leaf.keys.subList(mid, leaf.keys.size()));
        leaf.keys.subList(mid, leaf.keys.size()).clear();

        // Link the leaf nodes
        newLeaf.next = leaf.next;
        leaf.next = newLeaf;

        // If leaf was root, make a new root
        if (leaf == root) {
            BPlusTreeNode newRoot = new BPlusTreeNode(false);
            // The new key in the parent is the first key of the new leaf
            newRoot.keys.add(newLeaf.keys.get(0));
            newRoot.children.add(leaf);
            newRoot.children.add(newLeaf);
            root = newRoot;
        } else {
            // Insert separator key into parent
            insertIntoParent(leaf, newLeaf, newLeaf.keys.get(0));
        }
    }

    // Insert a new child (left, right) and a key into the parent
    private void insertIntoParent(BPlusTreeNode left, BPlusTreeNode right, int key) {
        BPlusTreeNode parent = findParent(root, left);

        // If for some reason we can't find a parent, it means 'left' was root
        if (parent == null) {
            // Create new root
            BPlusTreeNode newRoot = new BPlusTreeNode(false);
            newRoot.keys.add(key);
            newRoot.children.add(left);
            newRoot.children.add(right);
            root = newRoot;
            return;
        }

        // Otherwise, insert key into the parent
        int pos = Collections.binarySearch(parent.keys, key);
        if (pos >= 0) {
            // This theoretically shouldn't happen if we handle duplicates carefully,
            // but if it does, just ignore to avoid duplicates.
            return;
        }
        pos = -(pos + 1);

        parent.keys.add(pos, key);
        parent.children.add(pos + 1, right); //the right child is inserted at pos+1 in parent.children

        // If parent is overfull, split it
        if (parent.keys.size() > (order - 1)) {
            splitInternal(parent);
        }
    }

    // Split an internal node that became overfull
    private void splitInternal(BPlusTreeNode internal) {
        // We have order children max, hence up to (order - 1) keys. Overfull => we have 'order' keys.
        // We pick mid = order/2 to split so that left has mid keys, right has the remainder,
        // and the "middle key" gets promoted up.

        int mid = (order) / 2; 
        // The key to move up
        int middleKey = internal.keys.get(mid);

        // Create the new node (right sibling)
        BPlusTreeNode newInternal = new BPlusTreeNode(false);

        // Move keys after mid into new node
        newInternal.keys.addAll(internal.keys.subList(mid + 1, internal.keys.size()));
        // Move children after mid into new node
        newInternal.children.addAll(internal.children.subList(mid + 1, internal.children.size()));

        // Clear moved elements from the original node
        internal.keys.subList(mid, internal.keys.size()).clear(); 
        internal.children.subList(mid + 1, internal.children.size()).clear();

        // If 'internal' was the root, we create a new root
        if (internal == root) {
            BPlusTreeNode newRoot = new BPlusTreeNode(false);
            newRoot.keys.add(middleKey);
            newRoot.children.add(internal);
            newRoot.children.add(newInternal);
            root = newRoot;
        } else {
            // Insert the promoted key into the parent
            insertIntoParent(internal, newInternal, middleKey);
        }
    }

    // Find the parent node of a given (child) node
    private BPlusTreeNode findParent(BPlusTreeNode current, BPlusTreeNode target) {
        if (current.isleaf || current.children.isEmpty()) {
            return null;
        }
        for (int i = 0; i < current.children.size(); i++) {
            BPlusTreeNode child = current.children.get(i);
            if (child == target) {
                return current;
            }
            BPlusTreeNode result = findParent(child, target);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    
    public int getLeafCount() {
        // Start from the leftmost leaf.
        BPlusTreeNode current = root;
        while (!current.isleaf) {
            current = current.children.get(0);
        }
        int count = 0;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    private void printNode(BPlusTreeNode node, int level) {
        // Indent by 2 spaces per level
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }

        // Print the keys in bracketed form
        System.out.print("[ ");
        for (int i = 0; i < node.keys.size(); i++) {
            System.out.print(node.keys.get(i));
            if (i < node.keys.size() - 1) {
                System.out.print(" , ");
            }
        }
        System.out.println(" ]");

        // Recurse for children
        if (!node.isleaf) {
            for (BPlusTreeNode child : node.children) {
                printNode(child, level + 1);
            }
        }
    }
}
