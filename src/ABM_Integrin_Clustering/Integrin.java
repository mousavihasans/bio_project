package ABM_Integrin_Clustering;

import sim.engine.*;
import sim.util.*;

public class Integrin implements Steppable {
	private static final long serialVersionUID = 1;

	// public boolean randomize = false;
	// public int xdir; // -1, 0, or 1
	// public int ydir; // -1, 0, or 1

	// public Integrin(int xdir, int ydir)
	// {
	// this.xdir = xdir;
	// this.ydir = ydir;
	// }
	
	//Move variables
	static int n_dim = 2;
	static double delta_t = 0.0005;
	static int n_neb = 8;
	static double delta_l = Math.pow(10, -8);
	static double k_b = 1;
	static double temperatue = 310.15;
	static double viscosity = 2;
	static double stokesRadius = 5.7 * Math.pow(10, -9);
	static double d_c = Math.pow(10, -14); //k_b * temperatue / (6 * Math.PI * viscosity * stokesRadius);
	static double p_d = 2 * d_c * n_dim * delta_t / (n_neb * delta_l * delta_l);
	int turn = 0;
	
	//Interaction variables
	int ligandBindCapacity = 1;
	int numLigandBinds = 0;
	double k_d_il = 0.4 * Math.pow(10, -6);
	double k_off_il = 0.072;
	double k_on_il = k_off_il / k_d_il;
	double p_off_il = k_off_il * delta_t;
	int s_type = 1;
	double alpha = 0;
	double n_a = 6.0221413 * Math.pow(10, 23);
	double v_c = 2 * Math.pow(10, -24);
	double p_on_il = k_on_il * delta_t / (s_type * v_c * (n_neb + alpha) * n_a);
	
	
	boolean mark = false;
	
	public void step(SimState state) {
		IntegrinClustering stat = (IntegrinClustering) state;
		
		Int2D location = stat.integrins.getObjectLocation(this);
		
		if (turn == 0 && numLigandBinds == 0) {
			int tempRandom;
			while (true) {
				tempRandom = stat.random.nextInt(9);
				if (tempRandom != 4)
					break;
			}
			int newX = location.x + tempRandom / 3 - 1;
			int newY = location.y + tempRandom % 3 - 1;
			if (!(newX < 0 || newX >= stat.gridWidth || newY < 0 || newY >= stat.gridHeight)) {
				double p_m = p_d * Math.max(1 - stat.cellsMembraneVolume[newX][newY], 0);
				double randMove = stat.random.nextDouble();
				if (randMove < p_m) {
					stat.integrins.setObjectLocation(this, new Int2D(newX, newY));
					stat.cellsMembraneVolume[location.x][location.y] -= stat.integrinVolume;
					stat.cellsMembraneVolume[newX][newY] += stat.integrinVolume;
				}
			}
		} else if (turn == 1) {
			if (numLigandBinds == 0) {
				Bag tempBag = stat.ligands.getObjectsAtLocation(location.x, location.y);
				if (tempBag != null) {
					Ligand tempLigand = (Ligand)tempBag.objs[0];
					if (tempLigand.numBinds == 0) {
						double randInteract = stat.random.nextDouble();
						if (randInteract < p_on_il) {
							numLigandBinds++;
							tempLigand.numBinds++;
						}
					}
				}
			} else {
				Ligand tempLigand = (Ligand)stat.ligands.getObjectsAtLocation(location.x, location.y).objs[0];
				double randNoInteract = stat.random.nextDouble();
				if (randNoInteract < p_off_il) {
					numLigandBinds--;
					tempLigand.numBinds--;
				}
			}
		}
		turn = (turn + 1) % 3;
	}
}
