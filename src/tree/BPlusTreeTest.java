package tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BPlusTreeTest {
    public static void main(String[] args) {
    	 String filePath = "/Users/helenakamali/Desktop/Winter2025/EECS4411/BPlusTreeProject/src/3000_random_numbers-2.txt"; 
    	 
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

//        tree.insert(2);
//        tree.insert(3);
//        tree.insert(5);
//        tree.insert(7);
//        tree.insert(14);
//        tree.insert(16);
//        tree.insert(19);
//        tree.insert(20);
//        tree.insert(22);
//        tree.insert(4);
//        tree.insert(8);
//        tree.insert(24);
//        tree.insert(27);
//        tree.insert(29);
//        tree.insert(33);
//        tree.insert(34);
//        tree.insert(38);
//        tree.insert(39);
//        tree.insert(9);
//        tree.insert(10);
//    
//        tree.setPrintTreeEnabled(true);
//        System.out.println("Tree after insertion:");
//        tree.printTree();
    
//        //test for search
//        System.out.println("Search for 34: " + tree.search(34));  // True
//        System.out.println("Search for 25: " + tree.search(25));  // False
//        System.out.println("Search for 55: " + tree.search(55));
//        System.out.println("Search for 101: " + tree.search(101));
//    }
    
}