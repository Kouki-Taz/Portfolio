//15816053
//�c�V�q��

package p8;

import java.io.*;
import java.util.*;
import org.apache.commons.lang3.*;

class TreeNode {//�؍\����class
	private String label;
	private String elabel;
	ArrayList<TreeNode> children = new ArrayList<TreeNode>();
	
	/*�R���X�g���N�^*/
	TreeNode(String s){
		label = s;
	}
	
	/*�Z�b�^�[�ƃQ�b�^�[*/
	void setLabel(String s) {
		label = s;
	}
	void setElabel(String s) {
		elabel = s;
	}
	String getLabel() {
		return label;
	}
	String getElabel() {
		return elabel;
	}
	int getChildrenNum() {
		return children.size();
	}
	TreeNode getChild(int i) {
		if(0 <= i && i < children.size())
			return children.get(i);
		else
			return null;
	}
	
	void addChild(TreeNode n) {//ArrayList�ɒǉ�
		children.add(n);
	}

}


/*�f�[�^�̃��X�g*/
/*���̓f�[�^�̕\�̉�����ArrayList��Link��������*/
class DataList{
	private LinkedList <ArrayList<String>> data_list = new LinkedList <ArrayList<String>>();
	
	void add(ArrayList<String> d) {//LinkedList�ɒǉ�
		data_list.add(d);
	}
	
	/*�Q�b�^�[*/
	ArrayList<String> get(int i) {
		return data_list.get(i);
	}
	int size() {
		return data_list.size();
	}
}


/*�����̃��X�g*/
class AttrList{
	private ArrayList<String> attr_list = new ArrayList<String>();
	
	void add(String s) {//ArrayList�ɒǉ�
		attr_list.add(s);
	}
	
	/*�Q�b�^�[*/
	String get(int i) {
		return  attr_list.get(i);
	}
	int size() {
		return attr_list.size();
	}
	int indexOf(String s) {
		return attr_list.indexOf(s);
	}
}



public class SimpleDT {
	private String file_name;
	private AttrList attrNames = new AttrList();//�����̖��O
	private DataList data_list = new DataList();//���g�̃f�[�^
	private TreeNode root;
	
	/*�R���X�g���N�^*/
	SimpleDT(String s){
		file_name = s;
	}

	/*�Z�b�^�[*/
	void setAttribute(String s) {//�������t�@�C������Z�b�g����
		String token[];
		token = StringUtils.split(s);
		for(int i = 0; i < token.length; i++)
			attrNames.add(token[i]);
	}
	
	/*�t�@�C����ǂݍ���*/
	void readFile(){
		int data_num = 0;
		try{
			BufferedReader in = new BufferedReader (new FileReader(file_name));
			String s, token[];
			/*�������t�@�C������Z�b�g����(1�s�ڂ͕K������)*/
			s = in.readLine();
			setAttribute(s);
			/*�����̒l��ǂݍ��݁AArrayList�ɒǉ�����*/
			while((s = in.readLine()) != null){
				token = StringUtils.split(s);
				ArrayList<String> tmp_data = new ArrayList<String>();
				for(int i = 0; i < token.length; i++)
					tmp_data.add(token[i]);
				data_list.add(tmp_data);//n�s�ڂ̃f�[�^���i�[
				data_num++;
			}
			in.close();
		}
		catch(Exception e){
			System.out.println("Exception: " + e);
		}
		System.out.println("read " + data_num + " records");
	}
	
	DataList [] divideData(DataList dlist, int col, int div){
		DataList [] new_lists = new DataList [div];
		for(int i = 0; i < div; i++)
			new_lists[i] = new DataList();
		HashMap<String, Integer> index_list = new HashMap<String, Integer>();
		int index;
		
		for(int i = 0; i < dlist.size(); i++) {
			ArrayList<String> data = dlist.get(i);
			String value = data.get(col);
			if(index_list.containsKey(value))
				index = index_list.get(value);
			else {
				index = index_list.size();
				index_list.put(value, index);
			}
			new_lists[index].add(data);
		}
		/*���X�g��V��������Ă����Ԃ��Ă���*/
		return(new_lists);
	}
	
