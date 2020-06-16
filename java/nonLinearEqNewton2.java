//15816053
//田澤航樹
package p12;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Scanner;


public class nonLinearEqNewton2{

	public static void graphdrow(double[][] plot, double[][]solve) throws Exception{
		JFrame frame = new JFrame("Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 1000);
		Container contentPane = frame.getContentPane();
		
		GPanel graph= new GPanel();
		
		double ymin=0,ymax=0;
		for(int i=0;i<plot[0].length;i++) {
			if(plot[1][i] < ymin)
				ymin = plot[1][i];
			if(plot[1][i] > ymax)
				ymax = plot[1][i];
		}
		graph.Read(plot, solve, plot[0][plot[0].length-1], plot[0][0], Math.abs(plot[0][0]-plot[0][1]),ymin,ymax);
		contentPane.add(graph, BorderLayout.CENTER);
		
		frame.setVisible(true);
	}
	
	
	static double PI = Math.PI;
	public static double f(double x) {
		return Math.exp(x) - Math.sin(PI * x / 3.0);
	}
	public static double df(double x) {
		return Math.exp(x) - PI/3.0 * Math.cos(PI * x / 3.0);
	}
	
	public static double newton(double x) throws ArithmeticException{
		int n = 0, nmax = 30;
		double d = 0,EPS = 1.0E-15;
		
		System.out.printf("初期値 : x0 = %.2f\n", x);
		
		do {
			d = -f(x)/df(x);
			x = x + d;
			n++;
		}while(Math.abs(d) > EPS && n < nmax);
		
		
		if(n > nmax || Double.isNaN(x))
			throw new ArithmeticException("答えが見つかりませんでした。\n");
		else {
			System.out.printf("　　解　 : x  = %f\n", x);
			return x;
		}
	}
	
	public static double[][] InitialSearch(double x1, double x2, int d) throws ArithmeticException{
		int i = 0, n = 0;
		double x = 0, xtemp = x1;
		double solve[][] = new double[2][10];
		for(i=0;i<=d;i++) {
			x = x1 + i*(x2-x1)/d;
			if( f(x)*f(xtemp) < 0 ) {
				solve[0][n] = newton(xtemp);
				solve[1][n] = 0;
				n++;
			}
			xtemp = x;
		}
		if(n == 0)
			throw new ArithmeticException("解が見つかりませんでした\n");
		
		return solve;
	}
	
	public static double[][] Plot(double start, double end, int d) {
		double plot[][] = new double[2][d+1];
		for(int i=0; i<=d; i++) {
			plot[0][i] = start + i*(end-start)/d;
			plot[1][i] = f(plot[0][i]);
		}
		return plot;
	}
	
	public static void matrixShow( double[][] a, String s ) {
		System.out.println(s);
		for(int i=0; i<a.length; i++) {
			for(int j=0; j<a[0].length; j++) {
				System.out.printf("%7.3f  ",a[i][j]);
			}
			System.out.printf("\r\n");
		}
	}
	
	public static void main(String[] args) {
		double x = 0, start = Double.valueOf(args[0]), end = Double.valueOf(args[1]);
		int interval = (int)((end-start)/Double.valueOf(args[2]));
		double plot[][] = new double[2][interval+1];//プロットする点
		double solve[][] = new double[2][10];//解
		
		System.out.printf("【探索範囲%.2f ~ %.2f】 (args[0] ~ args[1])\n",start,end);
		System.out.printf("【分割数 = %d】  (args[2] = %.2f)\n\n",interval,Double.valueOf(args[2]));
		
		System.out.println("【初期値探索→ニュートン法】");
		try {
			solve = InitialSearch(start, end, interval);//初期値探索→ニュートン法
		}catch(ArithmeticException e) {
			System.out.println(e);
		}
		plot = Plot(start, end, interval);
		
		/*solve短くしている*/
		int solve_col = 0;
		for(int i=0;i<solve[0].length;i++) {
			if(solve[0][i] == 0)
				break;
			solve_col++;
		}
		double n_solve[][] = new double[2][solve_col];
		for(int i=0; i<solve_col; i++) {
			n_solve[0][i] = solve[0][i];
			n_solve[1][i] = solve[1][i];
		}
		
		/*お試し出力*/
		System.out.printf("\n【座標データ】\n");
		matrixShow(plot,"plot[2]["+plot[0].length+"] = ");
		matrixShow(n_solve,"solve[2]["+solve[0].length+"] = ");
		
		/*グラフを書く処理*/
		try {
			graphdrow(plot, n_solve);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}




class GPanel extends JPanel{

	private Point2D.Float[] points;
	static double xMax,xMin,interval,yMax,yMin;
	double Solve[][];
	
	public void Read(double[][] plot, double[][] solve, double x1, double x2, double d, double y1, double y2){
		points = new Point2D.Float[plot[0].length];
		for(int i = 0; i<plot[0].length; i++) 
			points[i] = new Point2D.Float((float)plot[0][i],(float)plot[1][i]);
		Solve = new double[2][solve[0].length];
		for(int i=0; i<Solve[0].length; i++) {
			Solve[0][i] = solve[0][i];
			Solve[1][i] = solve[1][i];
		}
		xMax = x1; xMin = x2; interval = d; yMax = y2; yMin = y1;
	}
	

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.black);
		g2d.setStroke(new BasicStroke());
		g2d.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));
		

