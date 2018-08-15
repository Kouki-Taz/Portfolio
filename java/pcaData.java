//15816053
//田澤航樹

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;

public class pcaData{
	public static void main(String [] args){
		JFrame frame = new JFrame("Task14-3");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		Container contentPane = frame.getContentPane();
		
		GPanel makeGraph = new GPanel();
		
		makeGraph.readfile(args[0], args[1]);
		contentPane.add(makeGraph, BorderLayout.CENTER);
		
		frame.setVisible(true);
	}
}

class GPanel extends JPanel{
	/*入力データを格納する*/
	ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	private static Point2D.Double[] points;
	
	/*ファイルから読み込む*/
	public void readfile(String fin, String fout){
		/*---ファイルのデータを一度全部String型で読み込んで使うデータだけをDouble型に変更して使うイメージ---*/
		try{
			Scanner input = new Scanner(new File(fin)); 
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fout)));
			int lines = 0;
			
			while(input.hasNextLine()){
				lines++;
				String dummy = input.nextLine();
			}
			input.close();
			
			input = new Scanner(new File(fin));
			while(input.hasNextLine()) {
				int i = 0;
				Scanner colReader = new Scanner(input.nextLine());
				ArrayList col = new ArrayList();
				while(colReader.hasNext()) {
					colReader.useDelimiter(",");
					col.add(colReader.next());
					i++;
				}
				data.add(col);
			}
			input.close();
			
			double[][] x = new double[data.size() - 1][data.get(0).size() - 1];
			for(int i=0; i<x.length; i++) 
				for(int j=0; j<x[i].length; j++) 
					x[i][j] = Double.parseDouble(data.get(i).get(j));
			
			matrixPrint(x, "X[][] =", pw);
			pca(x, pw);
			
		}catch(Exception e){
			System.out.println("Exception: " + e);
		}
		
	}
	
	public static double[][] matrixMul(double[][] a, double[][] b){
		int rows = a.length;
		int colums = b[0].length;
		
		double[][] c = new double[rows][colums];
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < colums;j++) {
				double sum = 0.0;
				for(int k = 0;k < a[0].length;k++) 
					sum += a[i][k] * b[k][j];
				c[i][j] = sum;
			}
		}
		return(c);
	}
	public static void matrixPrint(double[][] a, String s, PrintWriter pw){
		pw.printf("%s\r\n", s);
		for(int i = 0;i < a.length;i++) {
			for(int j = 0;j < a[0].length;j++) 
				pw.printf("%8.3f ",a[i][j]);
			pw.println("");
		}
		pw.println("");
	}
	
	/*ヤコビ法*/
	static double[][] eigen_vector;
	static double[] eigen_value;
	static double[] sorted;
	static void jacobi(double[][] a, double[][] r, PrintWriter pw) {
		
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
		
		/*単位行列を生成*/
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				r[i][j] = 0.0;
			}
			r[i][i] = 1.0;
		}
		
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
	
			if(max == eps){
				pw.printf("jacobi: finished after %d cycles s = %f\r\n", it, a[k][m]);
				/*処理結果の出力*/
				pw.println("\r\n【Final result】");
				pw.printf("eigen value\r\n");
				for(int i=0; i<n; i++) {
					pw.printf("%6f ", a[i][i]);
					eigen_value[i] = a[i][i];
					sorted[i] = a[i][i];
				}
				pw.printf("\r\neigen vectors\r\n");
				for(int i=0; i<n; i++) {
					for(int j=0; j<n; j++) {
						pw.printf("%6f ", r[i][j]);
						eigen_vector[i][j] = r[i][j];
					}
					pw.printf("\r\n");
				}
				return;
			}
			else {
				pw.printf("jacobi: cycle %d, found non-orthogonral max a[%d][%d]=%f\r\n", it+1,k,m,max);
			}
	
			/*対称行列かどうか*/
			for(int i=0; i<n; i++) {
				for(int j=0; j<n; j++) {
					if(a[i][j] != a[j][i]) {
						System.out.println("non-symmetric error\n");
						System.exit(1);
					}
				}
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
			pw.printf("jacobi: cos_t = %f, sin_t = %f\r\n", cos_t,sin_t);
			
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

			/*新しいAを出力*/
			pw.printf("jacobi: A(%d)", it+1);
			matrixPrint(a, "a[][] =", pw);
			
			for(int i=0; i<n; i++) {
				a_col_k[i] = r[i][k] * cos_t + r[i][m] * sin_t;
				a_col_m[i] = (-1) * r[i][k] * sin_t + r[i][m] * cos_t;
			}
			for(int i=0; i<n; i++) {
				r[i][k] = a_col_k[i];
				r[i][m] = a_col_m[i];
			}
			/*新しいRを出力*/
			pw.printf("jacobi: R(%d)", it+1);
			matrixPrint(r, "r[][] =", pw);
		}
	}

	/*PCA*/
	public void pca(double[][] Xc, PrintWriter pw){
		
		double ave1=0, ave2=0, ave3=0, ave4=0;
		
		/*列ごとの平均を求める*/
		for(int i = 0;i < Xc.length;i++) {
			for(int j = 0;j < Xc[i].length;j++) {
				if(j == 0) ave1 += Xc[i][j];
				if(j == 1) ave2 += Xc[i][j];
				if(j == 2) ave3 += Xc[i][j];
				if(j == 3) ave4 += Xc[i][j];
			}
		}
		ave1 /= Xc.length;
		ave2 /= Xc.length;
		ave3 /= Xc.length;
		ave4 /= Xc.length;
		
		pw.println("【average】");
		pw.println("ave1 = " + ave1);
		pw.println("ave2 = " + ave2);
		pw.println("ave3 = " + ave3);
		pw.println("ave4 = " + ave4);
		pw.println("");
		
		/*Xcを求める*/
		for(int i = 0;i < Xc.length;i++) {
			for(int j = 0;j < Xc[i].length;j++) {
				if(j == 0)  Xc[i][j] -= ave1;
				if(j == 1)  Xc[i][j] -= ave2;
				if(j == 2)  Xc[i][j] -= ave3;
				if(j == 3)  Xc[i][j] -= ave4;
			}
		}
		matrixPrint(Xc, "Xc[][] =", pw);
		
		/*Xcの転置行列の生成*/
		double[][] tennti = new double[Xc[0].length][Xc.length];
		for(int i = 0;i < Xc.length;i++)
			for(int j = 0;j < Xc[i].length;j++) 
				tennti[j][i] = Xc[i][j];
		//matrixPrint(tennti, "tennti =", pw);
		
		/*共分散行列を求める*/
		double[][] cmatrix = new double[tennti.length][tennti.length];
		cmatrix = matrixMul(tennti, Xc);
		matrixPrint(cmatrix, "cmatrix[][] =", pw);
		
		/*ヤコビ法*/
		double[][] r = new double[tennti.length][tennti.length];//単位行列
		eigen_vector = new double[tennti.length][tennti.length];
		eigen_value = new double[tennti.length];
		sorted = new double[tennti.length];
		jacobi(cmatrix, r, pw);
		
		/*大きいもの2つを選ぶ処理*/
		/*大きいものの番号*/
		int index1 = 0;
		int index2 = 0;;
		/*固有値をソートする(小さい順)*/
		Arrays.sort(sorted);
		for(int i = 0;i < eigen_value.length;i++) {
			if(sorted[eigen_value.length - 1] == eigen_value[i]) index1 = i;//1番目に大きいものと比較
			if(sorted[eigen_value.length - 2] == eigen_value[i]) index2 = i;//2番目に大きいものと比較
		}
		/*最大値、2番目に大きいものを別で保存*/
		double[][] Xc_select = new double[Xc.length][2];
		double[][] eigenvector_select = new double[tennti.length][2];
		for(int i = 0;i < tennti.length;i++) {
			for(int j = 0;j < tennti[i].length;j++) {
				if(index1 == j) 
					eigenvector_select[i][0] = eigen_vector[i][j];
				if(index2 == j) 
					eigenvector_select[i][1] = eigen_vector[i][j];
			}
		}
		matrixPrint(eigenvector_select, "\r\neigenvector_select[][] =", pw);
		
		/*Xpを生成*/
		double[][] Xp = new double[Xc.length][Xc[0].length];
		Xp = matrixMul(Xc, eigenvector_select);
		matrixPrint(Xp, "Xp[][] =", pw);
		/*Xpの座標をpoints[]に入れる*/
		points = new Point2D.Double[Xc.length];
		for(int i = 0;i < points.length;i++) 
			points[i] = new Point2D.Double(Xp[i][0], Xp[i][1]);
		
		pw.close();
	}
	
	
	
	
	/*----------以下、GUIの部分----------*/
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.black);
		g2d.setStroke(new BasicStroke());
		g2d.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));
		
		Float xLower = (float) -4;
		Float xUpper = (float) 4;
		Float xInterval = (float) 0.8;
		Float yLower = (float) -1.5;
		Float yUpper = (float) 1.5;
		Float yInterval = (float) 0.3;
		
		Float dx = (float) (xUpper - xLower);
		Float dy = (float) (yUpper - yLower);

		drawCenteredString(g2d, "15816053 Kouki Tazawa", 250, 25, (float) 0.);
		drawCenteredString(g2d, "x", 250, 475, (float) 0.);
		drawCenteredString(g2d, "y", 25, 250, (float) -Math.PI / 2);
		drawCenteredString(g2d, xLower.toString(), 50, 255, (float) 0);
		drawCenteredString(g2d, xUpper.toString(), 450, 255, (float) 0);
		drawCenteredString(g2d, yLower.toString(), 235, 450, (float) 0);
		drawCenteredString(g2d, yUpper.toString(), 240, 50, (float) 0);
		
		/*irisのラベル*/
		g2d.setPaint(Color.red);
		drawCenteredString(g2d, "Iris-setosa", 500, 25, (float) 0.);
		g2d.setPaint(Color.blue);
		drawCenteredString(g2d, "Iris-versicolor", 500, 35, (float) 0.);
		g2d.setPaint(Color.green);
		drawCenteredString(g2d, "Iris-virginica", 500, 45, (float) 0.);

		Float diam = 8f;
		int num_points = points.length;
		
		/*点をプロット*/
		/*赤の点*/
		g2d.setPaint(Color.red);
		for (int i = 0;i < 50;i++) {
			double ex = 400 * (points[i].x - xLower) / dx + 50;
			ex -= diam / 2;
			double ey = -400 * (points[i].y - yLower) / dy + 450;
			ey -= diam / 2;
			g2d.fill(new Ellipse2D.Double(ex, ey, diam, diam));
		}
		/*青の点*/
		g2d.setPaint(Color.blue);
		for (int i = 50;i < 100;i++) {
			double ex = 400 * (points[i].x - xLower) / dx + 50;
			ex -= diam / 2;
			double ey = -400 * (points[i].y - yLower) / dy + 450;
			ey -= diam / 2;
			g2d.fill(new Ellipse2D.Double(ex, ey, diam, diam));
		}
		/*緑の点*/
		g2d.setPaint(Color.green);
		for (int i = 100;i < num_points;i++) {
			double ex = 400 * (points[i].x - xLower) / dx + 50;
			ex -= diam / 2;
			double ey = -400 * (points[i].y - yLower) / dy + 450;
			ey -= diam / 2;
			g2d.fill(new Ellipse2D.Double(ex, ey, diam, diam));
		}
		
		/*軸を書く*/
		g2d.setPaint(Color.black);
		/*x軸*/
		Float exA = (xLower - xLower) / dx + 50;
		Float exB = 400 * (xUpper - xLower) / dx + 50;
		Float ey = -400 * (0 - yLower) / dy + 450;
		g2d.draw(new Line2D.Float(exA,ey,exB,ey));
		/*y軸*/
		Float eyA = (yLower - yLower) / dy + 50;
		Float eyB = 400 * (yUpper - yLower) / dy + 50;
		Float ex = -400 * (0 - xLower) / dx + 450;
		g2d.draw(new Line2D.Float(ex,eyA,ex,eyB));
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
