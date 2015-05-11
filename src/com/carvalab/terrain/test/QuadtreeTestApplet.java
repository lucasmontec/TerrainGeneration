package com.carvalab.terrain.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JApplet;

import com.carvalab.terrain.algorithms.QuadtreeTerrain;

public class QuadtreeTestApplet extends JApplet implements MouseListener, MouseMotionListener, KeyListener {
	private static final long	serialVersionUID	= 1L;

	private QuadtreeTerrain			tree;
	private DoubleBufferedCanvas	canvas;
	private int						mousex, mousey;
	private final float				zsize				= 15;
	private int						btn					= 0;
	// false will paint circle
	private boolean					paintRect			= false;
	private boolean					showGrid			= false;

	@Override
	public void init() {
		setSize(700, 700);

		// Add components
		// The terrain gen panel
		tree = new QuadtreeTerrain(0, 0, 699, 699, 9);
		add(canvas = new DoubleBufferedCanvas() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void draw(Graphics g) {
				g.setColor(Color.black);
				g.fillRect(0, 0, getWidth(), getHeight());

				// draw quadtree
				QuadtreeDraw.drawTree(tree, (Graphics2D) g, true, true, showGrid);

				// draw mouse
				g.setColor(Color.green);
				if (paintRect)
					g.drawRect(
							(int) (mousex - zsize / 2),
							(int) (mousey - zsize / 2),
							(int) zsize,
							(int) zsize);
				else
					g.drawOval(
							(int) (mousex - zsize),
							(int) (mousey - zsize),
							(int) (zsize * 2),
							(int) (zsize * 2));

				// Info
				g.drawString("Quadtree terrain", 10, 20);
				g.drawString("(G)rid: " + showGrid, 10, 35);
				g.drawString("(R)ectangle: " + paintRect, 10, 50);
			}
		});

		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		addKeyListener(this);
		canvas.addKeyListener(this);
		setFocusable(true);
		setVisible(true);
		requestFocusInWindow();
		canvas.start();
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if (paintRect)
			tree.paintRectangle((evt.getButton() == MouseEvent.BUTTON1), evt.getX() - zsize / 2, evt.getY()
					- zsize / 2, zsize, zsize);
		else
			tree.paintCircunference((evt.getButton() == MouseEvent.BUTTON1), mousex, mousey, zsize);
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if (paintRect)
			tree.paintRectangle(
					btn == MouseEvent.BUTTON1,
					evt.getX() - zsize / 2,
					evt.getY() - zsize / 2,
					zsize,
					zsize);
		else
			tree.paintCircunference(btn == MouseEvent.BUTTON1, mousex, mousey, zsize);
		mousex = evt.getX();
		mousey = evt.getY();
	}

	// Useless
	@Override
	public void mouseMoved(MouseEvent evt) {
		mousex = evt.getX();
		mousey = evt.getY();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent evt) {
		btn = evt.getButton();
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R)
			paintRect = !paintRect;
		if (e.getKeyCode() == KeyEvent.VK_G)
			showGrid = !showGrid;
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
