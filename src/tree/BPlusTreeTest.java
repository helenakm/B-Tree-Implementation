package tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BPlusTreeTest {
    public static void main(String[] args) {
    	 String filePath = "/.."; 
    	 
	        List<Integer> numberList = new ArrayList<>();
	        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                if (!line.trim().isEmpty()) { // Ignore empty lines
	                    numberList.add(Integer.parseInt(line.trim()));
	                }
	            }
	        } catch (IOException e) {
	            return;
	        }

	        int[] dataSet = new int[numberList.size()];
	        for (int i = 0; i < numberList.size(); i++) {
	            dataSet[i] = numberList.get(i);
	        }

        BPlusTree tree = new BPlusTree(5);
        
        tree.setPrintTreeEnabled(false);
        try {
            // Insert elements from the dataset into the tree
            for (int key : dataSet) {
                tree.insert(key);
            }

            tree.setPrintTreeEnabled(true);
            System.out.println("Tree after insertion:");
            tree.printTree();

            // Test search functionality
            System.out.println("Search for 34: " + tree.search(34));  // True
            System.out.println("Search for 25: " + tree.search(25));  // False
            System.out.println("Search for 55: " + tree.search(55));  // False
            System.out.println("Search for 101: " + tree.search(101));  // False
        } catch (Exception e) {
            System.out.println("An error occurred while performing tree operations: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
