//15816053
//田澤航樹


/*画像のサイズが同じものしか"連結&保存"できない*/
package p13;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class testMyImage5 extends JFrame{
	
	static int width;
	static int height;
	static int filenum = 0;
	static int mode = 0;// 1: ファイル　2: 左右反転　3: 上下反転　4: 連結&保存　5: クリア
	static BufferedImage parts1 = null;
	static BufferedImage parts2 = null;
	static BufferedImage output = null;
	
	public static void main(String args[]) {
		testMyImage5 root = new testMyImage5();//JFrame型
		Container contentPane = root.getContentPane();
		
		/*ボタン*/
		JButton filebtn = new JButton("ファイル");
		JButton fhbtn = new JButton("左右反転");
		JButton fvbtn = new JButton("上下反転");
		JButton savebtn = new JButton("連結&保存");
		JButton clearbtn = new JButton("クリア");
		/*ボタンのサイズ・フォント変更*/
		filebtn.setPreferredSize(new Dimension(200, 100));
		fhbtn.setPreferredSize(new Dimension(200, 100));
		fvbtn.setPreferredSize(new Dimension(200, 100));
		savebtn.setPreferredSize(new Dimension(200, 100));
		clearbtn.setPreferredSize(new Dimension(200, 100));
		filebtn.setFont(new Font("Default", Font.PLAIN, 30));
		fhbtn.setFont(new Font("Default", Font.PLAIN, 30));
		fvbtn.setFont(new Font("Default", Font.PLAIN, 30));
		savebtn.setFont(new Font("Default", Font.PLAIN, 30));
		clearbtn.setFont(new Font("Default", Font.PLAIN, 30));
		/*ボタンが押されたときの処理を追加*/
		/*"ファイル"*/
		filebtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(filenum < 2) {
	            	mode = 1;
	            	JFrame fileFrame = new JFrame();
	                JPanel filePanel = new JPanel();
	                JFileChooser fileChooser = new JFileChooser();
	                fileFrame.getContentPane().add(filePanel);
	                filePanel.add(fileChooser);
	                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	                int result = fileChooser.showOpenDialog(filePanel);
	                if (result != JFileChooser.APPROVE_OPTION) return;
	                File datafile = fileChooser.getSelectedFile();
	                readFile(datafile);
	                root.repaint();
            	}
            }
        });
		/*"左右反転"*/
		fhbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(filenum == 1) {
            		mode = 2;
            		root.repaint();
            	}
            }
		});
		/*"上下反転"*/
		fvbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(filenum == 1) {
            		mode = 3;
            		root.repaint();
            	}
            }
		});
		/*"連結&保存"*/
		savebtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(filenum == 2) {
            		mode = 4;
            		/*画像合成*/
            		/*くっつけた画像用に真っ黒な画像ベースを用意しておく*/
            		output = new BufferedImage(parts1.getWidth()*2, parts1.getHeight(), BufferedImage.TYPE_INT_ARGB);
            		/*真っ黒画像に 入力ファイル を2度描く。Y座標は縦に並ぶようにずらす。*/
            		Graphics g = output.getGraphics();
            		g.drawImage(parts1, 0, 0, null);
            		g.drawImage(parts2, width, 0, null);

            		/*合成後の画像をファイルに出力*/
            		try {
            			ImageIO.write(output, "png", new File("testMyImage5.png"));
            			System.out.println("合成成功 (testMyImage5.png)");
            		}catch(Exception ex) {
            			System.out.println("合成失敗" + ex);
            		}
            		root.repaint();
            	}
            }
		});
		/*"クリア"*/
		clearbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	mode = 5;
            	filenum = 0;
            	parts1 = null;
            	parts2 = null;
            	root.repaint();
            }
		});
		/*パネルにボタンを追加*/
		JPanel p = new JPanel();
		p.add(filebtn); p.add(fhbtn); p.add(fvbtn); p.add(savebtn); p.add(clearbtn);
	    contentPane.add(p, BorderLayout.SOUTH);
	    JPanel p2 = new JPanel(){
			public void paintComponent(Graphics g){
		    	super.paintComponents(g);
		    	if(mode == 1) {
		    		g.drawImage(parts1,0,0,null);
		    		if(filenum == 2) g.drawImage(parts2,width,0,null);
		    	}
		    	if(mode == 2) {
		    		g.drawImage(parts1,0,0,width,height,null);
		        	g.drawImage(parts1,width,0,width*2,height, width,0,0,height,null);//左右反転
		    	}
		    	if(mode == 3) {
		    		g.drawImage(parts1,0,0,width,height,null);
		        	g.drawImage(parts1,0,height,width,height*2, 0,height,width,0,null);//上下反転
		    	}
		    	if(mode == 4) {
		    		g.drawImage(output, 0, 0, null);
		    	}
		    	mode = 0;
		    }
		};
		contentPane.add(p2, BorderLayout.CENTER);
		
		/*注意書きのテキスト*/
		JLabel l3 = new JLabel("画像は2枚まで選択可能。"+"(同じサイズの画像しか対応しません。)");
		l3.setFont(new Font("Default", Font.PLAIN, 30));
		contentPane.add(l3,BorderLayout.NORTH);
	    
		
	    /*
	     *  ↓ プログラムイメージ ↓
	     *  
	     *                (上)
	     *      ---JPanel*2 + JLabel---          (p(ボタン)SOUTH と  p2(画像)CENTER と l3(テキスト)NORTH)
	     *   ----------Container---------        (contentPane)
	     * -------------JFrame-------------      (root)
	     *                (下)
	     */
	    
		
		
	    /*フレームの設定*/
        root.setSize(1600, 1600);
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setVisible(true);
	}
	
	

	/*fileChooserから画像を読み込む処理*/
	static void readFile(File datafile) {
		filenum++;
		if(filenum == 1) {
			try {
				parts1 = ImageIO.read(datafile);
			}
			catch(IOException e) {
				System.out.println("ファイル読み込みエラー" + e);
			}
			width = parts1.getWidth();
			height = parts1.getHeight();
			System.out.printf("幅　%d 高さ　%d ファイル数 %d\n",width,height,filenum);
		}
		if(filenum == 2) {
			try {
				parts2 = ImageIO.read(datafile);
			}
			catch(IOException e) {
				System.out.println("ファイル読み込みエラー" + e);
			}
			if(width != parts2.getWidth() || height != parts2.getHeight()) {
				System.out.println("画像サイズが合っていません。");
				System.exit(1);
			}
			System.out.printf("幅　%d 高さ　%d ファイル数 %d\n",width,height,filenum);
		}
	}
}
