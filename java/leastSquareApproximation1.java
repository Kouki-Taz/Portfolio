//15816053
//田澤航樹
package p13;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class leastSquareApproximation1 {

	static int N = 3;//~次式
	static double[] a = new double[N + 1];//最小２乗近似用

	
	public static void main(String[] args) {
		JFrame frame = new JFrame("最小２乗近似");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		Container contentPane = frame.getContentPane();

		GPanel plot_graph = new GPanel();
		plot_graph.read(args[0]);

		
		contentPane.add(plot_graph, BorderLayout.CENTER);
		
		frame.setResizable(false);//画面サイズを変更できなくする。
		frame.setVisible(true);
	}

	/*最小２乗近似*/
	public static void leastsquare(Point2D.Double[] b) {
		double[][] p = new double[N + 1][(N + 1) + 1];//ベクトルを後ろにくっつけるから

		/*くっつけるベクトルの計算*/
		for(int i = 0; i <= N; i++) 
			for(int j = 0; j < b.length; j++) 
				a[i] += b[j].y * Math.pow(b[j].x, i);
		
		/*行列の計算*/
		for(int i = 0; i <= N; i++) {
			for(int j = 0; j <= i; j++) {
				p[i][j] = 0.0;
				for(int k = 0; k < b.length; k++) 
					p[i][j] += Math.pow(b[k].x, i + j);
				if(i != j) 
					p[j][i] = p[i][j];
			}
		}
		
		/*ベクトルを行列の右にくっつける*/
		for(int i = 0; i <= N; i++) 
			p[i][N + 1] = a[i];
		
		gauss(p);//ガウスの消去法
		/*ベクトルの表示(お試し出力)*/
		for(int i = 1; i <= N + 1; i++) 
			System.out.println("a" + i + " = " + a[i - 1]);
	}
	/*ガウス消去法*/
	public static void gauss(double[][] g) {
		int pivot;
		double amax = 0, epsilon = 1.0E-99, tmp;

		for(int k = 0; k <= N; k++) {
			amax = Math.abs(g[k][k]);
			pivot = k;
			for(int i = k + 1; i <= N; i++) {
				if(Math.abs(g[i][k]) > amax) {
					amax = Math.abs(g[i][k]);
					pivot = i;
				}
			}

			if(amax < epsilon) {
				System.out.println("ガウス消去法エラー");
				System.exit(1);
			}

			if(pivot != k) {
				for(int j = k; j <= N + 1; j++) {
					tmp = g[k][j];
					g[k][j] = g[pivot][j];
					g[pivot][j] = tmp;
				}
			}

			amax = g[k][k];
			for(int j = k; j <= N + 1; j++) 
				g[k][j] /= amax;
			
			for(int i = k + 1; i <= N; i++) {
				amax = g[i][k];
				for(int j = k; j <= N + 1; j++) 
					g[i][j] -= g[k][j] * amax;
			}
		}

		for(int i = g.length - 1; i >= 0; i--) {
			double sum = 0.0;
			for(int j = i + 1; j < g.length; j++) 
				sum += g[i][j] * a[j];
			a[i] = (g[i][g.length] - sum) / g[i][i];
		}
	}
}


class GPanel extends JPanel {

	private Point2D.Double[] points;
	double xmin, xmax, ymin, ymax;

