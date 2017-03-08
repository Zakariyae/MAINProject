package MainTP1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DictConstructor {
	
	private final String wikiURL = "https://fr.wiktionary.org/wiki/Utilisateur:Darkdadaah/Listes/Mots_dump/frwiki/2016-02-03";
	
	
	public void getWikiWords() throws IOException
	{
		Document doc = Jsoup.connect(wikiURL).get();
        Elements tableElements = doc.select("table");        
        Elements tableRowElements = tableElements.select(":not(thead) tr");
        File dictWords = new File("src/MainTP1Files/dictWIKI.words");
		FileWriter fw = new FileWriter(dictWords.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
        
        for (int i = 0; i < tableRowElements.size(); i++) 
        {
        	Element row = tableRowElements.get(i);
        	if (i != 0 && row.select("td > a").text().toLowerCase().length() >= 5) 
        	{
        		bw.write(row.select("td > a").text().toLowerCase().replace("é", "e").replace("è", "e").replace("ê", "e").replace("à", "a"));
        		bw.write("\n");
        	}
        }
        
        bw.close();
	}
		
	public static void main(String[] args)
	{
		DictConstructor dict = new DictConstructor();
		try 
		{
			dict.getWikiWords();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
}
