package controller;

import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import org.jdom2.JDOMException;

import dao.PropertyDao;
import dao.WordDao;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Property;
import model.Word;
import tool.GetAmount;
import tool.GetVocList;
import tool.ParseHTML;
import tool.ParseTXT;
import tool.ParseXml;

public class MyController implements Initializable{
	WordDao wd = new WordDao();
	PropertyDao pd = new PropertyDao();
	final FileChooser fileChooser = new FileChooser();
	int count = 1;
	@FXML
	private Button btnCh, btnKnown, btnMemorized, btnStart, btnUnknow;
	@FXML
	private Label lbCh, lbEn, lbHide, lbQuota, lbTotalCount, lbLearned, lbVocName, lbUser,
		lbId, lbAmount, lbTodayAmount, lbWordId, lbWordKnow;
	@FXML
	private MenuItem importTxt1, importTxt2, importHtml, aboutImport, setQuota, chooseVoc;
	private Stage stage;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//读取配置文件
		PropertyDao propDao = new PropertyDao();
		try {
			Property prop = propDao.getProperty("1");
			String quota = prop.getQuota();
			String vocName = prop.getVocName();
			String name = prop.getName();
			String id = prop.getId();
			String total = prop.getTotal();
			lbVocName.setText(vocName);
			lbQuota.setText(quota);
			lbUser.setText(name);
			lbId.setText(id);
			lbAmount.setText(total);
			lbLearned.setText("0");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setStage(Stage stage){
		this.stage = stage;
	}
//	“释意”按键监听
	public void btnChHandler(ActionEvent event) throws JDOMException, IOException{
		Word word = wd.getWordByEn("TempVoc.xml", lbEn.getText());
		lbCh.setText(word.getCh());
	}
//	“认识”按键监听
	public void btnKnownHandler(ActionEvent event) throws JDOMException, IOException{
		Integer count = Integer.valueOf(lbLearned.getText())+1;
		lbLearned.setText(count.toString());//设置已学习量
		Integer todayAmount = Integer.valueOf(lbTodayAmount.getText());
		if(count.equals(todayAmount)){//当学习完时
			showInfo("本次学习结束");
			lbEn.setText("");
			lbCh.setText("");
		}else{
	//        临时词库中know元素+1,时间调整
	        Integer know = Integer.valueOf(lbWordKnow.getText());
	        if(know.equals(0)){//认识1次
	        	wd.updateWordDate_Know("1", "1", lbWordId.getText());
	        }else if(know.equals(1)){//认识2次
	        	wd.updateWordDate_Know("2", "2", lbWordId.getText());
	        }else if(know.equals(2)){//认识3次
	        	wd.deleteWord("Tempvoc.xml", lbWordId.getText());
	        }
	        showNextWord();
		}

	}
//	“掌握”按键监听
	public void btnMemorizedHandler(ActionEvent event) throws JDOMException, IOException{
		Integer count = Integer.valueOf(lbLearned.getText())+1;
		lbLearned.setText(count.toString());//设置已学习量
		Integer todayAmount = Integer.valueOf(lbTodayAmount.getText());
		if(count.equals(todayAmount)){//当学习完时
			showInfo("本次学习结束");
			lbEn.setText("");
		}else{
	//        临时词库中删除元素
			wd.deleteWord("Tempvoc.xml", lbWordId.getText());
		}
		showNextWord();
	}
//	“不认识”按键监听
	public void btnUnknowHandler(ActionEvent event) throws JDOMException, IOException{
		Integer count = Integer.valueOf(lbLearned.getText())+1;
		lbLearned.setText(count.toString());//设置已学习量
		Integer todayAmount = Integer.valueOf(lbTodayAmount.getText());
		if(count.equals(todayAmount)){//当学习完时
			showInfo("本次学习结束");
			lbEn.setText("");
		}else{
//			Calendar cal = Calendar.getInstance();
	//        临时词库中know元素、时间调整
//			System.out.println("lbWordKnow.getText():"+lbWordKnow.getText());
	        Integer know = Integer.valueOf(lbWordKnow.getText());
	        if(know.equals(0)){//know=0时不认识
	        	wd.updateWordDate_Know("1", "0", lbWordId.getText());
	        }else if(know.equals(1)) {//know=1时不认识
	        	wd.updateWordDate_Know("1", "0", lbWordId.getText());
	        }else if(know.equals(2)){
	        	wd.updateWordDate_Know("2", "1", lbWordId.getText());
	        }
	        showNextWord();
		}
	}
//	导入txt上下行格式词库
	public void importTxt2Handler(ActionEvent event){
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openTxtTwoLineFile(file);
        }
	}

