package ABM_Integrin_Clustering;

import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.display3d.Display3D;
import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.portrayal3d.grid.SparseGridPortrayal3D;
import sim.portrayal3d.simple.SpherePortrayal3D;
import sim.util.Bag;

public class IntegrinClusteringWithUI extends GUIState {
	private static final long serialVersionUID = 1;

	public Display2D display;
	public JFrame displayFrame;

	SparseGridPortrayal2D integrinsPortrayal = new SparseGridPortrayal2D();
	SparseGridPortrayal2D ligandsPortrayal = new SparseGridPortrayal2D();
	

	public static void main(String[] args) {
		new IntegrinClusteringWithUI().createController();
	}

	public IntegrinClusteringWithUI() {
		super(new IntegrinClustering(System.currentTimeMillis()));
	}

	public IntegrinClusteringWithUI(SimState state) {
		super(state);
	}

	public static String getName() {
		return "Integrin Clustering";
	}

	// We comment this out of the example, which will cause MASON to look
	// for a file called "index.html" in the same directory -- which we've
	// included for consistency with the other applications in the demo
	// apps directory.

	
	public static Object getInfoByClass(Class theClass) { return
	"<H2>Integrin Clustering</H2><p>Integrin Clustering Simulation!"; }
	

	public void quit() {
		super.quit();

		if (displayFrame != null)
			displayFrame.dispose();
		displayFrame = null; // let gc
		display = null; // let gc
	}

	public void start() {
		super.start();
		// set up our portrayals
		setupPortrayals();
	}

	public void load(SimState state) {
		super.load(state);
		// we now have new grids. Set up the portrayals to reflect that
		setupPortrayals();
	}

	// This is called by start() and by load() because they both had this code
	// so I didn't have to type it twice :-)
	public void setupPortrayals() {
		// tell the portrayals what to
		// portray and how to portray them
		IntegrinClustering stat = (IntegrinClustering) state;
		integrinsPortrayal.setField(stat.integrins);
		ligandsPortrayal.setField(stat.ligands);
		
		// particlesPortrayal.setField(((Tutorial3)state).particles);
		//Bag objs = stat.integrins.getAllObjects();
		// for (int i = 0; i < objs.size(); i++)
		ligandsPortrayal.setPortrayalForClass(Ligand.class,
				new RectanglePortrayal2D(Color.green, 0.9));
		integrinsPortrayal.setPortrayalForClass(Integrin.class,
				new RectanglePortrayal2D(Color.red, 0.7));
		

		// reschedule the displayer
		display.reset();

		// redraw the display
		display.repaint();
	}

	public void init(Controller c) {
		super.init(c);
		
		// Make the Display2D. We'll have it display stuff later.
		display = new Display2D(600, 600, this); // at 400x400, we've got 4x4
													// per array position
		displayFrame = display.createFrame();
		c.registerFrame(displayFrame); // register the frame so it appears in
										// the "Display" list
		displayFrame.setVisible(true);

		// specify the backdrop color -- what gets painted behind the displays
		display.setBackdrop(Color.black);

		// attach the portrayals
		display.attach(ligandsPortrayal, "Ligands");
		display.attach(integrinsPortrayal, "Integrins");
	}
}