		Float xLower = (float)xMin;
		Float xUpper = (float)xMax;
		Float xInterval = (float)interval;
		Float yLower = (float)yMin;
		Float yUpper = (float)yMax;
		Float yInterval = (float)interval;
		Float dx = xUpper - xLower;
		Float dy = yUpper - yLower;

		drawCenteredString(g2d, "Graph Tazawa Kouki", 250, 25, (float) 0.);
		drawCenteredString(g2d, "Graph", 250, 475, (float) 0.);
		drawCenteredString(g2d, "Graph", 25, 250, (float) -Math.PI / 2);
		drawCenteredString(g2d, xLower.toString(), 50, 475, (float) 0);
		drawCenteredString(g2d, xUpper.toString(), 450, 475, (float) 0);
		drawCenteredString(g2d, yLower.toString(), 25, 450, (float) 0);
		drawCenteredString(g2d, yUpper.toString(), 25, 50, (float) 0);

		g2d.setPaint(Color.gray);
		for (Float x = 50f; x <= 450; x += 400 * xInterval / dx)
			g2d.draw(new Line2D.Float(x, 450, x, 50));
		for (Float y = 50f; y <= 450; y += 400 * yInterval / dy)
			g2d.draw(new Line2D.Float(45, y, 450, y));

		/*点をプロットする*/
		g2d.setPaint(Color.red);
		Float diam = 8f;
		int num_points = points.length;

		for (int i = 0; i < num_points; i++) {
			if(points[i].y < 0)
				g2d.setPaint(Color.blue);
			if(points[i].y > 0)
				g2d.setPaint(Color.red);
			Float ex = 400 * (points[i].x - xLower) / dx + 50;
			ex -= diam / 2;
			Float ey = -400 * (points[i].y - yLower) / dy + 450;
			ey -= diam / 2;
			g2d.fill(new Ellipse2D.Float(ex, ey, diam, diam));
		}
		/*ニュートン法で求まった解をプロット*/
		g2d.setPaint(Color.green);
		for(int i=0; i<Solve[0].length; i++) {
			Float ex = 400 * ((float)Solve[0][i] - xLower) / dx + 50;
			ex -= diam / 2;
			Float ey = -400 * ((float)Solve[1][i] - yLower) / dy + 450;
			ey -= diam / 2;
			g2d.fill(new Ellipse2D.Float(ex, ey, diam, diam));
		}
		/*y=0の線を引く*/
		Float exA = 400 * ((float)xMin - xLower) / dx + 50;
		Float exB = 400 * ((float)xMax - xLower) / dx + 50;
		Float ey = -400 * (0 - yLower) / dy + 450;
		g2d.draw(new Line2D.Float(exA,ey,exB,ey));

		/*点の間に線を引く*/
		for (int i = 0; i < num_points - 1; i++) {
			if((points[i].y*points[i+1].y < 0))
				g2d.setPaint(Color.red);
			else
				g2d.setPaint(Color.black);
			Float ex1 = 400 * (points[i].x - xLower) / dx + 50;
			Float ey1 = -400 * (points[i].y - yLower) / dy + 450;
			Float ex2 = 400 * (points[i+1].x - xLower) / dx + 50;
			Float ey2 = -400 * (points[i+1].y - yLower) / dy + 450;
			g2d.draw(new Line2D.Float(ex1, ey1, ex2, ey2));
		}

	}

	private void drawCenteredString(Graphics2D g2d, String string,int x0, int y0, float angle) {
		FontRenderContext frc = g2d.getFontRenderContext();
		Rectangle2D bounds = g2d.getFont().getStringBounds(string, frc);
		LineMetrics metrics = g2d.getFont().getLineMetrics(string, frc);
		if (angle == 0) {
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