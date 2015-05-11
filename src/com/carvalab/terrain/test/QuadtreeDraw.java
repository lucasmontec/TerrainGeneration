package com.carvalab.terrain.test;

import java.awt.Color;
import java.awt.Graphics2D;

import com.carvalab.terrain.algorithms.QuadtreeTerrain;

public class QuadtreeDraw {

	public static Color	gridColor	= new Color(0, 255, 0);
	public static Color	fillColor	= new Color(60, 190, 60);

	public static void drawTree(QuadtreeTerrain tree, Graphics2D g, boolean fill,
			boolean brightnessBasedOnNodeLevel, boolean drawGrid) {
		if (tree != null) {
			if (tree.isLeaf()) {
				// Draw me
				drawNode(tree, g, fill, brightnessBasedOnNodeLevel, drawGrid);
			} else {
				// Draw my children
				for (QuadtreeTerrain child : tree.getChildren())
					drawTree(child, g, fill, brightnessBasedOnNodeLevel, drawGrid);
			}
		}
	}

	private static void drawNode(QuadtreeTerrain tree, Graphics2D g, boolean fill,
			boolean brightnessBasedOnNodeLevel, boolean drawBounds) {
		// Draw the filled nodes
		if (fill) {
			if (tree.isFilled()) {
				if (brightnessBasedOnNodeLevel) {
					float bright = 1f - (tree.getLevel() * 1f / tree.getMaxLevel() * 1f);
					g.setColor(brightness(fillColor, bright));
				} else
					g.setColor(fillColor);

				if (drawBounds)
					g.fillRect((int) tree.getX(), (int) tree.getY(), (int) tree.getWidth(), (int) tree.getHeight());
				else
					g.fillRect(
							(int) tree.getX() - 1,
							(int) tree.getY() - 1,
							(int) tree.getWidth() + 2,
							(int) tree.getHeight() + 2);
			}
		}

		// Draw the bounds of this node
		if (drawBounds) {
			g.setColor(gridColor);
			g.drawRect((int) tree.getX(), (int) tree.getY(), (int) tree.getWidth(), (int) tree.getHeight());
		}
	}

	/**
	 * Clamps v between 0..1
	 * 
	 * @param c
	 *            The color to change brighness
	 * @param v
	 *            The new brightness multiplyer
	 * @return The new color
	 */
	private static Color brightness(Color c, float v) {
		v = Math.max(0, Math.min(1, v));;
		return new Color((int) (c.getRed() * v), (int) (c.getGreen() * v), (int) (c.getBlue() * v));
	}
}
