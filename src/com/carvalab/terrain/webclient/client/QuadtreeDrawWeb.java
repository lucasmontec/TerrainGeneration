package com.carvalab.terrain.webclient.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class QuadtreeDrawWeb {

	public static CssColor	gridColor	= CssColor.make(0, 255, 0);
	public static CssColor	fillColor	= CssColor.make(60, 190, 60);

	public static void drawTree(QuadtreeTerrain tree, Context2d g, boolean fill, boolean drawGrid) {
		if (tree != null) {
			if (tree.isLeaf()) {
				// Draw me
				drawNode(tree, g, fill, drawGrid);
			} else {
				// Draw my children
				for (QuadtreeTerrain child : tree.getChildren())
					drawTree(child, g, fill, drawGrid);
			}
		}
	}

	private static void drawNode(QuadtreeTerrain tree, Context2d g, boolean fill, boolean drawBounds) {
		// Draw the filled nodes
		if (fill) {
			if (tree.isFilled()) {
				g.setFillStyle(fillColor);

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
			g.setStrokeStyle(gridColor);
			g.setFillStyle(gridColor);
			g.strokeRect((int) tree.getX(), (int) tree.getY(), (int) tree.getWidth(), (int) tree.getHeight());
			// g.rect((int) tree.getX(), (int) tree.getY(), (int) tree.getWidth(), (int) tree.getHeight());
		}
	}

}
