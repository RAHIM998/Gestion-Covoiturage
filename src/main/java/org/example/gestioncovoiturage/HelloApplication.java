package org.example.gestioncovoiturage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gestioncovoiturage.Repository.JPAUtil;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        JPAUtil.getEntityManagerFactory().createEntityManager();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WindowPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1010, 660);
        stage.setTitle("Gestion de covoiturage");
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}