	/*�g�����������폜����i�c��̑�����V����list�ɓo�^���Ă���list��Ԃ��j*/
	AttrList deleteAttrib(AttrList alist, int col){
		AttrList new_list = new AttrList();
		
		for(int i = 0; i < alist.size(); i++) {
			if(i == col) continue;
			new_list.add(alist.get(i));
		}

		return(new_list);
	}
	
	double log2(double d) {
		/*  complete this method */
		return Math.log(d) / Math.log(2.0);
	}
	
	double subInfo(DataList dlist, int col, String a_value){
		double info = 0.0, total = 0.0;
		
		/*  complete this method */
		
		/*play,notplay�̏o���񐔂�HashMap�����*/
		HashMap<String, Integer> p_or_np = new HashMap<String, Integer>();
		
		for(int i=0;i<dlist.size();i++) {//14��
			/*a_value��dlist�̒���col�Ԗڂ̒l�Ɠ����Ƃ� (�Ⴆ�΁Aa_value��sunny�̂Ƃ��A�f�[�^�̒���sunny�̍s����������)*/
			
			if(a_value.equals(dlist.get(i).get(col))) {/*dlist�̒���ArrayList�̒���String*/
				if(p_or_np.containsKey( dlist.get(i).get(attrNames.size()-1)) )//dlist�̍Ō�̍���(play,notplay)�ɂ���
					p_or_np.put(dlist.get(i).get(attrNames.size()-1) , p_or_np.get( dlist.get(i).get(attrNames.size()-1)) + 1 );
				else
					p_or_np.put(dlist.get(i).get(attrNames.size()-1) , 1);
				total++;//�m���̕���
			}
		}
		
		/*�V���������HashMap�ōŌ�̌v�Z*/
		for(String x : p_or_np.keySet())
			info -=  ((double)p_or_np.get(x)/ total) * log2((double)p_or_np.get(x)/ total);
		
		return (info);
	}
	
	double postInfo(DataList dlist, int col, HashMap<String, Integer> freq){
		double info = 0.0;

		/*  complete this method */
		for(String x : freq.keySet())
			info += ( (double)freq.get(x)/ (double)dlist.size() ) * subInfo(dlist, col, x);
		return (info);
	}
	
	double preInfo(int d_num, HashMap<String, Integer> c_dist) {//d_num�͓��̓f�[�^�̍s��, c_dist��aValueCount�A���C���X�g��play,notplay�̃n�b�V���}�b�v
		double info = 0.0;

		/*  complete this method */
		for(String x : c_dist.keySet()) //play, notplay�̂Q��
			info -= ((double)c_dist.get(x)/(double)d_num) * log2((double)c_dist.get(x)/(double)d_num);

		return (info);		
	}
	
	/*�����O���[�v��������*/
	String findMajority(HashMap<String, Integer> freq_list) {
		int max_freq = 0;
		String max_key = null;
		for(String key: freq_list.keySet()) {
			if(max_freq < freq_list.get(key)) {
				max_freq = freq_list.get(key);
				max_key = key;  
			}
		}
		return(max_key);
	}
	
	/*�e������key(sunny,rain,hot,mild�Ȃ�)�Ƃ��A�o���񐔂𒆐g�Ƃ���*/
	ArrayList<HashMap<String, Integer>> attrValues(DataList dlist, AttrList alist){
		ArrayList<HashMap<String, Integer>> attrValueFreq = new ArrayList<HashMap<String, Integer>>();
		
		/*  complete this method */
		
		for(int i=0; i<alist.size(); i++) {//�����鑮���̐�����for������(alist��makeTree���J��Ԃ����тɏ������Ȃ��Ă���)
			HashMap<String, Integer> a_v = new HashMap<String, Integer>();
			for(int j=0; j<dlist.size(); j++) {//�f�[�^���X�g��1�s�����Ă���(14��)
				String x = dlist.get(j).get( attrNames.indexOf(alist.get(i)) );//�f�[�^���X�g���c�i�������Ɓj�Ō��悤�Ƃ��Ă���
				
				if(a_v.containsKey(x))
					a_v.put(x, a_v.get(x)+1);
				else
					a_v.put(x, 1);//���� : (������(String), �p�x(int))
			}
			attrValueFreq.add(a_v);
		}
		
				
		return attrValueFreq;
		
	}
	
