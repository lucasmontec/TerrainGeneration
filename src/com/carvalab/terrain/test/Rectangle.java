package com.carvalab.terrain.test;

import java.util.Comparator;


public class Rectangle extends Shape {

	public Point	topLeft;
	public float	width, height;

	public Rectangle() {
		super();
		width = height = 10;
		topLeft = new Point();
	}

	public Rectangle(Point topLeft, float width, float height) {
		super();
		this.topLeft = new Point(topLeft);
		this.width = width;
		this.height = height;
	}

	public Rectangle(int width, int height) {
		topLeft = new Point();
		this.width = width;
		this.height = height;
	}

	public Point bottomRight() {
		return new Point(topLeft).add(width, height);
	}

	@Override
	public boolean contains(Point p) {
		return p.x > topLeft.x && p.x < topLeft.x + width && p.y > topLeft.y && p.y < topLeft.y + height;
	}

	@Override
	public Point center() {
		return new Point((topLeft.x + width / 2), (topLeft.y + height / 2));
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public void setCenter(Point p) {
		topLeft.x = p.x - width / 2;
		topLeft.y = p.y - height / 2;
	}

	public float area() {
		return width * height;
	}

	public static class RectangleAreaComparator implements Comparator<Rectangle> {

		@Override
		public int compare(Rectangle o1, Rectangle o2) {
			return (int) (o1.area() - o2.area());
		}

	}

	public int getX() {
		return (int) topLeft.x;
	}

	public int getY() {
		return (int) topLeft.y;
	}
}