//15816053
//田澤航樹

package p8;

import java.io.*;
import java.util.*;
import org.apache.commons.lang3.*;

class TreeNode {//木構造のclass
	private String label;
	private String elabel;
	ArrayList<TreeNode> children = new ArrayList<TreeNode>();
	
	/*コンストラクタ*/
	TreeNode(String s){
		label = s;
	}
	
	/*セッターとゲッター*/
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
	
	void addChild(TreeNode n) {//ArrayListに追加
		children.add(n);
	}

}


/*データのリスト*/
/*入力データの表の横一列のArrayListをLinkしたもの*/
class DataList{
	private LinkedList <ArrayList<String>> data_list = new LinkedList <ArrayList<String>>();
	
	void add(ArrayList<String> d) {//LinkedListに追加
		data_list.add(d);
	}
	
	/*ゲッター*/
	ArrayList<String> get(int i) {
		return data_list.get(i);
	}
	int size() {
		return data_list.size();
	}
}


/*属性のリスト*/
class AttrList{
	private ArrayList<String> attr_list = new ArrayList<String>();
	
	void add(String s) {//ArrayListに追加
		attr_list.add(s);
	}
	
	/*ゲッター*/
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
	private AttrList attrNames = new AttrList();//属性の名前
	private DataList data_list = new DataList();//中身のデータ
	private TreeNode root;
	
	/*コンストラクタ*/
	SimpleDT(String s){
		file_name = s;
	}

	/*セッター*/
	void setAttribute(String s) {//属性をファイルからセットする
		String token[];
		token = StringUtils.split(s);
		for(int i = 0; i < token.length; i++)
			attrNames.add(token[i]);
	}
	
	/*ファイルを読み込む*/
	void readFile(){
		int data_num = 0;
		try{
			BufferedReader in = new BufferedReader (new FileReader(file_name));
			String s, token[];
			/*属性をファイルからセットする(1行目は必ず属性)*/
			s = in.readLine();
			setAttribute(s);
			/*属性の値を読み込み、ArrayListに追加する*/
			while((s = in.readLine()) != null){
				token = StringUtils.split(s);
				ArrayList<String> tmp_data = new ArrayList<String>();
				for(int i = 0; i < token.length; i++)
					tmp_data.add(token[i]);
				data_list.add(tmp_data);//n行目のデータを格納
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
		/*リストを新しく作ってそれを返している*/
		return(new_lists);
	}
	
	/*使った属性を削除する（残りの属性を新しいlistに登録してそのlistを返す）*/
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
		
		/*play,notplayの出現回数のHashMapを作る*/
		HashMap<String, Integer> p_or_np = new HashMap<String, Integer>();
		
		for(int i=0;i<dlist.size();i++) {//14周
			/*a_valueがdlistの中のcol番目の値と同じとき (例えば、a_valueがsunnyのとき、データの中のsunnyの行だけを見る)*/
			
			if(a_value.equals(dlist.get(i).get(col))) {/*dlistの中のArrayListの中のString*/
				if(p_or_np.containsKey( dlist.get(i).get(attrNames.size()-1)) )//dlistの最後の項目(play,notplay)について
					p_or_np.put(dlist.get(i).get(attrNames.size()-1) , p_or_np.get( dlist.get(i).get(attrNames.size()-1)) + 1 );
				else
					p_or_np.put(dlist.get(i).get(attrNames.size()-1) , 1);
				total++;//確率の分母
			}
		}
		
		/*新しく作ったHashMapで最後の計算*/
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
	
	double preInfo(int d_num, HashMap<String, Integer> c_dist) {//d_numは入力データの行数, c_distはaValueCountアレイリストのplay,notplayのハッシュマップ
		double info = 0.0;

		/*  complete this method */
		for(String x : c_dist.keySet()) //play, notplayの２周
			info -= ((double)c_dist.get(x)/(double)d_num) * log2((double)c_dist.get(x)/(double)d_num);

		return (info);		
	}
	
	/*多数グループを見つける*/
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
	
	/*各属性をkey(sunny,rain,hot,mildなど)とし、出現回数を中身とする*/
	ArrayList<HashMap<String, Integer>> attrValues(DataList dlist, AttrList alist){
		ArrayList<HashMap<String, Integer>> attrValueFreq = new ArrayList<HashMap<String, Integer>>();
		
		/*  complete this method */
		
		for(int i=0; i<alist.size(); i++) {//今ある属性の数だけfor文を回す(alistはmakeTreeを繰り返すたびに小さくなっていく)
			HashMap<String, Integer> a_v = new HashMap<String, Integer>();
			for(int j=0; j<dlist.size(); j++) {//データリストを1行ずつ見ていく(14周)
				String x = dlist.get(j).get( attrNames.indexOf(alist.get(i)) );//データリストを縦（属性ごと）で見ようとしている
				
				if(a_v.containsKey(x))
					a_v.put(x, a_v.get(x)+1);
				else
					a_v.put(x, 1);//引数 : (属性名(String), 頻度(int))
			}
			attrValueFreq.add(a_v);
		}
		
				
		return attrValueFreq;
		
	}
	
	/*木構造を作る*/
	TreeNode makeTree(DataList dlist, AttrList alist) {
		ArrayList<HashMap<String, Integer>> aValueCount = attrValues(dlist, alist);
		DataList [] d_lists;
		AttrList new_alist;
		String cls_name = null;
		double max_gain = 0.0;
		int max_id = -1;
		int col;
		
		/*属性が1つだけになったとき*/
		if(alist.size() == 1) {
			cls_name = findMajority(aValueCount.get(0));
			return new TreeNode(cls_name);
		}
		/*play,notplayのどちらか1つだけになった時*/
		if(aValueCount.get(alist.size()-1).size() == 1) {
			for(String key: aValueCount.get(alist.size()-1).keySet())
				cls_name = key;//labelにplay,notplayを代入（PrintNodeをするために）
			return new TreeNode(cls_name);
		}

		/*preInfoの計算*/
		double info0 = preInfo(dlist.size(), aValueCount.get(alist.size()-1));//第２引数　：　inputの属性の値の行の最後が中身のclassだから-1???????????
		System.out.printf("  preInfo: %.3f\n", info0);
		
		/*Information gainの計算 (貪欲探索のための値)*/
		for(int i = 0; i < alist.size() - 1; i++){
			double info = postInfo(dlist, attrNames.indexOf(alist.get(i)), aValueCount.get(i));//データリスト、属性の値、属性の値
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

		new_alist = deleteAttrib(alist, max_id);//使った属性を削除する

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
		
		/*level : 木構造の深さ*/
		/*label : 節の属性*/
		/*elabel : 辺の属性値*/
		/*get.Children.Num : 子の数*/
		/*get.Child : 子をTreeNode型で返す*/
		for(int i=0; i<node.getChildrenNum();i++) {
			
			if(level != 0)
				System.out.printf("|  ");
			
			System.out.printf("%s = %s",node.getLabel(),node.getChild(i).getElabel());
			if(node.getChild(i).getChildrenNum() == 0)//子がなければ再帰終了
				System.out.printf(": %s\n",node.getChild(i).getLabel());
			else {//子があれば再帰
				System.out.printf("\n");
				printNode(node.getChild(i), level+1);//レベルを1つ増やす
			}
		}
		
		
		return;
	}

	void printTree() {
		printNode(root, 0);
	}
	
	public static void main(String[] args) {
		SimpleDT dt = new SimpleDT(args[0]);
		
		/* データの読込み */
		dt.readFile();
		/* 決定木の構築 */
		dt.buildTree();
		/* 決定木の表表示 */
		dt.printTree();
	}

}
