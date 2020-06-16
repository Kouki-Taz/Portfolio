//15816053
//�c�V�q��

package p6;

import java.io.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class TextInfo {
	private String source_name;
	private int total = 0;
	private HashMap<String, Integer> word_freq = new HashMap<String, Integer>();

	/*�R���X�g���N�^*/
	TextInfo(String fn){
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
	
	/*�������炢���Ȋ֐��ɔ��*/
	void readFile(){
		try {
			FileReader fr = new FileReader(getSourceName());
			BufferedReader br = new BufferedReader(fr);
			
			String s, token[];
			while((s = br.readLine()) != null) {//1�s���ǂݍ���
				token = StringUtils.split(s);
				for(int i=0;i<token.length;i++) {
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
	
	/*���ȏ��ʂ��v�Z����֐�*/
	double calSelfInfo(String word){
		return (-1.0)*(Math.log10(word_freq.get(word)/(double)total) / Math.log10(2.0) );
	}

	/*�Ō�̏��ʂ��o�͂���֐�*/
	void printAllSelfInfo(){
		for(String x: word_freq.keySet())
			System.out.printf("Self-Information of \"%s\": %.3f\n",x,calSelfInfo(x));
	}
	
	/*�G���g���s�[�̌v�Z������֐�*/
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u

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
