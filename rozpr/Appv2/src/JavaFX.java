import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by Marcin on 30.05.2017.
 */
public class JavaFX extends Application {
    TextField IP;
    String change="";
    Thread pinger;
    TextField RegisterServerIP;
    TextField Priority;
    Thread tr;
    Button btn;
    Button popsuj;
    TextArea Konsola= new TextArea("Brak wpisow");
    public ListView<String> IPLIst= new ListView<>();
    public String TekstKonsola="Brak Wpisow";
    public static void main(String args[]) {
        launch(args);
    }
    public void setLeyout(Stage primaryStage)
    {
        primaryStage.setTitle("RMI");
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        btn = new Button();
        popsuj = new Button();
        popsuj.setText("Popsuj");
        btn.setText("Zarejestruj");
        popsujSetHendler(primaryStage);
        btnSetHendler(primaryStage);
        StackPane root = new StackPane();
        GridPane grid = new GridPane();
        Label userName = new Label("ServerIP:");
        grid.add(userName, 0, 1);
        IP = new TextField();
        grid.add(IP, 1, 1);
        Label pw = new Label("RegisterServerIP:");
        grid.add(pw, 0, 2);
        RegisterServerIP = new TextField();
        grid.add(RegisterServerIP, 1, 2);
        IPLIst.setPrefHeight(70);
        Label pp = new Label("Priority:");
        grid.add(pp, 0, 3);
        Priority = new TextField();
        grid.add(Priority, 1, 3);
        grid.add(btn,1,4);
        grid.add(popsuj,0,4);
        grid.add(IPLIst, 0, 5, 2, 5);
        grid.add(Konsola,0,10,2,10);
        primaryStage.setScene(new Scene(root, 300, 250));
        root.getChildren().add(grid);
        popsuj.setVisible(false);
        btn.setVisible(true);
        primaryStage.show();
    }
    public void popsujSetHendler(Stage primaryStage)
    {
        popsuj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }
    public void btnSetHendler(Stage primaryStage)
    {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setLeyout(primaryStage);
    }
}
