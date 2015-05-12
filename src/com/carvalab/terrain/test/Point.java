package com.carvalab.terrain.test;

public class Point extends Shape {

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	public float	x;
	public float	y;

	public Point(float i, float j) {
		super();
		x = i;
		y = j;
	}

	public Point() {
		super();
		x = y = 0;
	}

	public Point(Point p) {
		super();
		x = p.x;
		y = p.y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Point set(java.awt.Point p) {
		x = p.x;
		y = p.y;
		return this;
	}

	public Point set(Point p) {
		x = p.x;
		y = p.y;
		return this;
	}

	/**
	 * Wont modify the object
	 */
	public Point addR(float x, float y) {
		return copy().add(x, y);
	}

	/**
	 * Will add x and y to this object and return it self for chaining calls
	 */
	public Point add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Point add(Point p) {
		return add(p.x, p.y);
	}

	public Point copy() {
		return new Point(this);
	}

	/**
	 * Wont modify the object
	 */
	public Point subR(float x, float y) {
		return copy().sub(x, y);
	}

	/**
	 * Will add x and y to this object and return it self for chaining calls
	 */
	public Point sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Point sub(Point p) {
		return sub(p.x, p.y);
	}

	public Point mul(float a) {
		x *= a;
		y *= a;
		return this;
	}

	public Point mulX(float a) {
		x *= a;
		return this;
	}

	public Point mulY(float a) {
		y *= a;
		return this;
	}

	public float distance2(float x, float y) {
		return (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y);
	}

	public float distance(float x, float y) {
		return (float) Math.sqrt(distance2(x, y));
	}

	public float distance(Point p) {
		return (float) Math.sqrt(distance2(p.x, p.y));
	}

	@Override
	public boolean contains(Point p) {
		return p.x == x && p.y == y;
	}

	@Override
	public Point center() {
		return this;
	}

	@Override
	public float getWidth() {
		return 1;
	}

	@Override
	public float getHeight() {
		return 1;
	}

	@Override
	public void setCenter(Point p) {
		set(p);
	}

	/**
	 * Normalize the point coordinates as they were vector coordinates
	 */
	public Point asVectorNormalize() {
		float length = getAsVectorLength();
		x = x / length;
		y = y / length;
		return this;
	}

	public float getAsVectorLength() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public Point set(float x2, float y2) {
		x = x2;
		y = y2;
		return this;
	}

	public Point mulR(float dt) {
		return copy().mul(dt);
	}

	public Point subR(Point p) {
		return copy().sub(p);
	}

	public float distance2(Point p) {
		return distance2(p.x, p.y);
	}
}