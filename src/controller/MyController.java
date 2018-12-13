package controller;

import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.*;

import dao.*;
import model.*;
import org.jdom2.JDOMException;

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
import tool.GetAmount;
import tool.GetVocList;
import tool.ParseHTML;
import tool.ParseTXT;
import tool.ParseXml;
/*
* todo 可以把业务逻辑放到另外一个类中
* */
public class MyController implements Initializable{
//	PropertyDao pd = new PropertyDao();
	WordDaoImpl wordDao;
	UserDao userDao;
	User user;
	UserWord word;
	final FileChooser fileChooser = new FileChooser();
	int count = 1;
//	HashSet<Integer> reviewSet;
	HashSet<Integer> newSet;
	@FXML
	private Button btnCh, btnKnown, btnMemorized, btnStart, btnNext;
	@FXML
	private Label lbCh, lbEn, lbHide, lbQuota, lbTotalCount,  lbVocName, lbUser, lbWordKnow,
			lbUid, lbAmount/*词库总词量*/,
			lbTodayAmount, lbLearned, lbReviewAmount, lbReviewed;
	@FXML
	private MenuItem importTxt1, importTxt2, importHtml, aboutImport, setQuota, chooseVoc;
	private Stage stage;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//初始化对象
		wordDao = new WordDaoImpl();
		userDao = new UserDaoImpl();
		user = userDao.load("1");//在实现登录功能之前，默认使用uid = 1的帐户
		//设置主界面初始值
		lbVocName.setText(user.getVocName().split("_")[0]);
		lbUser.setText(user.getUsername());
		lbUid.setText(user.getUid());
		lbAmount.setText(user.getTotal());
	}
	public void setStage(Stage stage){
		this.stage = stage;
	}
//	“释意”按键监听
	public void btnChHandler(ActionEvent event){
		lbCh.setText(word.getCh());
	}
//	“认识”按键监听
	public void btnKnownHandler(ActionEvent event){
        String vocName = user.getVocName();
	    //处理上一个词
		Integer times = word.getTimes();
        Integer conquer = word.getConquer();
        if(times == 1) {
        	conquer += 3;
        	word.setTimes(3);
        	wordDao.update(word,vocName);
        	if(newSet.contains(word.getId()))
        		lbLearned.setText((Integer.valueOf(lbLearned.getText())+ 1)+"");
        	else lbReviewed.setText((Integer.valueOf(lbReviewed.getText())+1)+ "");
		}
        else times -= 1;
        word.setTimes(times);
        word.setConquer(conquer);
        wordDao.update(word,vocName);
        //显示下一个词
        showNextWord(vocName);
	}
//	“掌握”按键监听
	public void btnMemorizedHandler(ActionEvent event){
		String vocName = user.getVocName();
		wordDao.delete(word.getId(),vocName);
		VocDaoImpl vocDao = new VocDaoImpl();

        if(newSet.contains(word.getId()))
            lbLearned.setText((Integer.valueOf(lbLearned.getText())+ 1)+"");
        else lbReviewed.setText((Integer.valueOf(lbReviewed.getText())+1)+ "");

		if(vocDao.getVocSize(vocName) == 0){
			//删除词库，提示更换词库
			vocDao.delete(vocName);
			showInfo(vocName.split("_")[0] +" 已学完，请更换词库");
		}else showNextWord(vocName);
	}
