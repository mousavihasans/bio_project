package ABM_Integrin_Clustering;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class IntegrinClustering extends SimState {
	private static final long serialVersionUID = 1;

	public SparseGrid2D ligands;
	public SparseGrid2D integrins;

	public int gridWidth = 100;
	public int gridHeight = 100;
	public static int numIntegrins = 3500;
	public int numLigands = 0;
	public double cellsMembraneVolume[][] = new double[gridWidth][gridHeight];
	public double cellsEcmVolume[][] = new double[gridWidth][gridHeight];
	public double integrinVolume = 0.1234;
	public double ligandVolume = 0.2; // Same ligand volume for all

	ClusterCounter cc;

	public IntegrinClustering(long seed) {
		super(seed);
	}

	public void start() {
		super.start();
		ligands = new SparseGrid2D(gridWidth, gridHeight);
		integrins = new SparseGrid2D(gridWidth, gridHeight);

		Integrin tempIntegrin;

		for (int i = 0; i < numIntegrins; i++) {
			tempIntegrin = new Integrin();

			schedule.scheduleRepeating(tempIntegrin);
			while (true) {
				Int2D tempLocation = new Int2D(random.nextInt(gridWidth),
						random.nextInt(gridHeight));
				if (cellsMembraneVolume[tempLocation.x][tempLocation.y] < 1) {
					cellsMembraneVolume[tempLocation.x][tempLocation.y] += integrinVolume;
					integrins.setObjectLocation(tempIntegrin, tempLocation);
					break;
				}
			}
		}
		Ligand tempLigand;

		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridHeight; j++) {
				if (i % 2 != 0 && j % 3 != 0)
					continue;
				tempLigand = new Ligand();

				schedule.scheduleRepeating(tempLigand);
				cellsEcmVolume[i][j] += ligandVolume;
				ligands.setObjectLocation(tempLigand, i, j);
			}
		}
		cc = new ClusterCounter();
		schedule.scheduleRepeating(cc);
	}

	public void finish() {
		cc.writer.close();
	}

	public static void main(String[] args) {
		doLoop(IntegrinClustering.class, args);
		System.exit(0);
	}
}
