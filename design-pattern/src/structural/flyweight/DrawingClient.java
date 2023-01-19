package structural.flyweight;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import structural.flyweight.ShapeFactory.ShapeType;

/**
 * Before we apply flyweight design pattern, we need to consider following
 * factors:
 * <P>
 * The number of Objects to be created by application should be huge.
 * <P>
 * The object creation is heavy on memory and it can be time consuming too.
 * <P>
 * The object properties can be divided into intrinsic and extrinsic properties,
 * extrinsic properties of an Object should be defined by the client program.
 * 
 * <P>
 * Flyweight Design Pattern Example in JDK All the wrapper classes valueOf()
 * method uses cached objects showing use of Flyweight design pattern. The best
 * example is Java String class String Pool implementation.
 * 
 * @author TinhNX
 *
 */
public class DrawingClient extends JFrame {

	private static final long serialVersionUID = -1350200437285282550L;
	private final int WIDTH;
	private final int HEIGHT;

	private static final ShapeType shapes[] = { ShapeType.LINE, ShapeType.OVAL_FILL, ShapeType.OVAL_NOFILL };
	private static final Color colors[] = { Color.RED, Color.GREEN, Color.YELLOW };

	public DrawingClient(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		Container contentPane = getContentPane();

		JButton startButton = new JButton("Draw");
		final JPanel panel = new JPanel();

		contentPane.add(panel, BorderLayout.CENTER);
		contentPane.add(startButton, BorderLayout.SOUTH);
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Graphics g = panel.getGraphics();
				for (int i = 0; i < 20; ++i) {
					Shape shape = ShapeFactory.getShape(getRandomShape());
					shape.draw(g, getRandomX(), getRandomY(), getRandomWidth(), getRandomHeight(), getRandomColor());
				}
			}
		});
	}

	private ShapeType getRandomShape() {
		return shapes[(int) (Math.random() * shapes.length)];
	}

	private int getRandomX() {
		return (int) (Math.random() * WIDTH);
	}

	private int getRandomY() {
		return (int) (Math.random() * HEIGHT);
	}

	private int getRandomWidth() {
		return (int) (Math.random() * (WIDTH / 10));
	}

	private int getRandomHeight() {
		return (int) (Math.random() * (HEIGHT / 10));
	}

	private Color getRandomColor() {
		return colors[(int) (Math.random() * colors.length)];
	}

	public static void main(String[] args) {
		DrawingClient drawing = new DrawingClient(500, 600);
	}
}