//	“不认识”按键监听
	public void btnUnknowHandler(ActionEvent event){
		String vocName = user.getVocName();
		//处理上一个词
		Integer times = word.getTimes();
		Integer conquer = word.getConquer();
		if(times < 3) times += 1;
		word.setTimes(times);
		wordDao.update(word,vocName);
		//显示下一个词
		showNextWord(vocName);
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

	//设置使用的词库
	public void setVocHandler(ActionEvent event){
		Stage setVoc = new Stage();
		VBox root = new VBox();
		Label lbVocList = new Label("词汇列表");
		Label lbSelectedVoc = new Label();
		Button btnOk = new Button("确定");
		String oldVocName = lbVocName.getText();
		btnOk.setOnAction(e -> {
			String vocName = lbSelectedVoc.getText();//选择的词库名
			VocNameDaoImpl vocNameDao = new VocNameDaoImpl();
			VocName vocNameObj = vocNameDao.load(vocName);
			//----------
			UserDao userDao = new UserDaoImpl();
			User user = userDao.load(lbUid.getText());
			String userVocName = vocName + "_"+ user.getUsername();
//            System.out.println("userVocName = " + userVocName);
			VocDao vocDao = new VocDaoImpl();
			//旧词库不存在的情况
			if(vocNameDao.ifVocNameExists(userVocName)== 0) vocDao.create(userVocName, vocName);
			//将用户词库名称和词库词量（用于初始化时在主界面上显示词量）添加到user表中
			user.setVocName(userVocName);
			user.setTotal(vocNameObj.getWordAmount());
			userDao.mod(user);
			//将词库名称和词量显示在主界面
			lbVocName.setText(vocName);
			lbAmount.setText(vocNameObj.getWordAmount());
			setVoc.close();
		});
		//在lbSelectedVoc上显示词库列表
		VocNameDao vocNameDao = new VocNameDaoImpl();
		List<VocName> vocnames = vocNameDao.findAll();
		ObservableList<String> vocs = FXCollections.observableArrayList();
		for (VocName vocname : vocnames) {
			vocs.add(vocname.getName()/*+ "  词量："+ vocname.getWordAmount()*/);//todo,先不加词量
		}

		ListView<String> vocList = new ListView<>(vocs);//添加到TreeView中
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
		//用户在按开始前其他键的处理，其他键不能被按,先将4个键设为disaable，点开始后再设置可用
		btnCh.setDisable(false);
		btnKnown.setDisable(false);
		btnMemorized.setDisable(false);
		btnNext.setDisable(false);
		//已学新词和已复习词的数量初始化
		lbLearned.setText("0");
		lbReviewed.setText("0");
        VocDaoImpl vocDao = new VocDaoImpl();
        String quota = user.getQuota();
        int nullConquerCount = vocDao.getNullConquerCount(user.getVocName());
        if(nullConquerCount < Integer.valueOf(quota))
            lbTodayAmount.setText(nullConquerCount+"");
        else lbTodayAmount.setText(quota);
		lbTodayAmount.setText(quota);
		int reviewCount = vocDao.getReviewCount(user.getVocName());
		lbReviewAmount.setText(reviewCount+ "");
        UserDaoImpl userDao = new UserDaoImpl();
        User user = userDao.load(lbUid.getText());

        String vocName = user.getVocName();
		if(Objects.equals(vocName, null) || vocName.equals("")) showInfo("请先选择词库！");//先判断是否已生成临时词库
        else{
        	if(Objects.equals(user.getQuota(),null) || user.getQuota().equals("")
					|| user.getQuota().equals("null"))
        		showInfo("请先设置每次背词数量！");
			else{
//				System.out.println(user.getQuota());
//			将conquer >1的值-1
				wordDao.updateConquer(vocName);
				newSet = new HashSet<>();

				showNextWord(vocName);
			}
		}
	}

//	--------------------------------------------
/*新词时将其conquer和times初始化 */
	public void showNextWord(String vocName) {
		word = null;
		//查询生词
		if (newSet.size() < Integer.valueOf(lbTodayAmount.getText())) {
			word = wordDao.getByRandomNew(vocName);
			word.setConquer(1);
			word.setTimes(3);
			wordDao.update(word,vocName);
			newSet.add(Integer.valueOf(word.getId()));
			System.out.println("newSet = " + newSet);
		} else{
			//	复习单词
			word = wordDao.getByRandom(vocName);
		}
        if(word == null ){
        	showInfo("本次学习结束！");
			newSet.clear();
		}
        else {
            //		显示单词信息
            lbEn.setText(word.getEn());
//        System.out.println("word.getEn:"+word.getEn());
//        lbCh清空
            lbCh.setText(null);
        }
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
//	    	将入户输入的数字保存到user表中
            user.setQuota(tf.getText());
            userDao.mod(user);
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

	/*没有使用该方法*/
	public void saveVocName_Amount(){
		Property ppt = new Property();
    	ppt.setTotal(lbAmount.getText());
    	ppt.setQuota("50");//默认学习量50
    	ppt.setId(lbUid.getText());
    	ppt.setVocName(lbVocName.getText());
    	PropertyDao ppd = new PropertyDao();
    	try {
			ppd.setProperty(ppt);
		} catch (JDOMException | IOException e1) {
			e1.printStackTrace();
		}
	}


}
