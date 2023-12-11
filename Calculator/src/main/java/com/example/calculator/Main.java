package com.example.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Project.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 324, 465);
            scene.getStylesheets().add(getClass().getResource("/com/example/calculator/styles.css").toExternalForm()); //dodanie stylów css
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setResizable(false);

            //wczytanie ikony aplikacji i ustawienie jej jako ikony głównego okna
            InputStream iconStream = getClass().getResourceAsStream("calculatorIcon.png");
            if (iconStream != null) { // czy poprawnie uzyskano strumień dla ikony
                Image icon = new Image(iconStream); //jesli pomyślnie uzyskano, tworzy obiekt Image na podstawie tego strumienia
                stage.getIcons().add(icon); //dodawanie do listy ikon przypisanych do głównego okna
            } else {
                System.out.println("Nie udało się wczytać ikony.");
            }

            //uzyskanie dostępu do kontrolera powiązanego z plikiem FXML i zainicjowanie go za pomocą metody init(stage)
            Controller controller = fxmlLoader.getController();
            controller.init(stage);

            stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}