package DataMining.InvertedIndex;

import java.io.File;
import java.io.IOException;

public class Demo {
	
	public static void main(String[] args) throws IOException {
		File menuDirectory = new File("menuSet");
		File[] menuSet = menuDirectory.listFiles();
		InvertedIndexer indexer = InvertedIndexer.getInstanceOf();

		indexer.addFileToInvertedIndex(menuSet[1]);
//		for(File currentFile : menuSet){
//			indexer.addFileToInvertedIndex(currentFile);
//		}

	}

}
