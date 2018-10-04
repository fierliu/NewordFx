package allan;

import controller.MyController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		try{
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/MyScene.fxml"));
			Parent root = fxmlloader.load();
			primaryStage.setTitle("Neword");
			primaryStage.setScene(new Scene(root));
			MyController controller = fxmlloader.getController();
			controller.setStage(primaryStage);
			primaryStage.setResizable(false);
			primaryStage.show();
		}catch(Exception e) {
            e.printStackTrace();
		}
	}
    public static void main(String[] args) {
        launch(args);
    }
}
