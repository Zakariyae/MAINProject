package MainTP1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;


public class Graphe{

	public static class Arc 
	{
		private Integer sommet;

		public Arc(Integer sommet) 
		{
			this.sommet = sommet;
		}
		
		public void setSommet(Integer sommet) 
		{
			this.sommet = sommet;
		}

		public Integer getSommet()
		{
			return sommet;
		}
		
		@Override
		public String toString() {
			return "Arc avec (Sommet = " + sommet + ")";
		}
	}

	private Map<Integer, List<Arc>> voisins = new HashMap<Integer, List<Arc>>();
	private static float[][] matriceStoch;
	private static InputStream ips;
	private static InputStreamReader ipsr;
	private static BufferedReader br;
	
	
	public Map<Integer, List<Arc>> getVoisins()
	{
		return voisins;
	}
	
	public void setVoisins(Map<Integer, List<Arc>> voisins) 
	{
		this.voisins = voisins;
	}

	public void ajouter(Integer sommet) 
	{
		if (voisins.containsKey(sommet))
			return;
		voisins.put(sommet, new ArrayList<Arc>());
	}

	public boolean contient(Integer sommet)
	{
		return voisins.containsKey(sommet);
	}

	public void ajouter(Integer de, Integer a) 
	{
		this.ajouter(de);
		this.ajouter(a);
		voisins.get(de).add(new Arc(a));
	}

	public int degreSortant(Integer integer) 
	{
		return voisins.get(integer).size();
	}

	public int degreEntrant(Integer sommet) 
	{
		return noeudEntrant(sommet).size();
	}

	public List<Integer> noeudSortant(Integer sommet)
	{
		List<Integer> list = new ArrayList<Integer>();
		for (Arc e : voisins.get(sommet))
			list.add(e.sommet);
		return list;
	}

	public List<Integer> noeudEntrant(Integer sommet) 
	{
		List<Integer> listIn = new ArrayList<Integer>();
		for (Integer a : voisins.keySet())
		{
			for (Arc e : voisins.get(a))
				if (e.sommet.equals(sommet))
					listIn.add(a);
		}
		return listIn;
	}

	public boolean isArc(Integer de, Integer a)
	{
		for (Arc e : voisins.get(de)) 
		{
			if (e.sommet.equals(a))
				return true;
		}
		return false;
	}
	
	public static Graphe chargementGraphe(String nomFichier) throws IOException
	{
		Graphe graph = new Graphe();
		String line;
		String[] results;
		try
		{
			ips = new FileInputStream(nomFichier);
			ipsr = new InputStreamReader(ips);
			br = new BufferedReader(ipsr);
			while ((line = br.readLine()) != null)
			{
				if(line.charAt(0) == '#')
					continue;
				results = line.split("\t");
				if (!graph.voisins.keySet().contains(Integer.parseInt(results[0])))
					graph.ajouter(Integer.parseInt(results[0]));
				graph.ajouter(Integer.parseInt(results[0]),Integer.parseInt(results[1]));
			}
			for (Integer source : graph.voisins.keySet()) 
			{
				for (Integer destination : graph.noeudSortant(source)) 
				{
					if (graph.isArc(source, destination)) 
					{
						matriceStoch[source][destination] = (float)(1/graph.degreSortant(source));
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			br.close();
		}
		return graph;
	}
	
	public void delete(Arc a, List<Integer> noeudEntrant) 
	{
		for (Integer NEnt : noeudEntrant) 
		{			
			for(int i = 0; i<voisins.get(NEnt).size(); i++)
			{
				if(a.toString().equals(voisins.get(NEnt).get(i).toString()))
				{
					voisins.get(NEnt).remove(i);
				}
			}
		}
		voisins.get(a.getSommet()).clear();
		voisins.remove(a.getSommet());
	}
	
	public static void main(String[] args) 
	{
		args = new String[1];
		args[0] = "src/MainTP1Files/Example.txt";
		if(args.length == 1)
		{
			try {
				Graphe graphe = Graphe.chargementGraphe(args[0]);
				for(Integer d : graphe.getVoisins().keySet())
				{
					System.out.println("Degree Entrant pour le sommet " + d + " est " + graphe.degreEntrant(d));
					System.out.println("Degree Sortant pour le sommet " + d + " est " + graphe.degreSortant(d));
					System.out.println("Noeuds Entrant pour le sommet " + d + " est " + graphe.noeudEntrant(d));
					System.out.println("Noeuds Sortant pour le sommet " + d + " est " + graphe.noeudSortant(d));
					System.out.println("---------------------------------------------------------------------------------------");
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