	/*�؍\�������*/
	TreeNode makeTree(DataList dlist, AttrList alist) {
		ArrayList<HashMap<String, Integer>> aValueCount = attrValues(dlist, alist);
		DataList [] d_lists;
		AttrList new_alist;
		String cls_name = null;
		double max_gain = 0.0;
		int max_id = -1;
		int col;
		
		/*������1�����ɂȂ����Ƃ�*/
		if(alist.size() == 1) {
			cls_name = findMajority(aValueCount.get(0));
			return new TreeNode(cls_name);
		}
		/*play,notplay�̂ǂ��炩1�����ɂȂ�����*/
		if(aValueCount.get(alist.size()-1).size() == 1) {
			for(String key: aValueCount.get(alist.size()-1).keySet())
				cls_name = key;//label��play,notplay�����iPrintNode�����邽�߂Ɂj
			return new TreeNode(cls_name);
		}

		/*preInfo�̌v�Z*/
		double info0 = preInfo(dlist.size(), aValueCount.get(alist.size()-1));//��Q�����@�F�@input�̑����̒l�̍s�̍Ōオ���g��class������-1???????????
		System.out.printf("  preInfo: %.3f\n", info0);
		
		/*Information gain�̌v�Z (�×~�T���̂��߂̒l)*/
		for(int i = 0; i < alist.size() - 1; i++){
			double info = postInfo(dlist, attrNames.indexOf(alist.get(i)), aValueCount.get(i));//�f�[�^���X�g�A�����̒l�A�����̒l
			double gain = info0 - info;
			System.out.printf("    gain for %s: %.3f\n", alist.get(i), gain);
			if(max_gain < gain){
				max_gain = gain;
				max_id = i;
			}
		}
		
		if(max_gain == 0.0){
			cls_name = findMajority(aValueCount.get(alist.size() - 1));
			return new TreeNode(cls_name);
		}
		
		col = attrNames.indexOf(alist.get(max_id));
		d_lists = divideData(dlist, col, aValueCount.get(max_id).size());

		new_alist = deleteAttrib(alist, max_id);//�g�����������폜����

		TreeNode new_node = new TreeNode(alist.get(max_id));
		for(int i = 0; i <  d_lists.length; i++){
			if(d_lists[i].size() != 0){
				TreeNode tmp = makeTree(d_lists[i], new_alist);
				tmp.setElabel(d_lists[i].get(0).get(col));
				new_node.addChild(tmp);
			}
		}
		return (new_node);
	}
	
	void buildTree() {
		root = makeTree(data_list, attrNames);		
	}
	
	void printNode(TreeNode node, int level){

		/*  complete this method */
		
		/*level : �؍\���̐[��*/
		/*label : �߂̑���*/
		/*elabel : �ӂ̑����l*/
		/*get.Children.Num : �q�̐�*/
		/*get.Child : �q��TreeNode�^�ŕԂ�*/
		for(int i=0; i<node.getChildrenNum();i++) {
			
			if(level != 0)
				System.out.printf("|  ");
			
			System.out.printf("%s = %s",node.getLabel(),node.getChild(i).getElabel());
			if(node.getChild(i).getChildrenNum() == 0)//�q���Ȃ���΍ċA�I��
				System.out.printf(": %s\n",node.getChild(i).getLabel());
			else {//�q������΍ċA
				System.out.printf("\n");
				printNode(node.getChild(i), level+1);//���x����1���₷
			}
		}
		
		
		return;
	}

	void printTree() {
		printNode(root, 0);
	}
	
	public static void main(String[] args) {
		SimpleDT dt = new SimpleDT(args[0]);
		
		/* �f�[�^�̓Ǎ��� */
		dt.readFile();
		/* ����؂̍\�z */
		dt.buildTree();
		/* ����؂̕\�\�� */
		dt.printTree();
	}

}
