package com.carvalab.terrain.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.carvalab.terrain.algorithms.FractalTerrain;

public class FractalTestFrame extends JFrame {
	private static final long	serialVersionUID	= -8966975108219655419L;

	private FractalTerrain		algorithm;
	private DoubleBufferedCanvas	canvas;
	private JTextField			rough;
	private JTextField			iterations;
	private JTextField			displace;
	private JPanel				buttonPanel;

	public static void main(String[] args) {
		FractalTestFrame frame = new FractalTestFrame();
		frame.prepareFrame();
		frame.repaint();
	}

	public void prepareFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(750, 500);
		setLocation(
				(int) (-getWidth() / 2 + Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2),
				(int) (-getHeight() / 2 + Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2));
		setLayout(new BorderLayout());

		// Prepare a button panel
		buttonPanel = new JPanel();

		// Add components
		// The terrain gen panel
		algorithm = new FractalTerrain(700);
		add(canvas = new DoubleBufferedCanvas(700, 400) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void draw(Graphics g) {
				algorithm.drawDebug(g, this);
			}
		}, BorderLayout.NORTH);

		// Add controls for the gen
		buttonPanel.add(new JLabel("Roughness(0-1 | double): "), BorderLayout.NORTH);

		rough = new JTextField();
		rough.setText("0.4");
		rough.setPreferredSize(new Dimension(100, 25));
		buttonPanel.add(rough, BorderLayout.NORTH);

		buttonPanel.add(new JLabel("Iterations(1-20 | int): "), BorderLayout.NORTH);

		iterations = new JTextField();
		iterations.setPreferredSize(new Dimension(100, 25));
		iterations.setText("8");
		buttonPanel.add(iterations, BorderLayout.SOUTH);

		buttonPanel.add(new JLabel("Displace(1-400 | int): "), BorderLayout.NORTH);

		displace = new JTextField();
		displace.setPreferredSize(new Dimension(100, 25));
		displace.setText("100");
		buttonPanel.add(displace, BorderLayout.SOUTH);

		// Add the button panel
		add(buttonPanel, BorderLayout.CENTER);

		// A button for generate terrain
		JButton generate = new JButton("Generate");
		generate.addActionListener(event -> {
			try {
				if (iterations.getText() != null && iterations.getText().length() > 0) {
					algorithm.setIterations(Integer.parseInt(iterations.getText()));
				}

				if (rough.getText() != null && rough.getText().length() > 0) {
					algorithm.setLandRouhness(Double.parseDouble(rough.getText()));
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(
						null,
						"Numbers are numbers, not text.",
						"ERROR: User is not smart",
						JOptionPane.ERROR_MESSAGE);
			}

			try {
				if (displace.getText() != null && displace.getText().length() > 0) {
					try {
						algorithm.generate(Double.parseDouble(displace.getText()));
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(
								null,
								"Numbers are numbers, not text.",
								"ERROR: User is not smart",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					algorithm.generate();
				}
				canvas.drawFrame();
			} catch (OutOfMemoryError o) {
				JOptionPane.showMessageDialog(
						null,
						"OUT OF MEMORY!",
						"ERROR: UUAT",
						JOptionPane.ERROR_MESSAGE);
				algorithm.clear();
			}
		});
		add(generate, BorderLayout.SOUTH);

		setVisible(true);
		canvas.drawFrame();
	}

}
