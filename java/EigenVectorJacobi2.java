package p14;


import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class EigenVectorJacobi2{

	EigenVectorJacobi2(){ 
	}
	
	public static void matrixShow( double[][] a, String s) {
		System.out.println(s);
		for(int i=0; i<a.length; i++) {
			for(int j=0; j<a[0].length; j++) {
				System.out.printf("%7.3f  ",a[i][j]);
			}
			System.out.printf("\r\n");
		}
	}
	public static double[][] readFromFile(String filename){
		int rows = 0;
		int columns = 0;
		ArrayList<ArrayList<Double>> a = new ArrayList<ArrayList<Double>>();
		
		try {
			Scanner input = new Scanner (new File(filename));
			for(rows = 0; input.hasNextLine(); rows++) {
				Scanner colReader = new Scanner(input.nextLine());
				ArrayList<Double> col = new ArrayList<Double>();
				for(columns = 0; colReader.hasNextDouble(); columns++) {
					col.add(colReader.nextDouble());
				}
				a.add(col);
				colReader.close();
			}
			input.close();
		}catch(Exception e){
			System.out.println(e);
			System.exit(1);
		}
		
		System.out.printf("%d行%d列の行列を読み込んだ\n", rows,columns);
		
		//ArrayListから二次元配列にコピーする処理
		double[][] b = new double[rows][columns];
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				if(j == columns)
					b[i][j] = a.get(rows).get(i);
				else
					b[i][j] = a.get(i).get(j);
			}
		}
		return b;
	}
	static double[][] jacobi(double a[][], double r[][], String s1){
		int n = a.length;
		int m=0,k=0;
		double t, u;
		double eps = 1.0E-10;
		double max=0;
		double sin_t,cos_t;
		double[] a_row_m, a_row_k, a_col_m, a_col_k;
		

		a_row_m = new double[n];
		a_row_k = new double[n];
		a_col_m = new double[n];
		a_col_k = new double[n];
		
		for(int it=0; it<100; it++) {
			max = eps;
			for(int i = 0; i<n; i++) {
				for(int j=i+1; j<n; j++) {
					if(Math.abs(a[i][j]) > max) {
						max = Math.abs(a[i][j]);
						k = i;
						m = j;
					}
				}
			}
			if(max == eps) {
				//System.out.printf("jacobi: finished after %d cycles s=%f\n",it+1,a[k][m]);
				/*処理結果の出力*/
				System.out.println("\n\n【Final result】");
				System.out.printf("%s: eigenvalues\n",s1);
				for(int i=0; i<n; i++) {
					System.out.printf("%f\n", a[i][i]);
				}
				System.out.printf("%s: eigen vectors", s1);
				matrixShow(r,"");
				
				return r;
			}
			else {
				//System.out.printf("jacobi: cycle %d, found non-orthogonral max a[%d][%d]=%f\n", it+1,k,m,max);
			}
			
			
			if(Math.abs(a[k][k] - a[m][m]) < eps) {
				cos_t = 1 / Math.sqrt(2.0);
				sin_t = a[k][m] > 0.0 ? 1.0/Math.sqrt(2.0) : -1.0/Math.sqrt(2.0);
			}
			else {
				t = (2.0 * a[k][m]) / (a[k][k] - a[m][m]);
				u = Math.sqrt(1.0 + t*t);
				cos_t = Math.sqrt((1.0+u) / (2.0*u));
				sin_t = Math.sqrt((-1.0+u) / (2.0*u)) * (t>0.0 ? 1.0 : -1.0);
			}
			//System.out.printf("jacobi: cos_t = %f, sin_t = %f\n", cos_t,sin_t);
			
			/*R'*A*Rのくだり*/
			for(int j=0; j<n; j++) {
				a_row_k[j] = a[k][j] * cos_t + a[m][j] * sin_t;
				a_row_m[j] = (-1) * a[k][j] * sin_t + a[m][j] * cos_t;
			}
			for(int i=0; i<n; i++) {
				a_col_k[i] = a[i][k] * cos_t + a[i][m] * sin_t;
				a_col_m[i] = (-1) * a[i][k] * sin_t + a[i][m] * cos_t;
			}
			a_col_k[k] = a[k][k] * cos_t * cos_t
					+ a[k][m] * cos_t * sin_t
					+ a[m][k] * cos_t * sin_t
					+ a[m][m] * sin_t * sin_t;
			a_col_m[m] = a[k][k] * sin_t * sin_t
					- a[m][k] * cos_t * sin_t
					- a[k][m] * cos_t * sin_t
					+ a[m][m] * cos_t * cos_t;
			a_col_k[m] = 0.0;
			a_col_m[k] = 0.0;
			
			for(int j=0; j<n; j++) {
				a[k][j] = a_row_k[j];
				a[m][j] = a_row_m[j];
			}
			for(int i=0; i<n; i++) {
				a[i][k] = a_col_k[i];
				a[i][m] = a_col_m[i];
			}
			
			//System.out.printf("jacobi: A(%d)\n", it+1);
			//matrixShow(a,"a = ");
			
			for(int i=0; i<n; i++) {
				a_col_k[i] = r[i][k] * cos_t + r[i][m] * sin_t;
				a_col_m[i] = (-1) * r[i][k] * sin_t + r[i][m] * cos_t;
			}
			for(int i=0; i<n; i++) {
				r[i][k] = a_col_k[i];
				r[i][m] = a_col_m[i];
			}
			
			//System.out.printf("jacobi: R(%d)\n", it+1);
			//matrixShow(r, "r = ");
			
		}
		System.out.println("ヤコビエラー");
		return r;
	}

	public static void main(String [] args) throws Exception{
		
		/*ファイルエラーチェック*/
		if(args.length != 1) System.out.println("inputエラー");
		double[][] a;
		System.out.println("program started");
		a = readFromFile(args[0]);
		/*正方行列かどうか*/
		if(a.length != a[0].length) {
			System.out.println("正方行列でない\n");
			System.exit(1);
		}
		matrixShow(a,"入力行列 a=");
		
		double b[][] = new double[a.length][a[0].length];
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[0].length; j++)
				b[i][j] = a[i][j];
		
		/*単位行列の作成*/
		double r[][] = new double[a.length][a[0].length];
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[0].length; j++) {
				if(i == j) r[i][j] = 1;
				else r[i][j] = 0;
			}
		matrixShow(r,"単位行列 r=");
		
		/*ヤコビ法*/
		double j[][] = new double[a.length][a[0].length];
		System.out.println("\n\n");
		j = jacobi(a, r, args[0]);
		
		/*以下、GUI*/
		JFrame frame = new JFrame("Task14-2"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		Container contentPane = frame.getContentPane(); 
	
		GPanel graph= new GPanel();
		
		graph.PutRed(j);
		graph.PutBlue(b);
		contentPane.add(graph, BorderLayout.CENTER);
		
		
		frame.setVisible(true); 
	
	}

	
}

