package com.carvalab.terrain.webclient.client;

public class QuadtreeIntersection {

	public static boolean rectangleToRectangle(float x, float y, float width, float height, float rx,
			float ry, float rwidth, float rheight) {
		return !(rx > x + width || rx + rwidth < x || ry > y + height || ry + rheight < y);
	}

	public static boolean circunferenceToRectangle(float cx, float cy, float radius, float x, float y,
			float width, float height) {
		// Border check
		// Y collision
		if (cx > x && cx < x + width) {
			boolean YintersectionTop = (cy + radius > y) && (cy < y + height);
			boolean YintersectionBottom = (cy - radius < y + height) && (cy > y);
			return YintersectionTop || YintersectionBottom;
		} else {
			// Corner check
			if (cx < x) { // Corners to the left
				if (cy < y)// Top left corner
					return Math.sqrt((cx - x) * (cx - x) + (cy - y) * (cy - y)) < radius;
				else if (cy > y + height)// Bottom
					// left
					// corner
					return Math.sqrt((cx - x) * (cx - x) + (cy - (y + height)) * (cy - (y + height))) < radius;
			} else {// Corners to the right
				if (cy < y)// Top right corner
					return Math.sqrt((cx - (x + width)) * (cx - (x + width)) + (cy - y) * (cy - y)) < radius;
				else if (cy > y + height)// Bottom
					// right
					// corner
					return Math.sqrt((cx - (x + width)) * (cx - (x + width)) + (cy - (y + height))
							* (cy - (y + height))) < radius;
			}
		}
		// X collision
		if (cy > y && cy < y + height) {
			boolean XintersectionLeft = (cx + radius > x) && (cx < x + width);
			boolean XintersectionRight = (cx - radius < x + width) && (cx > x);
			return XintersectionLeft || XintersectionRight;
		}

		return false;
	}

}
