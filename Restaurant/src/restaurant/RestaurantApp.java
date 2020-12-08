
package restaurant;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RestaurantApp extends Application{
    public static void main(String[] args) {
            launch(args);
    }
	
    @Override
    public void start(Stage primary) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("./view/root.fxml"));
        Scene scene = new Scene(root);
        primary.setScene(scene);
        primary.show();
    }
}