package javaFX_otamesi;

import java.io.File;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class imageotamesi extends Application{
	
	final double cSize = 0.5;//�J�[�h��\������T�C�Y�{��
	
	Canvas canvas;
	GraphicsContext gc;
	File[] files;
	Image image[];//���P�̃J�[�h�摜
	int hyouzi=0;
	
	public static void main(String[] args) {
        launch(args);
    }
	public void start(Stage stage) throws Exception {
		final String title = "�f�[�^�x�[�X-";
		final String cardimgpath = "image/cardimage";//�J�[�h�摜��ǂݍ��ރt�H���_��
		final String cardname = "genntotu%d.jpg";//�J�[�h�摜�̖��O
		final String ypath1 = "image/icon/yazirusi2.png";//���{�^��(��)�̉摜�̃p�X
		final String ypath2 = "image/icon/yazirusi.png";//���{�^��(�E)�̉摜�̃p�X
		final String haikei = "image/background/haikei.jpg";//�w�i�摜�̃p�X
		final String btniconpath1 = "image/icon/buttoncut1.png";//�{�^��(��)�̃p�X
		final String btniconpath2 = "image/icon/buttoncut3.png";//�{�^��(��)�̃p�X
		//final String;
		
		
		/*Stage���*/
		stage.setTitle(title);
		stage.centerOnScreen();
		
		/*�摜�t�H���_���ǂݍ���*/
		File dir = new File(cardimgpath);
		File[] files = dir.listFiles();
		/*�摜*/
		canvas = new Canvas();
		image = new Image[4];
		for(int i=0;i<files.length;i++) {
			String fname = String.format(cardimgpath+"/"+cardname, i+1);
			image[i] = new Image(Paths.get(fname).toUri().toString());
		}
		canvas.setWidth(image[0].getWidth()*cSize);
		canvas.setHeight(image[0].getHeight()*cSize);
		gc = canvas.getGraphicsContext2D();
		gc.drawImage(image[0], 0, 0, 640*cSize, 800*cSize);
		
		
		
		HBox hbox_btn = new HBox(20d);
		hbox_btn.setAlignment(Pos.CENTER);
		/*���{�^��(��)*/
		Image btnicon3 = new Image(Paths.get(ypath1).toUri().toString());
		ImageView yazirusi = new ImageView(btnicon3);
		Button y1 = new Button();
		y1.setGraphic(yazirusi);
		y1.setOnAction((ActionEvent)->{
			/*���{�^���̏���*/
			if(hyouzi == 0) hyouzi = files.length-1;
			else hyouzi--;
			gc.drawImage(image[hyouzi], 0, 0, 640*cSize, 800*cSize);
		});
		hbox_btn.getChildren().add(y1);
		/*�{�^��*/
		Image btnicon1 = new Image(Paths.get(btniconpath1).toUri().toString());
		Image btnicon2 = new Image(Paths.get(btniconpath2).toUri().toString());
		ImageView imageView;
		ImageView imageView2;
		for(int i = 0;i<files.length;i++) {
			imageView = new ImageView(btnicon1);//�ʏ펞�p
			imageView2 = new ImageView(btnicon2);//�N���b�N���ꂽ�Ƃ��p
			Button b = new Button();
			b.setGraphic(imageView);//�ʏ펞���Z�b�g
			int n=i;
			b.setOnAction((ActionEvent)-> {//�{�^���������ꂽ�Ƃ��̏���
				hyouzi = n;
				gc.drawImage(image[hyouzi], 0, 0, 640*cSize, 800*cSize);
				//b.setGraphic(imageView2);
			});
			hbox_btn.getChildren().add(b);
		}
		/*���{�^��(�E)*/
		Image btnicon4 = new Image(Paths.get(ypath2).toUri().toString());
		ImageView yazirusi2 = new ImageView(btnicon4);
		Button y2 = new Button();
		y2.setGraphic(yazirusi2);
		y2.setOnAction((ActionEvent)->{
			/*���{�^���̏���*/
			if(hyouzi == files.length-1) hyouzi = 0;
			else hyouzi++;
			gc.drawImage(image[hyouzi], 0, 0, 640*cSize, 800*cSize);
		});
		hbox_btn.getChildren().add(y2);
		
		
		
		/*�w�i*/
		StackPane root = new StackPane();
		String root_bg = new File(haikei).toURI().toString();//�w�i�摜�̃p�X
		root.setStyle(
			    "-fx-background-image: url('" + root_bg + "'); " +
			    	    "-fx-background-position: center center; " +
			    	    "-fx-background-repeat: stretch;"
			    	);
		
		
		
		/*��ʍ�����*/
		VBox vbox = new VBox(20d);
		vbox.setAlignment(Pos.CENTER);
		//vbox.setPrefWidth(100);
		vbox.getChildren().addAll(canvas,hbox_btn);
		
		/*��ʉE����*/
		VBox vbox2 = new VBox();
		
		/*��ʂ��c�ɂQ������������HBox*/
		HBox hbox = new HBox();
		hbox.getChildren().addAll(vbox,vbox2);
		
		root.getChildren().add(hbox);
		
		stage.setScene(new Scene(root,960,750));//��v���̉�ʃT�C�Y
		stage.show();
	}
	
	
}