	public void read(String fname) {
		try {
			Scanner input = new Scanner(new File(fname));
			int count = 0;
			while(input.hasNextLine()) {
				String dummy = input.nextLine();
				++count;
			}
			input.close();

			points = new Point2D.Double[count];

			input = new Scanner(new File(fname));
			for(int i = 0; i < count; i++) {
				Scanner s = new Scanner(input.nextLine());
				s.useDelimiter(",");
				points[i] = new Point2D.Double(s.nextFloat(), s.nextFloat());
				
				/*最大値最小値を記録*/
				if(i == 0) {
					xmax = points[i].x;
					xmin = points[i].x;
					ymax = points[i].y;
					ymin = points[i].y;
				}
				if(xmax < points[i].x)  xmax = points[i].x;
				if(xmin > points[i].x)  xmin = points[i].x;
				if(ymax < points[i].y)  ymax = points[i].y;
				if(ymin > points[i].y)  ymin = points[i].y;
			}
			input.close();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.black);
		g2d.setStroke(new BasicStroke());
		g2d.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));

		Float xLower = (float) xmin;
		Float xUpper = (float) xmax;
		Float xInterval = (float) (xmax - xmin) / 10;
		Float yLower = (float) ymin;
		Float yUpper = (float) ymax;
		Float yInterval = (float) (ymax - ymin) / 10;
		Float dx = xUpper - xLower;
		Float dy = yUpper - yLower;

		drawCenteredString(g2d, "15816053 Kouki Tazawa", 250, 25, (float) 0.);
		drawCenteredString(g2d, "x", 250, 475, (float) 0.);
		drawCenteredString(g2d, "y", 25, 250, (float) -Math.PI / 2);
		drawCenteredString(g2d, xLower.toString(), 50, 475, 0);
		drawCenteredString(g2d, xUpper.toString(), 450, 475, 0);
		drawCenteredString(g2d, yLower.toString(), 25, 450, 0);
		drawCenteredString(g2d, yUpper.toString(), 25, 50, 0);

		g2d.setPaint(Color.gray);
		for(Float x = 50f; x <= 450; x += 400 * xInterval / dx) {
			g2d.draw(new Line2D.Float(x, 450, x, 50));
		}
		for(Float y = 50f; y <= 450; y += 400 * yInterval / dy) {
			g2d.draw(new Line2D.Float(45, y, 450, y));
		}

		g2d.setPaint(Color.red);
		Float diam = 8f;
		int num_points = points.length;

		for(int i = 0; i < num_points; i++) {
			Double ex = 400 * (points[i].x - xLower) / dx + 50;
			ex -= diam / 2;
			Double ey = -400 * (points[i].y - yLower) / dy + 450;
			ey -= diam / 2;
			g2d.fill(new Ellipse2D.Double(ex, ey, diam, diam));
		}
		/*追加部分*/
		leastSquareApproximation1.leastsquare(points);//最小2乗近似
		g2d.setPaint(Color.blue);
		double x0 = 0, y0 = 0;
		for(double x = points[0].x - 0.5; x <= points[points.length - 1].x; x += 0.1) {
			double y = 0.0;
			for(int i = leastSquareApproximation1.N; i >= 0; i--)
				y += leastSquareApproximation1.a[i] * Math.pow(x, i);
			if(x != points[0].x - 0.5) {
				double ex1 = 400 * (x0 - xLower) / dx + 50;
				double ey1 = -400 * (y0 - yLower) / dy + 450;
				double ex2 = 400 * (x - xLower) / dx + 50;
				double ey2 = -400 * (y - yLower) / dy + 450;
				g2d.draw(new Line2D.Double(ex1, ey1, ex2, ey2));
			}
			x0 = x;
			y0 = y;
		}
	}

	private void drawCenteredString(Graphics2D g2d, String string, int x0, int y0, float angle) {
		FontRenderContext frc = g2d.getFontRenderContext();
		Rectangle2D bounds = g2d.getFont().getStringBounds(string, frc);
		LineMetrics metrics = g2d.getFont().getLineMetrics(string, frc);
		if(angle == 0) {
			g2d.drawString(string, x0 - (float) bounds.getWidth() / 2,
					y0 + metrics.getHeight() / 2);
		} else {
			g2d.rotate(angle, x0, y0);
			g2d.drawString(string, x0 - (float) bounds.getWidth() / 2,
					y0 + metrics.getHeight() / 2);
			g2d.rotate(-angle, x0, y0);
		}
	}
}