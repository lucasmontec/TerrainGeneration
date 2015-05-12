package com.carvalab.terrain.test;

import java.util.ArrayList;

import com.carvalab.terrain.algorithms.QuadtreeTerrain;


public class QuadtreeBounds {

	private static ArrayList<Rectangle>	solidBounds	= new ArrayList<>();
	private static ArrayList<Rectangle>	minGridBounds	= new ArrayList<>();
	private static int					minGridWidth	= 0, minGridHeight = 0;
	private static int					minGridWSize	= 0, minGridHSize = 0;
	private static boolean[][]			minGrid;
	private static boolean[][]			finalMinGrid;

	public static ArrayList<Rectangle> generate(QuadtreeTerrain tree, int resLevel) {
		solidBounds.clear();
		minGridWidth = (int) (tree.getWidth() / 10);
		minGridHeight = (int) (tree.getHeight() / 10);
		minGridWSize = (int) (tree.getWidth() / minGridWidth);
		minGridHSize = (int) (tree.getHeight() / minGridHeight);
		minGrid = new boolean[minGridWSize][minGridHSize];
		finalMinGrid = new boolean[minGridWSize][minGridHSize];

		// Goes down the tree and get all solid bounds
		analizeTree(tree, resLevel);

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
		System.out.println("mingrid: ");
		printMatrix(minGrid);

		//Simplify the mingrid
		for (int x = 1; x < minGridWSize - 1; x++) {
			for (int y = 1; y < minGridHSize - 1; y++) {
				if (minGrid[x + 1][y] && minGrid[x - 1][y] && minGrid[x][y + 1] && minGrid[x][y - 1]
						&& minGrid[x][y])
					finalMinGrid[x][y] = false;
			}
		}
		System.out.println("simplified: ");
		printMatrix(finalMinGrid);

		//Build minGridBounds
		for (int x = 0; x < minGridWSize; x++) {
			for (int y = 0; y < minGridHSize; y++) {
				if (finalMinGrid[x][y])
					minGridBounds.add(new Rectangle(new Point(x * minGridWidth, y * minGridHeight),
							minGridWidth, minGridHeight));
			}
		}

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

	public static void printMatrix(boolean[][] m) {
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
