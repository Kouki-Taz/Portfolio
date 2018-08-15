//15816053
//�c�V�q��
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
	
	
	/*�R���X�g���N�^*/
	WordCount(String fn){
		setSourceName(fn);//���̓t�@�C�������Z�b�g
	}
	
	
	/*�t�@�C�����̃Q�b�^�[�ƃZ�b�^�[*/
	String getSourceName(){
		return source_name;
	}
	void setSourceName(String s){
		source_name = s;
	}
	
	
	/*�o���p�x�𐔂���֐�*/
	private void countFreq(String word){
		/*�n�b�V���}�b�v�ɂ��̃L�[�̂��̂����݂��Ă���Ώo���p�x��+1*/
		/*���Ȃ���Ώo����1���Z�b�g����*/
		if(word_freq.containsKey(word))
			word_freq.put(word, word_freq.get(word)+1);
		else
			word_freq.put(word, 1);
		/*containsKey�@���@�n�b�V���}�b�v�ɂ��̃L�[�̂��̂����݂���ΐ^��Ԃ�*/
	}
	
	
	/*�t�@�C���̃f�[�^��ǂݍ���*/
	void readFile(){
		try {
			FileReader fr = new FileReader(getSourceName());
			BufferedReader br = new BufferedReader(fr);
			
			String s, token[];
			while((s = br.readLine()) != null) {//1�s���ǂݍ���
				token = StringUtils.split(s);
				for(int i=0;i<token.length;i++) {
					/*��V��ۑ�@�ǉ��v����*/
					token[i] = token[i].toLowerCase();//�������ɂ���
					/*  �������A��������������̂��̂���菜�� ��  */
					token[i] = StringUtils.strip(token[i], ",");
					token[i] = StringUtils.strip(token[i], ".");
					token[i] = StringUtils.strip(token[i], "�h");
					token[i] = StringUtils.strip(token[i], "�g");
					token[i] = StringUtils.strip(token[i], "(");
					token[i] = StringUtils.strip(token[i], ")");
					countFreq(token[i]);//�o���p�x���L�^���n�b�V���}�b�v�Ƀ��[�h�o�^
					total++;//�����������L�^
				}
			}
			br.close();
			fr.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	
	/*�o���p�x�̃Q�b�^�[*/
	int getFreq(String word){
		return word_freq.get(word);
	}
	/*�o���p�x���o�͂���֐�*/
	void printAllFreq(){
		for(String x: word_freq.keySet())
			System.out.printf("Frequency of \"%s\": %d\n", x,getFreq(x));
	}
	
	
	/*Exercise 1*/
	Set<String> getWords(){
		return word_freq.keySet();
	}
	void printTopNFrequentWords(int n) {
		/*�\�[�g*/
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
	HashMap<String, Double> TFIDF = new HashMap<String, Double>();//TFIDF�̃n�b�V���}�b�v��錾
	
	void calTFIDF(HashMap<String,Integer>doc_freq, int docNum) {
		for(String x: word_freq.keySet()) {
			double tfidf = ((double)word_freq.get(x) / (double)total) * (Math.log((double)docNum / (double)doc_freq.get(x)));
			TFIDF.put(x, tfidf);
		}
		
	}
	void printTopNTfIdfWords(int n) {
		/*�\�[�g*/
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
		
		/*�@word_allfreq�@�F�@�S���̒P�ꂪ�����̃t�@�C���Ŏg���Ă��邩��o�^���Ă���*/
		HashMap<String, Integer> word_allfreq = new HashMap<String, Integer>();
		
		/*�S�t�@�C���̃f�[�^���i�[����*/
		for(File file: files) {
			WordCount source = new WordCount(file.toString());//�t�@�C�����𖳗����String�^�ɂ��ăZ�b�^�[�ɓn����
			source.readFile();
			wc_list.add(source);//�t�@�C���̒l��S���ǂݍ��ݏI���Ă���ǉ�����
		}
		
		/*���܂�HM word_allfreq�ɑS���̒P�ꂪ����i�O�ŏ������j*/
		for(int i=0; i<wc_list.size(); i++) {
			for(String key : wc_list.get(i).getWords())
				word_allfreq.put(key,0);
		}
		/*������ł����̃t�@�C���Ɋe�P�ꂪ�o�Ă邩���L�^*/
		for(String x : word_allfreq.keySet()) {
			for(int i=0; i<wc_list.size(); i++) {
				if(wc_list.get(i).word_freq.containsKey(x))
					word_allfreq.put(x, word_allfreq.get(x)+1);
			}
		}
		
		/*ArrayList�̗v�f�ł���i�Ԗڂ̃t�@�C����WordCount�^��word_freq��total��n��*/
		for(int i=0; i<wc_list.size();i++) {
			wc_list.get(i).calTFIDF(word_allfreq, wc_list.size());
		}
		
				
		/*���ʏo��*/
		for(int i=0; i<wc_list.size(); i++) {
			System.out.println(files[i].toString());//�t�@�C�����o��
			wc_list.get(i).printTopNFrequentWords(5);//�g�b�v5�̏o���p�x��word���o��
			wc_list.get(i).printTopNTfIdfWords(5);//�g�b�v5��TFIDF��word���o��
		}
		
		/*Exercise7-3�� void readFile()����ύX����*/
		
	}

}
