package com.carvalab.terrain.test;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JOptionPane;

/**
 * Simple double buffered canvas to use when drawing the tests for the algorithms.
 * 
 * @author Lucas M Carvalhaes
 *
 */
public abstract class DoubleBufferedCanvas extends Canvas implements Runnable {
	private static final long serialVersionUID = 8503349736297787253L;

	private boolean				running				= false;
	private Thread				ownerThread			= null;

	public DoubleBufferedCanvas(int width, int height) {
		setIgnoreRepaint(true);
		setPreferredSize(new Dimension(width, height));
		setSize(width, height);
	}

	public DoubleBufferedCanvas() {
		setIgnoreRepaint(true);
	}

	private void innerDraw() {
		//Grab this component buffer strategy
		BufferStrategy bufferStr = getBufferStrategy();
		if (bufferStr == null) {
			this.createBufferStrategy(2);
			bufferStr = getBufferStrategy();
		}
		//Prepare a graphics to use
		Graphics g = null;

		//Try to get the graphics
		try{

			//Get it
			g = bufferStr.getDrawGraphics();

			//Set the default color
			g.setColor(Color.black);

			// Clear the background
			g.clearRect(0, 0, getWidth(), getHeight());

			// Draw stuff on the graphics
			draw(g);

		}catch(Exception e){
			//Error if buffer strategy fails
			JOptionPane.showMessageDialog(null, "Error on creating double buffer! \n"+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			//Dispose our graphics for drawing
			g.dispose();
		}

		//Switch buffers
		if(!bufferStr.contentsLost())
			bufferStr.show();

		//Sync the drawing with the system to avoid delays
		Toolkit.getDefaultToolkit().sync();
	}

	//Override this function to draw stuff
	protected abstract void draw(Graphics g);

	/**
	 * Draws one frame
	 */
	public void drawFrame() {
		innerDraw();
	}

	/**
	 * Start continuous drawing
	 */
	public void start() {
		running = true;
		ownerThread = new Thread(this);
		ownerThread.start();
	}

	/**
	 * Stop continuous drawing
	 */
	public void stop() {
		running = false;
		try {
			ownerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (running) {
			innerDraw();
		}
	}
}
