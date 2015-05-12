package com.carvalab.terrain.webclient.client;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class QuadtreeTestApp implements EntryPoint {

	Canvas					canvas;

	// mouse positions relative to canvas
	int						mouseX, mouseY;

	// canvas size, in px
	static final int		height		= 700;
	static final int		width		= 700;
	private final float	zsize	= 15;
	private static boolean	paintRect	= false;
	private static boolean	drawGrid	= false;

	private static boolean	mouseDown	= false;
	private static int		buttonDown	= -1;

	// background fill
	CssColor				black	= CssColor.make(0, 0, 0);
	CssColor				green		= CssColor.make(0, 255, 0);

	Context2d				context;

	//Quadtree
	QuadtreeTerrain		terrain;

	@Override
	public void onModuleLoad() {
		terrain = new QuadtreeTerrain(0, 0, width, height, 8);

		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			RootPanel.get().add(new Label("Your browser does not support the HTML5 Canvas."));
			return;
		}

		canvas.setWidth(width + "px");
		canvas.setHeight(height + "px");
		canvas.setStyleName("disableContext");
		canvas.setStyleName("center", true);
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		RootPanel.get().add(canvas);
		context = canvas.getContext2d();

		final Timer timer = new Timer() {
			@Override
			public void run() {
				doUpdate();
			}
		};
		timer.scheduleRepeating(25);

		prepareHandlers();
	}

	private void prepareHandlers() {
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent mvh) {
				mouseX = mvh.getRelativeX(canvas.getElement());
				mouseY = mvh.getRelativeY(canvas.getElement());

				// Dragg
				if (mouseDown) {
					if (paintRect)
						terrain.paintRectangle((buttonDown == MouseEvent.BUTTON1), mouseX - zsize / 2, mouseY
								- zsize / 2, zsize, zsize);
					else
						terrain.paintCircunference((buttonDown == MouseEvent.BUTTON1), mouseX, mouseY, zsize);
				}
			}
		});
		canvas.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent evt) {
				mouseDown = true;
				buttonDown = evt.getNativeButton();
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent evt) {
				mouseDown = false;
				mouseX = evt.getRelativeX(canvas.getElement());
				mouseY = evt.getRelativeY(canvas.getElement());
				if (paintRect)
					terrain.paintRectangle((evt.getNativeButton() == MouseEvent.BUTTON1),
							mouseX - zsize / 2,
							mouseY - zsize / 2,
							zsize,
							zsize);
				else
					terrain.paintCircunference(
							(evt.getNativeButton() == MouseEvent.BUTTON1),
							mouseX,
							mouseY,
							zsize);
			}
		});
		canvas.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent evt) {
				if (evt.getNativeKeyCode() == KeyEvent.VK_G)
					drawGrid = !drawGrid;
				if (evt.getNativeKeyCode() == KeyEvent.VK_R)
					paintRect = !paintRect;
			}
		});
	}

	void doUpdate() {
		// update the back canvas
		context.setFillStyle(black);
		context.fillRect(0, 0, width, height);

		// Draw info
		context.setFillStyle(green);
		context.fillText("Quadtree", 10, 20);
		context.fillText("(G)rid: " + drawGrid, 10, 35);
		context.fillText("(R)ectangle: " + paintRect, 10, 50);

		// Draw stuff
		QuadtreeDrawWeb.drawTree(terrain, context, true, drawGrid);
	}

}
