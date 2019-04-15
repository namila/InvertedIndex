package DataMining.InvertedIndex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Demo {
	
	public static void main(String[] args) throws IOException {
		File menuDirectory = new File("menuSet");
		File[] menuSet = menuDirectory.listFiles();
		InvertedIndexer indexer = InvertedIndexer.getInstanceOf();

		for(int i = 0; i < menuSet.length ; ++i){
			System.out.println(i + " Menu "+ menuSet[i].getName());
		}
		
		for(int i = 0; i < menuSet.length ; ++i){
			indexer.addFileToInvertedIndex(menuSet[i], i);
		}
		
		indexer.printInvertedIndex();
		
		ArrayList<Integer> searchResults = indexer.searchForTerms("medium", "pie");
		System.out.println(searchResults);
		
	}

}
