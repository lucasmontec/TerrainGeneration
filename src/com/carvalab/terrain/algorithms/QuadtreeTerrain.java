package com.carvalab.terrain.algorithms;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Is a nice quad tree ;)
 * 
 * @author Lucas M Carvalhaes
 *
 */
public class QuadtreeTerrain {
	private enum Quadrant {
		TOPLEFT,
		TOPRIGHT,
		BOTTOMLEFT,
		BOTTOMRIGHT
	}

	/**
	 * Children
	 */
	private QuadtreeTerrain	topLeft, topRight, bottomLeft, bottomRight;
	private QuadtreeTerrain	father;
	private float			width, height;
	private float			x, y;
	private boolean			filled;
	private final int				level;
	private final int		maxLevel;

	public QuadtreeTerrain() {
		maxLevel = 5;
		level = 0;
	}

	public QuadtreeTerrain(float x, float y, float width, float height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		maxLevel = 5;
		level = 0;
	}

	public QuadtreeTerrain(float x, float y, float width, float height, int maxLevel) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.maxLevel = maxLevel;
		level = 0;
	}

	/**
	 * This is for inner subdivision
	 * 
	 * @param father
	 *            The father quadtree
	 * @param quadrant
	 *            The quadrant ID 0=top left, 1=top right, 2=bottom left, 3=bottom right
	 */
	private QuadtreeTerrain(QuadtreeTerrain father, Quadrant quadrant) {
		maxLevel = father.maxLevel;
		level = father.level + 1;
		this.father = father;
		if (level > maxLevel)
			throw new RuntimeException("Max level reached!");

		// Store new size
		float halfW = father.width / 2;
		float halfH = father.height / 2;

		// Set the position
		switch (quadrant) {
			case TOPLEFT:// Topleft
				x = father.x;
				y = father.y;
				break;
			case TOPRIGHT:// Topright
				x = father.x + halfW;
				y = father.y;
				break;
			case BOTTOMLEFT:// Bottomleft
				x = father.x;
				y = father.y + halfH;
				break;
			case BOTTOMRIGHT:// Bottomright
				x = father.x + halfW;
				y = father.y + halfH;
				break;
			default:// Topleft default
				x = father.x;
				y = father.y;
				break;
		}

		// Set the size
		width = halfW;
		height = halfH;

		// Store filled
		filled = father.filled;
	}

	/**
	 * Inserts a rectangular region.
	 * 
	 * @param add
	 *            if set to true will be aditive, false subtractive
	 * @param rx
	 *            top left
	 * @param ry
	 *            top right
	 * @param rwidth
	 *            region width
	 * @param rheight
	 *            region height
	 */
	public void paintRectangle(boolean add, float rx, float ry, float rwidth, float rheight) {
		if (QuadtreeIntersection.rectangleToRectangle(x, y, width, height, rx, ry, rwidth, rheight)) {
			// This tree collides with the region
			// Lets split the tree
			if (isBottom()) {
				filled = add;
			} else {
				// subdivide
				subdivide();
				// test subtrees
				for (QuadtreeTerrain child : getChildren())
					child.paintRectangle(add, rx, ry, rwidth, rheight);
				// test merge
				if (canMerge())
					merge();
			}
		}
		// if is doesn't collide, do nothing
	}

	/**
	 * adds a circular region
	 * 
	 * @param add
	 *            if set to true will be aditive, false subtractive
	 * @param cx
	 *            center x of the circle
	 * @param cy
	 *            center y of the circle
	 * @param radius
	 *            the circle radius
	 */
	public void paintCircunference(boolean add, float cx, float cy, float radius) {
		if (QuadtreeIntersection.circunferenceToRectangle(cx, cy, radius, x, y, width, height)) {
			// This tree collides with the region
			// Lets split the tree
			if (isBottom()) {
				filled = add;
			} else {
				// subdivide
				subdivide();
				// test subtrees
				for (QuadtreeTerrain child : getChildren())
					child.paintCircunference(add, cx, cy, radius);
				// test merge
				if (canMerge())
					merge();
			}
		}
		// if is doesn't collide, do nothing
	}

	/**
	 * Creates all subtrees
	 */
	protected void subdivide() {
		if (topLeft == null)
			topLeft = new QuadtreeTerrain(this, Quadrant.TOPLEFT);
		if (topRight == null)
			topRight = new QuadtreeTerrain(this, Quadrant.TOPRIGHT);
		if (bottomLeft == null)
			bottomLeft = new QuadtreeTerrain(this, Quadrant.BOTTOMLEFT);
		if (bottomRight == null)
			bottomRight = new QuadtreeTerrain(this, Quadrant.BOTTOMRIGHT);
	}

	/**
	 * @return true if all children are the same
	 */
	protected boolean canMerge() {
		if (hasChildren())
			if (topLeft.isLeaf() && topRight.isLeaf() && bottomLeft.isLeaf() && bottomRight.isLeaf())
				if (!(topLeft.filled != topRight.filled || topLeft.filled != bottomRight.filled || topLeft.filled != bottomLeft.filled))
					return true;
		return false;
	}

	/**
	 * Deletes children
	 */
	protected void merge() {
		// Copy children fill state
		filled = topLeft.filled;

		// Delete children
		topLeft = null;
		topRight = null;
		bottomLeft = null;
		bottomRight = null;
	}

	/**
	 * A bottom node is also a leaf node
	 * 
	 * @return true if this tree canno't be subdivided (current level = maxLevel)
	 */
	public boolean isBottom() {
		return level == maxLevel;
	}

	/**
	 * @return true if this tree is a leaf node
	 */
	public boolean isLeaf() {
		return isBottom()
				|| (topLeft == null && topRight == null && bottomLeft == null && bottomRight == null);
	}

	/**
	 * @return true if the tree has all four children
	 */
	public boolean hasChildren() {
		return !(topLeft == null && topRight == null && bottomLeft == null && bottomRight == null);
	}

	/**
	 * @return true if this is a 'filled' node to draw
	 */
	public boolean isFilled() {
		return filled;
	}

	/*
	 * GETS / SETS
	 */

	public LinkedList<QuadtreeTerrain> getChildren() {
		return new LinkedList<>(Arrays.asList(topLeft, topRight, bottomLeft, bottomRight));
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	/**
	 * The level this node is currently at
	 * 
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * The max level this tree can subdvide to
	 * 
	 * @return
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	public QuadtreeTerrain getFather() {
		return father;
	}
}
