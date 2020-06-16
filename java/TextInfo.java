//15816053
//田澤航樹

package p6;

import java.io.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class TextInfo {
	private String source_name;
	private int total = 0;
	private HashMap<String, Integer> word_freq = new HashMap<String, Integer>();

	/*コンストラクタ*/
	TextInfo(String fn){
		setSourceName(fn);//入力ファイル名をセット
	}
	
	/*ファイル名のゲッターとセッター*/
	String getSourceName(){
		return source_name;
	}
	void setSourceName(String s){
		source_name = s;
	}
	
	
	/*出現頻度を数える関数*/
	private void countFreq(String word){
		/*ハッシュマップにそのキーのものが存在していれば出現頻度を+1*/
		/*しなければ出現回数1をセットする*/
		if(word_freq.containsKey(word))
			word_freq.put(word, word_freq.get(word)+1);
		else
			word_freq.put(word, 1);
		/*containsKey　→　ハッシュマップにそのキーのものが存在すれば真を返す*/
	}
	
	/*ここからいろんな関数に飛ぶ*/
	void readFile(){
		try {
			FileReader fr = new FileReader(getSourceName());
			BufferedReader br = new BufferedReader(fr);
			
			String s, token[];
			while((s = br.readLine()) != null) {//1行ずつ読み込む
				token = StringUtils.split(s);
				for(int i=0;i<token.length;i++) {
					countFreq(token[i]);//出現頻度を記録＆ハッシュマップにワード登録
					total++;//総文字数を記録
				}
			}
			br.close();
			fr.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/*出現頻度のゲッター*/
	int getFreq(String word){
		return word_freq.get(word);
	}

	/*出現頻度を出力する関数*/
	void printAllFreq(){
		for(String x: word_freq.keySet())
			System.out.printf("Frequency of \"%s\": %d\n", x,getFreq(x));
	}
	
	/*自己情報量を計算する関数*/
	double calSelfInfo(String word){
		return (-1.0)*(Math.log10(word_freq.get(word)/(double)total) / Math.log10(2.0) );
	}

	/*最後の情報量を出力する関数*/
	void printAllSelfInfo(){
		for(String x: word_freq.keySet())
			System.out.printf("Self-Information of \"%s\": %.3f\n",x,calSelfInfo(x));
	}
	
	/*エントロピーの計算をする関数*/
	double calInfo(){
		double answer=0;
		for(String x: word_freq.keySet())
			answer += (word_freq.get(x)/(double)total)*calSelfInfo(x);
		return answer;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		TextInfo source = new TextInfo(args[0]);
		System.out.println("Information source: " + source.getSourceName());
		source.readFile();
		source.printAllFreq();
		source.printAllSelfInfo();
		System.out.printf("Information of \"%s\": %.3f\n", source.getSourceName(), source.calInfo());

		/* If you implement your own functions, uncomment the following two lines, and
		 * make a code to test your own functions under those lines.
		 */
		//System.out.println("\n Hereafter, the results of my own functions");
		//System.out.println("=========================================");


	}

}
