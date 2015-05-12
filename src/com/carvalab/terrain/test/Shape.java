package com.carvalab.terrain.test;


public abstract class Shape {

	public abstract boolean contains(Point p);

	public abstract Point center();

	public abstract float getWidth();

	public abstract float getHeight();

	public abstract void setCenter(Point p);

}