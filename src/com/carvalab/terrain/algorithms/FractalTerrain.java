package com.carvalab.terrain.algorithms;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class FractalTerrain {
	private int						iterations			= 8;
	private final double			displaceSize		= 10;
	private double					landRoughness		= 0.5;
	private ArrayList<Double>		verticesY;
	private final ArrayList<Double>	verticesX;
	private final Random			random;
	private final int						width;

	/*
	 * Algorithm: Start with a single horizontal line segment. Repeat for a sufficiently large number of times { Repeat over each line segment in the
	 * scene { Find the midpoint of the line segment. Displace the midpoint in Y by a random amount. Reduce the range for random numbers. } }
	 */

	public FractalTerrain(float width) {
		random = new Random();

		this.width = (int) width;

		verticesY = new ArrayList<Double>();
		verticesX = new ArrayList<Double>();
	}

	public void clear() {
		verticesY.clear();
		verticesX.clear();
	}

	public void generate(double startHeight, double endHeight, double displace) {
		// Empty the list
		verticesY.clear();

		// Add the starting height and the ending
		verticesY.add(startHeight);
		verticesY.add(endHeight);

		// Create a displace var
		double disp = displace;

		// Apply displacment
		for (int i = 0; i < iterations; i++)
			verticesY = applyMidpointDisplacement(verticesY, disp *= landRoughness);

		// Clear old x positions
		verticesX.clear();

		// Generate X positions
		for (int i = 0; i < verticesY.size(); i++) {
			double dx = width * 1.0 / verticesY.size() * 1.0;
			verticesX.add(dx * i);
		}

	}

	public void generate(double displace) {
		generate(0.0, 0.0, displace);
	}

	public void generate() {
		generate(displaceSize);
	}

	private ArrayList<Double> applyMidpointDisplacement(ArrayList<Double> list, double displacement) {
		// Check if the list has at least 2 elements
		if (list != null && list.size() < 2)
			return null;

		// Create a list to return
		ArrayList<Double> ret = new ArrayList<Double>();

		// Iterate over the input list
		for (int i = 0; i < list.size() - 1; i++) {
			double middleY = (list.get(i + 1) - list.get(i)) / 2.0;
			double displace = random.nextDouble() * displacement;
			// Add the segment point
			ret.add(list.get(i));
			// Add the segment new middle point
			ret.add(list.get(i) + middleY + displace);
		}

		// Add the end segment end point
		ret.add(list.get(list.size() - 1));

		// Return the new cut list
		return ret;
	}

	public void drawDebug(Graphics g, Canvas c) {
		// Draw a BG
		g.setColor(Color.black);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());

		// Draw the line
		g.setColor(Color.green);
		if (verticesY.size() > 0) {
			for (int i = 0; i < verticesY.size() - 1; i++) {
				g.drawLine(
						(int) Math.round(verticesX.get(i)),
						c.getHeight() - (int) Math.round(verticesY.get(i)),
						(int) Math.round(verticesX.get(i + 1)),
						c.getHeight() - (int) Math.round(verticesY.get(i + 1)));
				// g.fillRect((int) Math.round(verticesX.get(i)), (int) Math.round(verticesY.get(i)), 3, 3);
			}
		}
	}

	// Getters and setters
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getIterations() {
		return iterations;
	}

	public void setLandRouhness(double r) {
		landRoughness = r;
	}

	public double getLandRoughness() {
		return landRoughness;
	}
}
