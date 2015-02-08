package ABM_Integrin_Clustering;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Int2D;

public class ClusterCounter implements Steppable {
	private static final long serialVersionUID = 1;

	int turn = 0;

	Object[] allIntegrins;
	IntegrinClustering stat;
	PrintWriter writer;

	public ClusterCounter() {
		super();
		try {
			int counter = 1;
			while (true) {
				if (new File("average-cluster-size-dt" + Integrin.delta_t + "-"
						+ IntegrinClustering.numIntegrins + "-" +counter + ".txt")
						.exists()) {
					counter++;
				} else {
					writer = new PrintWriter("average-cluster-size-dt"
							+ Integrin.delta_t + "-"
							+ IntegrinClustering.numIntegrins + "-" +counter + ".txt",
							"UTF-8");
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void step(SimState state) {
		stat = (IntegrinClustering) state;

		if (turn == 2) {
			allIntegrins = stat.integrins.getAllObjects().toArray();
			for (int i = 0; i < allIntegrins.length; i++)
				((Integrin) allIntegrins[i]).mark = false;
			int clusterSizeSum = 0;
			int clusterNum = 0;
			for (int i = 0; i < allIntegrins.length; i++) {
				if (((Integrin) allIntegrins[i]).mark == true)
					continue;
				int clusterSize = dfs(((Integrin) allIntegrins[i]));
				if (clusterSize > 9) {
					clusterSizeSum += clusterSize;
					clusterNum++;
				}
			}
			writer.println((double) clusterSizeSum / clusterNum);
		}
		turn = (turn + 1) % 3;
	}

	public int dfs(Integrin inputIntegrin) {
		Int2D location = stat.integrins.getObjectLocation(inputIntegrin);
		inputIntegrin.mark = true;
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			if (i == 4)
				continue;
			int newX = location.x + i / 3 - 1;
			int newY = location.y + i % 3 - 1;
			if (newX < 0 || newX >= stat.gridWidth || newY < 0
					|| newY >= stat.gridHeight)
				continue;
			if (stat.integrins.getObjectsAtLocation(newX, newY) == null)
				continue;
			Object[] temp = stat.integrins.getObjectsAtLocation(newX, newY)
					.toArray();
			if (temp == null)
				continue;
			for (int j = 0; j < temp.length; j++) {
				if (((Integrin) temp[j]).numLigandBinds == 0) {
					((Integrin) temp[j]).mark = true;
				} else if (((Integrin) temp[j]).mark == false) {
					sum += dfs(((Integrin) temp[j]));
				}
			}
		}
		return sum + 1;
	}
}
