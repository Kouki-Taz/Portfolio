//15816053
//�c�V�q��


/*�摜�̃T�C�Y���������̂���"�A��&�ۑ�"�ł��Ȃ�*/
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
	static int mode = 0;// 1: �t�@�C���@2: ���E���]�@3: �㉺���]�@4: �A��&�ۑ��@5: �N���A
	static BufferedImage parts1 = null;
	static BufferedImage parts2 = null;
	static BufferedImage output = null;
	
	public static void main(String args[]) {
		testMyImage5 root = new testMyImage5();//JFrame�^
		Container contentPane = root.getContentPane();
		
		/*�{�^��*/
		JButton filebtn = new JButton("�t�@�C��");
		JButton fhbtn = new JButton("���E���]");
		JButton fvbtn = new JButton("�㉺���]");
		JButton savebtn = new JButton("�A��&�ۑ�");
		JButton clearbtn = new JButton("�N���A");
		/*�{�^���̃T�C�Y�E�t�H���g�ύX*/
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
		/*�{�^���������ꂽ�Ƃ��̏�����ǉ�*/
		/*"�t�@�C��"*/
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
		/*"���E���]"*/
		fhbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(filenum == 1) {
            		mode = 2;
            		root.repaint();
            	}
            }
		});
		/*"�㉺���]"*/
		fvbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(filenum == 1) {
            		mode = 3;
            		root.repaint();
            	}
            }
		});
		/*"�A��&�ۑ�"*/
		savebtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(filenum == 2) {
            		mode = 4;
            		/*�摜����*/
            		/*���������摜�p�ɐ^�����ȉ摜�x�[�X��p�ӂ��Ă���*/
            		output = new BufferedImage(parts1.getWidth()*2, parts1.getHeight(), BufferedImage.TYPE_INT_ARGB);
            		/*�^�����摜�� ���̓t�@�C�� ��2�x�`���BY���W�͏c�ɕ��Ԃ悤�ɂ��炷�B*/
            		Graphics g = output.getGraphics();
            		g.drawImage(parts1, 0, 0, null);
            		g.drawImage(parts2, width, 0, null);

            		/*������̉摜���t�@�C���ɏo��*/
            		try {
            			ImageIO.write(output, "png", new File("testMyImage5.png"));
            			System.out.println("�������� (testMyImage5.png)");
            		}catch(Exception ex) {
            			System.out.println("�������s" + ex);
            		}
            		root.repaint();
            	}
            }
		});
		/*"�N���A"*/
		clearbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	mode = 5;
            	filenum = 0;
            	parts1 = null;
            	parts2 = null;
            	root.repaint();
            }
		});
		/*�p�l���Ƀ{�^����ǉ�*/
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
		        	g.drawImage(parts1,width,0,width*2,height, width,0,0,height,null);//���E���]
		    	}
		    	if(mode == 3) {
		    		g.drawImage(parts1,0,0,width,height,null);
		        	g.drawImage(parts1,0,height,width,height*2, 0,height,width,0,null);//�㉺���]
		    	}
		    	if(mode == 4) {
		    		g.drawImage(output, 0, 0, null);
		    	}
		    	mode = 0;
		    }
		};
		contentPane.add(p2, BorderLayout.CENTER);
		
		/*���ӏ����̃e�L�X�g*/
		JLabel l3 = new JLabel("�摜��2���܂őI���\�B"+"(�����T�C�Y�̉摜�����Ή����܂���B)");
		l3.setFont(new Font("Default", Font.PLAIN, 30));
		contentPane.add(l3,BorderLayout.NORTH);
	    
		
	    /*
	     *  �� �v���O�����C���[�W ��
	     *  
	     *                (��)
	     *      ---JPanel*2 + JLabel---          (p(�{�^��)SOUTH ��  p2(�摜)CENTER �� l3(�e�L�X�g)NORTH)
	     *   ----------Container---------        (contentPane)
	     * -------------JFrame-------------      (root)
	     *                (��)
	     */
	    
		
		
	    /*�t���[���̐ݒ�*/
        root.setSize(1600, 1600);
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setVisible(true);
	}
	
	

	/*fileChooser����摜��ǂݍ��ޏ���*/
	static void readFile(File datafile) {
		filenum++;
		if(filenum == 1) {
			try {
				parts1 = ImageIO.read(datafile);
			}
			catch(IOException e) {
				System.out.println("�t�@�C���ǂݍ��݃G���[" + e);
			}
			width = parts1.getWidth();
			height = parts1.getHeight();
			System.out.printf("���@%d �����@%d �t�@�C���� %d\n",width,height,filenum);
		}
		if(filenum == 2) {
			try {
				parts2 = ImageIO.read(datafile);
			}
			catch(IOException e) {
				System.out.println("�t�@�C���ǂݍ��݃G���[" + e);
			}
			if(width != parts2.getWidth() || height != parts2.getHeight()) {
				System.out.println("�摜�T�C�Y�������Ă��܂���B");
				System.exit(1);
			}
			System.out.printf("���@%d �����@%d �t�@�C���� %d\n",width,height,filenum);
		}
	}
}