	public void importHtmlHandler(ActionEvent event){
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openHtmlFile(file);
        }
	}
	public void aboutImportHandler(ActionEvent event) throws IOException{
		Stage aboutIS = new Stage();
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/AboutImpScene.fxml"));
		Parent root = fxmlloader.load();
		aboutIS.setScene(new Scene(root));
		aboutIS.setResizable(false);
		aboutIS.show();
	}
	public void setQuotaHandler(ActionEvent event){
		//显示学习量选择框，将选择的数值保存到Property.xml中
		showSetQuoraDialog();
	}
	public void setVocHandler(ActionEvent event) throws IOException{
		Stage setVoc = new Stage();
		VBox root = new VBox();
		Label lbVocList = new Label("词汇列表");
		Label lbSelectedVoc = new Label();
		Button btnOk = new Button("确定");
		btnOk.setOnAction(e -> {
			String vocName = lbSelectedVoc.getText();
			//获取词库词量
			Integer amount = null;
			try {
				amount = GetAmount.getAmount("VocaList/"+vocName, "word");
				//将词库名称和词量显示在主界面
				lbVocName.setText(vocName.substring(0, (vocName.length()-4)));//去掉.xml后缀
				lbAmount.setText(amount.toString());
				//将词库名称和词量保存到Property中
				saveVocName_Amount();
				pd.setDay("0");//将Property中的day设置为0
			} catch (JDOMException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        setVoc.close();
	        //创建临时词库
	        ParseXml ph = new ParseXml();
	        Word word = null;
	        Set<Integer> set = new LinkedHashSet<>();//存放随机数，避免重复
	        Random rd = new Random();
	        try {
	        	Integer i;
	        	for(Integer j= 1; j<amount+1; j++){
	        		do{
	    				i = rd.nextInt(amount)+1;
	    			  }while(set.contains(i));
	        		set.add(i);
//	        		System.out.println("i:"+i);
	        		word = wd.getWordById(vocName, i.toString());//取出Word对象
	        		ph.creatTempXml(word, j.toString());//写入Word对象到临时词库
	    		}
			} catch (JDOMException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	    });
		List<File> files = GetVocList.getVocList();
		ObservableList<String> vocs = FXCollections.observableArrayList();
		for(File file: files){
			String _fileName = file.toString().substring(file.toString().lastIndexOf("\\")+1);//截取词库文件名
//			System.out.println(_fileName);
			vocs.add(_fileName);
		}
		ListView<String> vocList = new ListView<String>(vocs);//添加到TreeView中
		MultipleSelectionModel<String> sm = vocList.getSelectionModel();//获取选择模型
		sm.selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> changed,
							String oldVal, String newVal){
				lbSelectedVoc.setText(newVal);
			}
		});
		root.getChildren().addAll(lbVocList, vocList, lbSelectedVoc,btnOk);
		setVoc.setScene(new Scene(root));
		setVoc.show();
	}
