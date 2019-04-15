package DataMining.InvertedIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;


public class InvertedIndexer 
{
	private static InvertedIndexer instance;
	
	private Map<Dictionary, TreeSet<Integer>> invertedIndex;
    
	private InvertedIndexer() {
		invertedIndex = new HashMap<Dictionary, TreeSet<Integer>>();
	}
	
	public static InvertedIndexer getInstanceOf(){
		if(instance == null){
			instance = new InvertedIndexer();
		}
		return instance;
	}
	
	
	private Dictionary getDictionaryIfPresent(String term){
		Dictionary dictionaryForTerm = null;
		
		for(Dictionary dictionary : invertedIndex.keySet()){
			
			if(dictionary.term.equals(term)){
				dictionaryForTerm = dictionary;
				break;
			}
		}
		
		return dictionaryForTerm;
	}
	
	
	public void addFileToInvertedIndex(File file, int documentId) throws IOException{
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
				currentTerm = currentTerm.replaceAll("[^a-zA-Z0-9\\s]", "").trim().toLowerCase();
				
				if( StringUtils.isWhitespace(currentTerm) || StringUtils.isNumeric(currentTerm) ){
					continue;
				}
				
				Dictionary relaventDictionary = getDictionaryIfPresent(currentTerm);
				
				if (relaventDictionary == null){
					relaventDictionary = new Dictionary(currentTerm);
					invertedIndex.put(relaventDictionary, new TreeSet<Integer>());
				}
				
				relaventDictionary.frequency += 1;
				invertedIndex.get(relaventDictionary).add(documentId);
													
			}
			
			line = bufferedReader.readLine();
		}
		
		inputStreamReader.close();
		bufferedReader.close();		
	}
	
	public ArrayList<Integer> searchForTerms(String term1, String term2){
		ArrayList<Integer> documentsHavingBothTerms = new ArrayList<Integer>();
		int pointer1 = 0;
		int pointer2 = 0;
		Dictionary term1Dictionary = getDictionaryIfPresent(term1.toLowerCase());
		Dictionary term2Dictionary = getDictionaryIfPresent(term2.toLowerCase());
		ArrayList<Integer> term1Postings = new ArrayList<Integer>(invertedIndex.get(term1Dictionary));
		ArrayList<Integer> term2Postings = new ArrayList<Integer>(invertedIndex.get(term2Dictionary));
		
		while(pointer1 < term1Postings.size() && pointer2 < term2Postings.size()){
			
			if(term1Postings.get(pointer1) == term2Postings.get(pointer2)){
				documentsHavingBothTerms.add(term1Postings.get(pointer1));
				pointer1 += 1;
				pointer2 += 1;
			}
			else if (term1Postings.get(pointer1) > term2Postings.get(pointer2)){
				pointer2 += 1;
			}
			else{
				pointer1 += 1;
			}
		}
		return documentsHavingBothTerms;
	}
	
	public void printInvertedIndex(){
		for(Dictionary key : invertedIndex.keySet()){
			System.out.println("Key " + key.term + " Frequency "+ key.frequency);
			System.out.println("Listing");
			
			for(Integer i : invertedIndex.get(key)){
				System.out.println(i);
			}
		}
	}
	
	
}
