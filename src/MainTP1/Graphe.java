package MainTP1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class Graphe{

	private Map<Integer, List<Arc>> voisins = new HashMap<Integer, List<Arc>>();
	/*
	 * Matrice Stochastique
	 */
	//private static float[][] matriceStoch;
	@SuppressWarnings("rawtypes")
	private static List Ctable;
	@SuppressWarnings("rawtypes")
	private static List Ltable;
	@SuppressWarnings("rawtypes")
	private static List Itable;
	private int nbrePas;
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
	
	public int getNbrePas() 
	{
		return nbrePas;
	}

	public int degreEntrant(Integer sommet) 
	{
		return noeudEntrant(sommet).size();
	}

	public List<Integer> noeudSortant(Integer sommet)
	{
		List<Integer> list = new ArrayList<Integer>();
		for (Arc e : voisins.get(sommet))
			list.add(e.getSommet());
		return list;
	}

	public List<Integer> noeudEntrant(Integer sommet) 
	{
		List<Integer> listIn = new ArrayList<Integer>();
		for (Integer a : voisins.keySet())
		{
			for (Arc e : voisins.get(a))
				if (e.getSommet().equals(sommet))
					listIn.add(a);
		}
		return listIn;
	}

	public boolean isArc(Integer de, Integer a)
	{
		for (Arc e : voisins.get(de)) 
		{
			if (e.getSommet().equals(a))
				return true;
		}
		return false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
				graph.ajouter(Integer.parseInt(results[0]),Integer.parseInt(results[1]));
			}
			/*
			 * Spécification de la taille de la matrice stochastique
			 */
			//matriceStoch = new float[graph.voisins.keySet().size()][graph.voisins.keySet().size()];
			Ctable = new ArrayList();
			Ltable = new ArrayList();
			Itable = new ArrayList();
			boolean premIter = true;
			
			for (Integer source : graph.voisins.keySet()) 
			{
				for (Integer destination : graph.noeudSortant(source)) 
				{
					/*
					 * Remplissage de la matrice stochastique
					 */
					//matriceStoch[source][destination] = (float) (1.0/graph.degreSortant(source));
					//Ctable.add(matriceStoch[source][destination]);
					Ctable.add((float) (1.0/graph.degreSortant(source)));
					Itable.add(destination);
				}
				if (premIter)
				{
					Ltable.add(0);
					premIter = false;
				}
				Ltable.add(Ctable.size());
			}
			/*
			 * Affichage de la matrice stockastique
			 */
//			for (int i = 0; i < matriceStoch.length; i++)
//			{
//				for (int j = 0; j < matriceStoch[i].length; j++) 
//				{
//					System.out.print(matriceStoch[i][j] + " | ");
//				}
//				System.out.println("\n");
//			}
//			System.out.println("---------------------------------------------------------------------------------------");
			for (int i = 0; i < Ctable.size(); i++) 
			{
				System.out.print(Ctable.get(i) + " ");
			}
			System.out.println("\n---------------------------------------------------------------------------------------");
			for (int i = 0; i < Ltable.size(); i++) 
			{
				System.out.print(Ltable.get(i) + " ");
			}
			System.out.println("\n---------------------------------------------------------------------------------------");
			for (int i = 0; i < Itable.size(); i++) 
			{
				System.out.print(Itable.get(i) + " ");
			}
			System.out.println("\n---------------------------------------------------------------------------------------");
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
	
	public float[] prodScalZero(float[] X)
	{
		float[] Y = new float[voisins.keySet().size()];
		
		for (int i = 0; i < Y.length; i++) 
		{
			for (int j = (int)Ltable.get(i); j < (int)Ltable.get(i+1); j++) 
			{
				Y[i] += (float)Ctable.get(j) * X[(int)Itable.get(j)];
			}
		}
		
		return Y;
	}
	
	public float[] prodScalZap(float[] X, float d)
	{
		float[] Y = new float[voisins.keySet().size()];
		
		for (int i = 0; i < Y.length; i++) 
		{
			for (int j = (int)Ltable.get(i); j < (int)Ltable.get(i+1); j++) 
			{
				Y[(int)Itable.get(j)] += ((d/voisins.keySet().size()) + (1-d)) * (float)Ctable.get(j) * X[i];
			}
		}
		
		/*
		for (int i = 0; i < Y.length; i++)
		{
			Y[i] = (d/voisins.keySet().size()) + (1-d) * Y[i];
		}
		*/
		
		return Y;
	}
	
	public float[] pageRankZero(float epsilon)
	{
		float[] X = new float[voisins.keySet().size()];
		X[0] = 1;
		float[] Y = X;
		float norme = 1;
		nbrePas = 0;
		
        while (norme > epsilon)
        {
            X = prodScalZero(X);
            norme = normalVect(X,Y);
            Y = X;
            nbrePas++;
        }
        
        return X;
	}
	
	public float[] pageRankZap(float epsilon, float d)
	{
		float[] X = new float[voisins.keySet().size()];
		Arrays.fill(X, 1/(float)X.length);
		float[] Y = X;
		float norme = 1;
		nbrePas = 0;
		
        while (norme > epsilon)
        {
            X = prodScalZap(X,d);
            norme = normalVect(X,Y);
            Y = X;
            nbrePas++;
        }
        
        return X;
	}
	
	public float normalVect(float[] X, float[] Y)
	{
		float norme = 0;
	       
        for(int i = 0; i < X.length; i++)
        {
        	norme += (float) Math.pow((X[i] - Y[i]), 2);
        }
        
        return (float) Math.sqrt(norme);
	}
	
	public static void main(String[] args) throws IOException
	{
		if(args.length == 3)
		{
			Graphe graphe = Graphe.chargementGraphe(args[1]);
			for(Integer d : graphe.getVoisins().keySet())
			{
				System.out.println("Degree Entrant pour le sommet " + d + " est " + graphe.degreEntrant(d));
				System.out.println("Degree Sortant pour le sommet " + d + " est " + graphe.degreSortant(d));
				System.out.println("Noeuds Entrant pour le sommet " + d + " est " + graphe.noeudEntrant(d));
				System.out.println("Noeuds Sortant pour le sommet " + d + " est " + graphe.noeudSortant(d));
				System.out.println("---------------------------------------------------------------------------------------");
			}
			if (args[0].equals("-pr0")) 
			{
				System.out.println(Arrays.toString(graphe.pageRankZero(Float.parseFloat(args[2]))));
				System.out.println("Nombre de Pas : " + graphe.getNbrePas());
			}else
				System.out.println("L'un de vos paramètres est invalides…");		
		}
		else if(args.length == 4)
		{
			Graphe graphe = Graphe.chargementGraphe(args[1]);
			for(Integer d : graphe.getVoisins().keySet())
			{
				System.out.println("Degree Entrant pour le sommet " + d + " est " + graphe.degreEntrant(d));
				System.out.println("Degree Sortant pour le sommet " + d + " est " + graphe.degreSortant(d));
				System.out.println("Noeuds Entrant pour le sommet " + d + " est " + graphe.noeudEntrant(d));
				System.out.println("Noeuds Sortant pour le sommet " + d + " est " + graphe.noeudSortant(d));
				System.out.println("---------------------------------------------------------------------------------------");
			}
			if (args[0].equals("-zap")) 
			{
				System.out.println(Arrays.toString(graphe.pageRankZap(Float.parseFloat(args[2]),Float.parseFloat(args[3]))));
				System.out.println("Nombre de Pas : " + graphe.getNbrePas());
			}else
				System.out.println("L'un de vos paramètres est invalides…");
		}else
			System.out.println("Manque de paramètres…");
	}
}
