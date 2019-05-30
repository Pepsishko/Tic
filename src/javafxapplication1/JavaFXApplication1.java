package javafxapplication1;

import java.awt.Point;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafxapplication1.data.GomokuBoard;

public class JavaFXApplication1 extends Application {

   // final int M_DEBUG=1, M_TEST=2, M_DEFAULT=3;
  //  int mode = M_DEFAULT;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainSetupFx.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
     //   if (mode==M_TEST) {
    //    } else if (mode==M_DEBUG) {
     //       System.out.println(root.getClass().getName());
     //   }
        stage.getIcons().add(new Image("javafxapplication1/data/X.png"));
        stage.setTitle("Игровые настройки");
        stage.show();
    }
    
    public static void main(String[] args) {        
        launch(args);
    }
    
}
