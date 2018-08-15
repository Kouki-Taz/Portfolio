//15816053
//田澤航樹
package p7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.Map.Entry;
import org.apache.commons.lang3.*;


public class WordCount {
	private String source_name;
	private int total = 0;
	private HashMap<String, Integer> word_freq = new HashMap<String, Integer>();
	
	
	/*コンストラクタ*/
	WordCount(String fn){
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
	
	
	/*ファイルのデータを読み込む*/
	void readFile(){
		try {
			FileReader fr = new FileReader(getSourceName());
			BufferedReader br = new BufferedReader(fr);
			
			String s, token[];
			while((s = br.readLine()) != null) {//1行ずつ読み込む
				token = StringUtils.split(s);
				for(int i=0;i<token.length;i++) {
					/*第７回課題　追加」部分*/
					token[i] = token[i].toLowerCase();//小文字にする
					/*  ↓文頭、文末から第二引数のものを取り除く ↓  */
					token[i] = StringUtils.strip(token[i], ",");
					token[i] = StringUtils.strip(token[i], ".");
					token[i] = StringUtils.strip(token[i], "”");
					token[i] = StringUtils.strip(token[i], "“");
					token[i] = StringUtils.strip(token[i], "(");
					token[i] = StringUtils.strip(token[i], ")");
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
	
	
	/*Exercise 1*/
	Set<String> getWords(){
		return word_freq.keySet();
	}
	void printTopNFrequentWords(int n) {
		/*ソート*/
		ArrayList<Entry<String,Integer>>entries = new ArrayList<Entry<String,Integer>>(word_freq.entrySet());
		Collections.sort(entries, new Comparator<Entry<String,Integer>>(){
			public int compare(Entry<String,Integer>obj1, Entry<String,Integer>obj2)
			{
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});
		
		System.out.printf("Top %d words in frequency\n", n);
		int i=0;
		for(Entry<String, Integer> entry : entries) {
			System.out.printf("%d: %s (%d)\n",i+1,entry.getKey(),entry.getValue());
			i++;
			if(i==n)
				break;
		}
	}
	
	
	/*Exercise 2*/
	HashMap<String, Double> TFIDF = new HashMap<String, Double>();//TFIDFのハッシュマップを宣言
	
	void calTFIDF(HashMap<String,Integer>doc_freq, int docNum) {
		for(String x: word_freq.keySet()) {
			double tfidf = ((double)word_freq.get(x) / (double)total) * (Math.log((double)docNum / (double)doc_freq.get(x)));
			TFIDF.put(x, tfidf);
		}
		
	}
	void printTopNTfIdfWords(int n) {
		/*ソート*/
		ArrayList<Entry<String,Double>>entries = new ArrayList<Entry<String,Double>>(TFIDF.entrySet());
		Collections.sort(entries, new Comparator<Entry<String,Double>>(){
			public int compare(Entry<String,Double>obj1, Entry<String,Double>obj2)
			{
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});
		
		System.out.printf("Top %d words in TF-IDF weight\n", n);
		int i=0;
		for(Entry<String, Double> entry : entries) {
			System.out.printf("%d: %s (%.3f)\n",i+1,entry.getKey(),entry.getValue());
			i++;
			if(i==n)
				break;
		}
	}
	
	
	public static void main(String[] args) {
		File dir = new File(args[0]);
		File []files = dir.listFiles();
		List<WordCount>wc_list = new ArrayList<WordCount>();
		
		/*　word_allfreq　：　全部の単語がいくつのファイルで使われているかを登録している*/
		HashMap<String, Integer> word_allfreq = new HashMap<String, Integer>();
		
		/*全ファイルのデータを格納する*/
		for(File file: files) {
			WordCount source = new WordCount(file.toString());//ファイル名を無理やりString型にしてセッターに渡した
			source.readFile();
			wc_list.add(source);//ファイルの値を全部読み込み終えてから追加する
		}
		
		/*↓まずHM word_allfreqに全部の単語が入る（０で初期化）*/
		for(int i=0; i<wc_list.size(); i++) {
			for(String key : wc_list.get(i).getWords())
				word_allfreq.put(key,0);
		}
		/*↓これでいくつのファイルに各単語が出てるかを記録*/
		for(String x : word_allfreq.keySet()) {
			for(int i=0; i<wc_list.size(); i++) {
				if(wc_list.get(i).word_freq.containsKey(x))
					word_allfreq.put(x, word_allfreq.get(x)+1);
			}
		}
		
		/*ArrayListの要素であるi番目のファイルのWordCount型のword_freqとtotalを渡す*/
		for(int i=0; i<wc_list.size();i++) {
			wc_list.get(i).calTFIDF(word_allfreq, wc_list.size());
		}
		
				
		/*結果出力*/
		for(int i=0; i<wc_list.size(); i++) {
			System.out.println(files[i].toString());//ファイル名出力
			wc_list.get(i).printTopNFrequentWords(5);//トップ5の出現頻度のwordを出力
			wc_list.get(i).printTopNTfIdfWords(5);//トップ5のTFIDFのwordを出力
		}
		
		/*Exercise7-3は void readFile()内を変更した*/
		
	}

}
