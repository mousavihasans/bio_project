package ABM_Integrin_Clustering;

import sim.engine.SimState;
import sim.engine.Steppable;

public class Ligand implements Steppable{
	private static final long serialVersionUID = 1;

	int turnParity = 0;
	int bindCapacity = 1;
	int numBinds = 0;
	
	public void step(SimState state) {
		IntegrinClustering stat = (IntegrinClustering) state;
		
		//if (turnParity == 1) {
			
		//}
		//turnParity = (turnParity + 1) % 2;
	}

}