class GPanel extends JPanel{

	private Point2D.Float[] points;//赤線
	private double blue[][];//青線(始点)
	private double blue2[][];//青線(終点)
	
	public static void matrixShow( double[][] a, String s) {
		System.out.println(s);
		for(int i=0; i<a.length; i++) {
			for(int j=0; j<a[0].length; j++) {
				System.out.printf("%7.3f  ",a[i][j]);
			}
			System.out.printf("\r\n");
		}
	}

	public void PutRed (double[][] j){
		points = new Point2D.Float[j.length];
		points[0] = new Point2D.Float((float)j[0][0], (float)j[1][0]);
		points[1] = new Point2D.Float((float)j[0][1], (float)j[1][1]);
		
		/*無理やり入れてる*/
		//points[0] = new Point2D.Float((float)1, (float)-4);
		//points[1] = new Point2D.Float((float)1, (float)1);
		
		System.out.println("\n【pointsの中身】");
		System.out.printf("points[0] = (%f, %f)\n",points[0].x,points[0].y);
		System.out.printf("points[1] = (%f, %f)\n",points[1].x,points[1].y);
	}
	public void PutBlue (double[][] a){
		blue = new double[21*21][2];//(xUpper-xLower+1) & (yUpper-yLower+1)
		//System.out.println("\n【blueの中身】");
		for(int i=0; i<21; i++) {
			for(int j=0; j<21; j++) {
				blue[21*i+(j)][0] = j-10;//x
				blue[21*i+(j)][1] = -i+10;//y
				//System.out.printf("blue[%3d]=(%3.0f, %3.0f)",21*i+(j+1),blue[i][0],blue[i][1]);
			}
			//System.out.printf("\n");
		}
		/*お試し出力*/
		for(int k=0; k<441; k++) {
			System.out.printf("blue[%3d]=(%3.0f, %3.0f)",k+1,blue[k][0],blue[k][1]);
			if((k+1)%21 == 0) System.out.printf("\n");
		}
		
		/*青の終点計算*/
		blue2 = new double[21*21][2];
		for(int i=0; i<21*21; i++) {
			blue2[i][0] = blue[i][0]*a[0][0] + blue[i][1]*a[0][1];
			blue2[i][1] = blue[i][0]*a[1][0] + blue[i][1]*a[1][1];
		}
		/*お試し出力*/
		matrixShow(a,"");
		for(int k=0; k<441; k++) {
			System.out.printf("blue2[%3d]=(%3.0f, %3.0f)",k+1,blue2[k][0],blue2[k][1]);
			if((k+1)%21 == 0) System.out.printf("\n");
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.black);
		g2d.setStroke(new BasicStroke());
		g2d.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));

		Float xLower = -15f;
		Float xUpper = 15f;
		Float xInterval = 1f;
		Float yLower = -15f;
		Float yUpper = 15f;
		Float yInterval = 1f;
		Float dx = xUpper - xLower;
		Float dy = yUpper - yLower;

		drawCenteredString(g2d, "15816053", 250, 25, (float) 0.);
		drawCenteredString(g2d, "Graph", 250, 475, (float) 0.);
		drawCenteredString(g2d, "Graph", 25, 250, (float) -Math.PI / 2);
		drawCenteredString(g2d, xLower.toString(), 50, 475, (float) 0);
		drawCenteredString(g2d, xUpper.toString(), 450, 475, (float) 0);
		drawCenteredString(g2d, yLower.toString(), 25, 450, (float) 0);
		drawCenteredString(g2d, yUpper.toString(), 25, 50, (float) 0);
		
		/*グラフの格子を書いている*/
		/*
		g2d.setPaint(Color.gray);
		for (Float x = 50f; x <= 450; x += 400 * xInterval / dx)
			g2d.draw(new Line2D.Float(x, 450, x, 50));
		for (Float y = 50f; y <= 450; y += 400 * yInterval / dy)
			g2d.draw(new Line2D.Float(45, y, 450, y));
		 */
		g2d.setPaint(Color.red);
		Float diam = 8f;
		int num_points = points.length;
		/*
		for (int i = 0; i < num_points; i++) {
			Float ex = 400 * (points[i].x - xLower) / dx + 50;
			ex -= diam / 2;
			Float ey = -400 * (points[i].y - yLower) / dy + 450;
			ey -= diam / 2;
			g2d.fill(new Ellipse2D.Float(ex, ey, diam, diam));
		}
		
		for (int i = 0; i < num_points - 1; i++) {
			Float ex1 = 400 * (points[i].x - xLower) / dx + 50;
			Float ey1 = -400 * (points[i].y - yLower) / dy + 450;
			Float ex2 = 400 * (points[i+1].x - xLower) / dx + 50;
			Float ey2 = -400 * (points[i+1].y - yLower) / dy + 450;
			g2d.draw(new Line2D.Float(ex1, ey1, ex2, ey2));
		}
		*/
		/*青線*/
		g2d.setPaint(Color.blue);
		for(int i=0; i<blue2.length; i++) {
			Float ex1 = 400 * ((float)blue[i][0] - xLower) / dx + 50;
			Float ey1 = -400 * ((float)blue[i][1] - yLower) / dy + 450;
			Float a = (float)(blue[i][0] + (blue2[i][0]-blue[i][0])/20 );
			Float b = (float)(blue[i][1] + (blue2[i][1]-blue[i][1])/20 );
			Float ex2 = 400 * (a - xLower) / dx + 50;
			Float ey2 = -400 * (b - yLower) / dy + 450;
			g2d.draw(new Line2D.Float(ex1, ey1, ex2, ey2));
		}
		/*赤線*/
		Float x1 = 0f;
		Float y1 = 0f;
		g2d.setPaint(Color.red);
		for(int i=0; i<points.length; i++) {
			for(int j=0; j<(xUpper-xLower+1); j++) {
				Float x = j-Math.abs(xLower);
				Float y = (points[i].y/points[i].x) * (j-Math.abs(xLower));
				Float ex1 = 400 * (x1 - xLower) / dx + 50;
				Float ey1 = -400 * (y1 - yLower) / dy + 450;
				Float ex2 = 400 * (x - xLower) / dx + 50;
				Float ey2 = -400 * (y - yLower) / dy + 450;
				if(j > 0) g2d.draw(new Line2D.Float(ex1, ey1, ex2, ey2));
				x1 = x;
				y1 = y;
			}
		}
		
		/*x=0の線を引く*/
		g2d.setPaint(Color.black);
		Float eyA = 400 * ((float)-15 - yLower) / dy + 50;
		Float eyB = 400 * ((float)15 - yLower) / dy + 50;
		Float ex = -400 * (0 - xLower) / dx + 450;
		g2d.draw(new Line2D.Float(ex,eyA,ex,eyB));
		/*y=0の線を引く*/
		Float exA = 400 * ((float)-15 - xLower) / dx + 50;
		Float exB = 400 * ((float)15 - xLower) / dx + 50;
		Float ey = -400 * (0 - yLower) / dy + 450;
		g2d.draw(new Line2D.Float(exA,ey,exB,ey));
		

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