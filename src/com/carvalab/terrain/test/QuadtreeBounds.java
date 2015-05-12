package com.carvalab.terrain.test;

import java.util.ArrayList;

import com.carvalab.terrain.algorithms.QuadtreeTerrain;


public class QuadtreeBounds {

	private static ArrayList<Rectangle>	minGridBounds	= new ArrayList<>();
	private static float				minGridWidth	= 0, minGridHeight = 0;
	private static int					minGridWSize	= 0, minGridHSize = 0;
	private static boolean[][]			minGrid;
	private static boolean[][]			finalMinGrid;

	public static ArrayList<Rectangle> generate(QuadtreeTerrain tree, int resLevel, int minGridLevel) {
		minGridBounds.clear();
		minGridWidth = tree.getWidth() / minGridLevel;
		minGridHeight = tree.getHeight() / minGridLevel;
		minGridWSize = minGridLevel;
		minGridHSize = minGridLevel;
		minGrid = new boolean[minGridWSize][minGridHSize];
		finalMinGrid = new boolean[minGridWSize][minGridHSize];

		// Goes down the tree and get all solid bounds
		System.out.println("Start");
		long time = System.nanoTime();
		long total = System.nanoTime();

		// Run through the mingrid
		for (int i = 0; i < minGridWSize; i++) {
			for (int j = 0; j < minGridHSize; j++) {

				// Check if is contained
				boolean solid = tree.hasSolidsInRange(
						i * minGridWidth,
						j * minGridHeight,
						minGridWidth,
						minGridHeight);
				minGrid[i][j] = solid;
				finalMinGrid[i][j] = solid;
			}
		}
		System.out.println("mingrid make: " + (System.nanoTime() - time) / 1000000 + "ms");
		time = System.nanoTime();

		// Simplify the mingrid - removes all non edges
		for (int x = 1; x < minGridWSize - 1; x++) {
			for (int y = 1; y < minGridHSize - 1; y++) {
				if (minGrid[x + 1][y] && minGrid[x - 1][y] && minGrid[x][y + 1] && minGrid[x][y - 1]
						&& minGrid[x][y])
					finalMinGrid[x][y] = false;
			}
		}
		System.out.println("mingrid edge: " + (System.nanoTime() - time) / 1000000 + "ms");
		time = System.nanoTime();

		//Build minGridBounds
		for (int x = 0; x < minGridWSize; x++) {
			for (int y = 0; y < minGridHSize; y++) {
				if (finalMinGrid[x][y]) {
					// Store the rectangle start
					Point start = new Point(x * minGridWidth, y * minGridHeight);

					// Find if the block spans in x or y
					int spanx = x + 1;
					int spany = y + 1;
					if (spanx < minGridWSize-1 && finalMinGrid[spanx][y]) { // X span
						while (spanx < minGridWSize - 1 && finalMinGrid[spanx][y]) {
							finalMinGrid[spanx][y] = false;
							spanx++;
						}
						minGridBounds.add(new Rectangle(start, minGridWidth * (spanx - x), minGridHeight));
					} else if (spany < minGridHSize-1 && finalMinGrid[x][spany]) { // Y span
						while (spany < minGridHSize-1 && finalMinGrid[x][spany]) {
							finalMinGrid[x][spany] = false;
							spany++;
						}
						minGridBounds.add(new Rectangle(start, minGridWidth, minGridHeight * (spany - y)));
					} else { // No span
						minGridBounds.add(new Rectangle(start, minGridWidth, minGridHeight));
					}
				}
			}
		}

		System.out.println("mingrid merge: " + (System.nanoTime() - time) / 1000000 + "ms");

		System.out.println("total mingrid: " + (System.nanoTime() - total) / 1000000 + "ms");
		System.out.println("# generated: " + minGridBounds.size());

		return minGridBounds;
	}

}