//	"开始"按键监听
	public void btnStartHandler(ActionEvent event) throws JDOMException, IOException{
		if((new File("TempVoc.xml")).exists()){//先判断是否已生成临时词库
//			将所有date>0的词的date-1
			wd.updateDate();

//			获取Property中的day,将其+1
			Property pro = null;
			try {
				pro = pd.getProperty("1");
			} catch (JDOMException | IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Integer day = Integer.valueOf(pro.getDay());
			Integer _day = day+1;
//			将+1后的day保存到Property中
			pd.setDay(_day.toString());
			System.out.println("day in Property:"+day);
			/**********************设置临时词库date****************************/
			String vocName = lbVocName.getText();
			Integer amount = GetAmount.getAmount("VocaList/"+vocName+".xml", "word");
	        int quota = Integer.parseInt(lbQuota.getText());//每天学习量
	        int days = amount/quota;//新词被分成的份数-1,也是新词总共学习的天数-1
	        try {
	        	if(day<days){
//	        		将本次要学的quota个新词的date设置为0
		        	Integer id;
		        	for(id= day*quota; id<day*quota+quota+1; id=id+1){
		        		wd.updateWordDate("0", id.toString());//将本次要学的quota个新词的date设置为0
		    		}
	        	}else if(day.equals(days)){
	        		//当背到最后一天时，剩余的词汇可能不足一天的quota量，特殊处理
		        	for(Integer j= days*quota; j<amount+1; j++){
		        		wd.updateWordDate("0", j.toString());
		        	}
	        	}else{
	        		//do nothing
	        	}

			} catch (JDOMException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			设置今天要学习的量
			Integer todayAmount = wd.getTodayAmount("TempVoc.xml", "0");
			lbTodayAmount.setText(todayAmount.toString());
//			显示单词信息
	        showNextWord();

		}else{
			showInfo("请先选择词库！");
		}
	}

//	--------------------------------------------

	public void showNextWord() throws JDOMException, IOException{

    	//		显示单词信息
        Word word = wd.getWordByDate("TempVoc.xml", "0");
        lbWordId.setText(word.getId());
        lbWordKnow.setText(word.getKnow());
        lbEn.setText(word.getEn());
//        System.out.println("word.getEn:"+word.getEn());
//        lbCh清空
        lbCh.setText(null);
	}
	public void showInfo(String content){
		Platform.runLater(() ->{
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("INFO");
			alert.setHeaderText("");
			alert.setContentText(content);
			alert.initOwner(null);
			alert.show();

			}
		);

	}

	private void openHtmlFile(File file) {
        EventQueue.invokeLater(() -> {
            String path = file.getPath();
//            System.out.println("原始html文档路径："+path);
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

	private void openTxtTwoLineFile(File file) {
        EventQueue.invokeLater(() -> {
            String path = file.getPath();
            ParseTXT pt = new ParseTXT();
            try {
            	pt.parseTxt(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
	public void showSetQuoraDialog() {
	    Stage popup = new Stage();
	    popup.setAlwaysOnTop(true);
	    popup.setResizable(false);
	    popup.initModality(Modality.APPLICATION_MODAL);
//	    popup.getIcons().add(new Image("/application/eye.png"));

	    Label lb = new Label("输入每日学习量：");
	    Label lbNote = new Label("(输入小于词库词汇总量的数字)");
	    TextField tf = new TextField();
	    tf.setPrefColumnCount(4);
	    Button closeBtn = new Button("确定");
	    closeBtn.setOnAction(e -> {
//	    	将入户输入的数字保存到Property.xml中
	    	Property ppt = new Property();
	    	ppt.setQuota(tf.getText());
	    	ppt.setId(lbId.getText());
	    	ppt.setVocName(lbVocName.getText());
	    	ppt.setTotal(lbAmount.getText());
	    	ppt.setDay("0");
	    	PropertyDao ppd = new PropertyDao();
	    	try {
				ppd.setProperty(ppt);
			} catch (JDOMException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	lbQuota.setText(tf.getText());
	        popup.close();
	    });
	    VBox root = new VBox();
	    root.setPadding(new Insets(10));
	    root.setAlignment(Pos.CENTER);//显示位置
	    root.setSpacing(10);
	    root.getChildren().addAll(lb, lbNote, tf, closeBtn);

	    Scene scene = new Scene(root);
	    popup.setScene(scene);
	    popup.setTitle("设置学习量");
	    popup.setWidth(180);
	    popup.setHeight(130);
	    popup.show();

	    Thread thread = new Thread(() -> {
	        try {
	            Thread.sleep(1000000);
	            if (popup.isShowing()) {
	                Platform.runLater(() -> popup.close());
	            }
	        } catch (Exception exp) {
	            exp.printStackTrace();
	        }
	    });
	    thread.setDaemon(true);
	    thread.start();
	}

	public void saveVocName_Amount(){
		Property ppt = new Property();
    	ppt.setTotal(lbAmount.getText());
    	ppt.setQuota("50");//默认学习量50
    	ppt.setId(lbId.getText());
    	ppt.setVocName(lbVocName.getText());
    	PropertyDao ppd = new PropertyDao();
    	try {
			ppd.setProperty(ppt);
		} catch (JDOMException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


}
