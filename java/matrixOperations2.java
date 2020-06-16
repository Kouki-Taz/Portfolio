//15816053
//ìc‡Vçqé˜
package p9;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class matrixOperations2 {

	public static void main(String[] args) {
		
		try {
			/*aì«Ç›çûÇﬁ*/
			Scanner input = new Scanner (new File(args[0]));
			int rows = 0;
			int columns = 0;
			
			while(input.hasNextLine()) {
				++rows;
				Scanner colReader = new Scanner(input.nextLine());
				columns = 0;
				while(colReader.hasNextDouble()) {
					double dummy = colReader.nextDouble();
					++columns;
				}
			}
			ArrayList<ArrayList<Double>> a = new ArrayList<ArrayList<Double>>();
			input = new Scanner(new File(args[0]));
			while(input.hasNextLine()) {
				Scanner colReader = new Scanner(input.nextLine());
				ArrayList col = new ArrayList();
				while(colReader.hasNextDouble()) {
					col.add(colReader.nextDouble());
				}
				a.add(col);
			}
			
			/*bì«Ç›çûÇﬁ*/
			Scanner input1 = new Scanner (new File(args[1]));
			int rows1 = 0;
			int columns1 = 0;
			
			while(input1.hasNextLine()) {
				++rows1;
				Scanner colReader1 = new Scanner(input1.nextLine());
				columns1 = 0;
				while(colReader1.hasNextDouble()) {
					double dummy1 = colReader1.nextDouble();
					++columns1;
				}
			}
			ArrayList<ArrayList<Double>> b = new ArrayList<ArrayList<Double>>();
			input = new Scanner(new File(args[1]));
			while(input.hasNextLine()) {
				Scanner colReader = new Scanner(input.nextLine());
				ArrayList col = new ArrayList();
				while(colReader.hasNextDouble()) {
					col.add(colReader.nextDouble());
				}
				b.add(col);
			}
			
			
			
			StringBuilder builder = new StringBuilder();
			/*ë´ÇµéZ*/
			builder.append("add\r\n");
			for(int i=0; i<a.size(); i++) {
				for(int j=0;j<a.get(0).size();j++) {
					builder.append(a.get(i).get(j) + b.get(i).get(j)+"");
					if(j<(a.get(0).size()-1))
						builder.append(",");
				}
				builder.append("\r\n");
			}
			builder.append("\r\n");
			/*à¯Ç´éZ*/
			builder.append("sub\r\n");
			for(int i=0; i<a.size(); i++) {
				for(int j=0;j<a.get(0).size();j++) {
					builder.append(a.get(i).get(j) - b.get(i).get(j)+"");
					if(j<(a.get(0).size()-1))
						builder.append(",");
				}
				builder.append("\r\n");
			}
			builder.append("\r\n");
			/*ä|ÇØéZ*/
			builder.append("mult\r\n");
			for(int i=0; i<a.size(); i++) {
				for(int j=0;j<a.get(0).size();j++) {
					double c =0;
					for(int k=0;k<a.size();k++) {
						c += a.get(i).get(k) * b.get(k).get(j);
					}
					builder.append(c+"");
					if(j<(a.get(0).size()-1)) 
						builder.append(",");
				}
				builder.append("\r\n");
			}
			builder.append("\r\n");
			BufferedWriter writer = new BufferedWriter(new FileWriter("Output4.txt"));
			writer.write(builder.toString());
			writer.close();
			
		}catch(Exception e){
				System.out.println(e.getMessage());
		}


	}

}
