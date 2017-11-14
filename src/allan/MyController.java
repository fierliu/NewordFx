package allan;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;

import org.jdom2.JDOMException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MyController implements Initializable{
	WordHandler wh = new WordHandler();
	ReadDailyNum rdn = new ReadDailyNum();
	private int dailyNum = rdn.readDailyNum();
	final FileChooser fileChooser = new FileChooser();
	int count = 1;
	@FXML
	private Button btnCh, btnNext, btnMemorized;
	@FXML
	private Label lbCh, lbEn, lbHide, lbThisCount, lbTotalCount, lbLeftCount;
	@FXML
	private MenuItem importTxt, importHtml, aboutImport;
	private Stage stage;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	public void setStage(Stage stage){
		this.stage = stage;
	}
	public void btnChHandler(ActionEvent event){
		if(count==1){}
		else{
			lbCh.setText(wh.showExplain(getHideNum()));
			System.out.println(wh.showExplain(getHideNum()));
		}
	}
	public void btnNextHandler(ActionEvent event){
		lbThisCount.setText("本次学习："+dailyNum);
		lbTotalCount.setText("总词数剩余："+wh.getTotalSize());
		int i = wh.makeRandom();
		if(count> dailyNum ){
			showInfo("本次学习已结束");
			try {
				wh.writefile();
			} catch (UnsupportedEncodingException | FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		if(wh.getWritelistSize()<dailyNum){
			i = wh.makeLowRandom();
		}
		else{
			showNextWord(i);
				count += 1;
		}
	}
	public void btnMemorizedHandler(ActionEvent event){
		if(count==1){}
		else if(count> dailyNum ){
//			弹出学习完毕窗口
			showInfo("本次学习已结束");
		}
		else{
			wh.delword(getHideNum());
			lbTotalCount.setText("总词数剩余："+wh.getTotalSize());
			int i = wh.makeRandom();
			showNextWord(i);
			count += 1;
		}
	}
	public void importTxtHandler(ActionEvent event){
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openFile(file);
        }
	}
	public void importHtmlHandler(ActionEvent event){
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openFile(file);
        }
	}
	public void aboutImportHandler(ActionEvent event) throws IOException{
		Stage aboutIS = new Stage();
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/allan/AboutImpScene.fxml"));
		Parent root = fxmlloader.load();
		aboutIS.setScene(new Scene(root));
		aboutIS.setResizable(false);

		aboutIS.show();
	}

	public void showNextWord(int i){
		lbLeftCount.setText("已学习："+count);
		lbEn.setText(wh.showWord(i));
		lbHide.setText(String.valueOf(i));//将随机索引值记录在hide标签里
		lbCh.setText(null);
		if (wh.getWritelistSize() == 0){
			showInfo("本词库学习已结束，请导入新词库");
		}
	}
	public void showInfo(String content){
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("INFO");
		alert.setHeaderText("");
		alert.setContentText(content);
		alert.initOwner(null);
		alert.show();
	}
	public int getHideNum() throws NumberFormatException{
	    int index = Integer.parseInt(lbHide.getText());
		return index;
	}
	private void openFile(File file) {
        EventQueue.invokeLater(() -> {
            //                desktop.open(file);
            String path = file.getPath();
//            System.out.println(path);
            ParseHTML ph = new ParseHTML();
            try {
            	ph.Parse(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
}
