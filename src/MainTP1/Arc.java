package MainTP1;

public class Arc 
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
