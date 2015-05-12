package com.carvalab.terrain.test;

import java.util.ArrayList;

import com.carvalab.terrain.algorithms.QuadtreeTerrain;


public class QuadtreeBounds {

	private static ArrayList<Rectangle>	solidBounds	= new ArrayList<>();
	private static ArrayList<Rectangle>	minGridBounds	= new ArrayList<>();
	private static float				minGridWidth	= 0, minGridHeight = 0;
	private static int					minGridWSize	= 0, minGridHSize = 0;
	private static boolean[][]			minGrid;
	private static boolean[][]			finalMinGrid;

	public static ArrayList<Rectangle> generate(QuadtreeTerrain tree, int resLevel, int minGridLevel) {
		solidBounds.clear();
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
		analizeTree(tree, resLevel);
		System.out.println("Tree analisys: " + (System.nanoTime() - time) / 1000000 + "ms");
		time = System.nanoTime();

		// Run through the mingrid
		for (int i = 0; i < minGridWSize; i++) {
			for (int j = 0; j < minGridHSize; j++) {
				Rectangle min = new Rectangle(new Point(i * minGridWidth, j * minGridHeight), minGridWidth,
						minGridHeight);

				// Check if is contained
				for (Rectangle r : solidBounds)
					if (!(r.getX() > min.getX() + min.width || r.getX() + r.width < min.getX()
							|| r.getY() > min.getY() + min.height || r.getY() + r.height < min.getY())) {
						minGrid[i][j] = true;
						finalMinGrid[i][j] = true;
					}
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

		System.out.println("mingrid bounds: " + (System.nanoTime() - time) / 1000000 + "ms");

		return minGridBounds;
	}

	/**
	 * Goes down the tree and get all solid bounds
	 * 
	 * @param tree
	 */
	private static void analizeTree(QuadtreeTerrain tree, int resLevel) {
		if (tree != null) {
			if (tree.isLeaf() || tree.getLevel() == resLevel) {
				// Analise each son
				analizeNode(tree);
			} else {
				// Go down the tree
				for (QuadtreeTerrain child : tree.getChildren())
					analizeTree(child, resLevel);
			}
		}
	}

	/**
	 * Only leaf are analized.
	 * Store all solid rectangles.
	 * 
	 * @param node
	 */
	private static void analizeNode(QuadtreeTerrain node) {
		if (node.isFilled())
			solidBounds.add(new Rectangle(new Point((int) node.getX(), (int) node.getY()), (int) node
					.getWidth(), (int) node.getHeight()));
	}

	private static void printMatrix(boolean[][] m) {
		try {
			int rows = m.length;
			int columns = m[0].length;
			String str = " ";

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					str += m[i][j] + " ";
				}

				System.out.println(str + "");
				str = " ";
			}

		} catch (Exception e) {
			System.out.println("Matrix is empty!!");
		}
	}
}
