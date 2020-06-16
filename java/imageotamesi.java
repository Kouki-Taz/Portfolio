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
	
	final double cSize = 0.5;//カードを表示するサイズ倍率
	
	Canvas canvas;
	GraphicsContext gc;
	File[] files;
	Image image[];//将姫のカード画像
	int hyouzi=0;
	
	public static void main(String[] args) {
        launch(args);
    }
	public void start(Stage stage) throws Exception {
		final String title = "データベース-";
		final String cardimgpath = "image/cardimage";//カード画像を読み込むフォルダ名
		final String cardname = "genntotu%d.jpg";//カード画像の名前
		final String ypath1 = "image/icon/yazirusi2.png";//矢印ボタン(左)の画像のパス
		final String ypath2 = "image/icon/yazirusi.png";//矢印ボタン(右)の画像のパス
		final String haikei = "image/background/haikei.jpg";//背景画像のパス
		final String btniconpath1 = "image/icon/buttoncut1.png";//ボタン(青)のパス
		final String btniconpath2 = "image/icon/buttoncut3.png";//ボタン(赤)のパス
		//final String;
		
		
		/*Stage情報*/
		stage.setTitle(title);
		stage.centerOnScreen();
		
		/*画像フォルダ情報読み込み*/
		File dir = new File(cardimgpath);
		File[] files = dir.listFiles();
		/*画像*/
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
		/*矢印ボタン(左)*/
		Image btnicon3 = new Image(Paths.get(ypath1).toUri().toString());
		ImageView yazirusi = new ImageView(btnicon3);
		Button y1 = new Button();
		y1.setGraphic(yazirusi);
		y1.setOnAction((ActionEvent)->{
			/*矢印ボタンの処理*/
			if(hyouzi == 0) hyouzi = files.length-1;
			else hyouzi--;
			gc.drawImage(image[hyouzi], 0, 0, 640*cSize, 800*cSize);
		});
		hbox_btn.getChildren().add(y1);
		/*ボタン*/
		Image btnicon1 = new Image(Paths.get(btniconpath1).toUri().toString());
		Image btnicon2 = new Image(Paths.get(btniconpath2).toUri().toString());
		ImageView imageView;
		ImageView imageView2;
		for(int i = 0;i<files.length;i++) {
			imageView = new ImageView(btnicon1);//通常時用
			imageView2 = new ImageView(btnicon2);//クリックされたとき用
			Button b = new Button();
			b.setGraphic(imageView);//通常時をセット
			int n=i;
			b.setOnAction((ActionEvent)-> {//ボタンが押されたときの処理
				hyouzi = n;
				gc.drawImage(image[hyouzi], 0, 0, 640*cSize, 800*cSize);
				//b.setGraphic(imageView2);
			});
			hbox_btn.getChildren().add(b);
		}
		/*矢印ボタン(右)*/
		Image btnicon4 = new Image(Paths.get(ypath2).toUri().toString());
		ImageView yazirusi2 = new ImageView(btnicon4);
		Button y2 = new Button();
		y2.setGraphic(yazirusi2);
		y2.setOnAction((ActionEvent)->{
			/*矢印ボタンの処理*/
			if(hyouzi == files.length-1) hyouzi = 0;
			else hyouzi++;
			gc.drawImage(image[hyouzi], 0, 0, 640*cSize, 800*cSize);
		});
		hbox_btn.getChildren().add(y2);
		
		
		
		/*背景*/
		StackPane root = new StackPane();
		String root_bg = new File(haikei).toURI().toString();//背景画像のパス
		root.setStyle(
			    "-fx-background-image: url('" + root_bg + "'); " +
			    	    "-fx-background-position: center center; " +
			    	    "-fx-background-repeat: stretch;"
			    	);
		
		
		
		/*画面左半分*/
		VBox vbox = new VBox(20d);
		vbox.setAlignment(Pos.CENTER);
		//vbox.setPrefWidth(100);
		vbox.getChildren().addAll(canvas,hbox_btn);
		
		/*画面右半分*/
		VBox vbox2 = new VBox();
		
		/*画面を縦に２分したいからHBox*/
		HBox hbox = new HBox();
		hbox.getChildren().addAll(vbox,vbox2);
		
		root.getChildren().add(hbox);
		
		stage.setScene(new Scene(root,960,750));//戦プリの画面サイズ
		stage.show();
	}
	
	
}
