package MainTP1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DictConstructor {
	
	private final String wikiURL = "https://fr.wiktionary.org/wiki/Utilisateur:Darkdadaah/Listes/Mots_dump/frwiki/2016-02-03";
	private final String dictFileName = "src/MainTP1Files/dictWIKI.words";
	private final String XMLFileName = "src/MainTP1Files/frwiki.xml";
	
	/**
	 * Require jsoup-1.10.2.jar
	 */
	public void getWikiWords()
	{
		try
		{
			Document doc = Jsoup.connect(wikiURL).get();
			Elements tableElements = doc.select("table");        
			Elements tableRowElements = tableElements.select(":not(thead) tr");
			Set<String> dictArray = new HashSet<String>();
			File dictWords = new File(dictFileName);
			FileWriter fw = new FileWriter(dictWords.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
	        
	        for (int i = 1; i < tableRowElements.size(); i++) 
	        {
	        	Element row = tableRowElements.get(i);
	        	
	        	if (row.select("td > a").text().toLowerCase().length() >= 5) 
	        	{
	        		dictArray.add(row.select("td > a").text().toLowerCase().replace("é", "e").replace("è", "e").replace("ê", "e").replace("à", "a").replace("â", "a"));
	        	}
	        }
	        
	        String[] dictArrayTemp = new String[dictArray.size()];
	        
	        int i = 0;
	        for (String word : dictArray) 
	        {
	        	dictArrayTemp[i] = word;
	        	i++;
			}
	                
	        dictArray.clear();
	        java.util.Arrays.sort(dictArrayTemp);
	        
	        for (int j = 0; j < dictArrayTemp.length; j++) 
	        {
	    		bw.write(dictArrayTemp[j]);
	    		bw.write("\n");
			}
	        
	        bw.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Require commons-lang3-3.5.jar 
	 */
	public Map<String, ArrayList<String>> wordPageRel()
	{
		Map<String, ArrayList<String>> wordPageMap = null;
	    try 
	    {
	    	wordPageMap = new HashMap<String, ArrayList<String>>();
	    	File XMLFile = new File(XMLFileName);
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	org.w3c.dom.Document doc = dBuilder.parse(XMLFile);
	    	doc.getDocumentElement().normalize();
	    	
	    	for (String word : loadDict()) 
			{
				wordPageMap.put(word, new ArrayList<String>());
			}
	    	
	    	NodeList nList = doc.getElementsByTagName("page");

	    	for (int i = 0; i < nList.getLength(); i++) 
	    	{
	    		Node nNode = nList.item(i);
	    			    		
	    		if (nNode.getNodeType() == Node.ELEMENT_NODE)
	    		{
	    			org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
	    			String ID = eElement.getElementsByTagName("id").item(0).getTextContent();
	    			String unescapedText = StringEscapeUtils.unescapeHtml4(eElement.getElementsByTagName("text").item(0).getTextContent().toLowerCase().replace("é", "e").replace("è", "e").replace("ê", "e").replace("à", "a").replace("â", "a"));	    			
	    			for (String word : loadDict()) 
	    			{
	    				int j = 0;
	    				Pattern p = Pattern.compile(word);
	    				Matcher m = p.matcher(unescapedText);
	    				while (m.find())
	    				{
	    				    j++;
	    				}
	    				//System.out.println(word + " -> [" + ID + " -- " + j + "]");
	    				wordPageMap.get(word).add(ID + " - " + j);
					}
	    		}
	    	}
	    	
	    	for (String word : wordPageMap.keySet()) 
	    	{
				for (String value : wordPageMap.get(word))
				{
					System.out.println("The word '" + word + "' appears in (page - occurences) " + value);
				}
			}
	    				
	    } catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
		return wordPageMap;
	}
	
	private Set<String> loadDict()
	{
		Set<String> dictArray = null;
		try 
		{
			dictArray = new HashSet<String>();
			Scanner dictFileReader = new Scanner(new BufferedReader(new FileReader(dictFileName)));
			while (dictFileReader.hasNext()) 
			{
				dictArray.add(dictFileReader.nextLine());
			}
			dictFileReader.close();
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return dictArray;
	}
			
	public static void main(String[] args)
	{
		DictConstructor dict = new DictConstructor();
		dict.getWikiWords();
		dict.wordPageRel();
	}
	
}
