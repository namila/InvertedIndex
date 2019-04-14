package DataMining.InvertedIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * Hello world!
 *
 */
public class InvertedIndexer 
{
	private static InvertedIndexer instance;
	
	private Map<String, LinkedList<Integer>> invertedIndex;
    
	private InvertedIndexer() {
		invertedIndex = new HashMap<String, LinkedList<Integer>>();
	}
	
	public static InvertedIndexer getInstanceOf(){
		if(instance == null){
			instance = new InvertedIndexer();
		}
		return instance;
	}
	
	
	public void addFileToInvertedIndex(File file) throws IOException{
		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
		
		InputStream inputStream = new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line = bufferedReader.readLine();
		
		while(line != null){
			CoreDocument document  = new CoreDocument(line);
			pipeline.annotate(document);
			List<CoreLabel> tokens = document.tokens();
			String currentTerm = "";
			
			for(CoreLabel token: tokens){
				currentTerm = token.lemma();
				System.out.println(currentTerm);
				
			}
			
			line = bufferedReader.readLine();
		}
	}
	
	
}
