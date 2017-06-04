import com.sun.xml.internal.bind.v2.runtime.Coordinator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sun.reflect.generics.tree.Tree;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

/**
 * Created by Marcin on 30.05.2017.
 */
public class JavaFX extends Application {
    int ileczekac=25;
    int aktualizacjaguiCo=500;
    boolean firstReg=true;
    int ttl=25;
    boolean DzialaAppka= true;
    boolean started =false;
    Hello hello;
    TextField IP;
    TextField Textttl;
    String change="";
    Thread pinger;
    Label electedCoordinatorLabel;
    TextField RegisterServerIP;
    TextField Priority;
    Thread tr;
    Button btn;
    Button popsuj;
    private static Registry registry;
    CoordinatorList coordinatorList= new CoordinatorList();
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
        Label tmpppp= new Label("Elected Coordinator:");
        electedCoordinatorLabel=new Label(("null"));
        grid.add(tmpppp,0,4);
        grid.add(electedCoordinatorLabel,1,4);
 //       Label tmppppp= new Label("ttl:");
 //       Textttl=new TextField();
 //       grid.add(tmppppp,0,5);
//        grid.add(Textttl,1,5);
        grid.add(btn,1,5);
        grid.add(popsuj,0,5);
        IPLIst.setMinSize(20,120);
        grid.add(IPLIst, 0, 6, 2, 6);
        grid.add(Konsola,0,30,2,30);
        primaryStage.setScene(new Scene(root, 500, 650));
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
                try {
                    Naming.unbind("rmi://"+IP.getText()+"/"+IP.getText());
                    started=false;
                } catch (NotBoundException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                popsuj.setVisible(false);
                btn.setVisible(true);
            }
        });
    }
    public void btnSetHendler(Stage primaryStage)
    {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TekstKonsola+="\n"+"IP:"+IP.getText()+"ServerIP:"+RegisterServerIP.getText();
                StartServer();
               StartElection();
                popsuj.setVisible(true);
                btn.setVisible(false);

            }
        });
    }
    public void StartServer()
    {
        try {
            try {
                registry = LocateRegistry.createRegistry(1099);
            }
            catch (Exception e)
            {

            }

            hello= new HelloImpl(this);

            Naming.rebind("rmi://"+IP.getText()+"/"+IP.getText(), hello);

            System.out.println("Uruchomiono serwer.");
            TekstKonsola+="\nUruchomiono serwer.";
            started=true;
            if (firstReg)
            {
                coordinatorList.addNoRepeads(new CoordinatorData(IP.getText(),Priority.getText()));
                coordinatorList.addNoRepeads(new CoordinatorData(RegisterServerIP.getText(),"-1"));
                TekstKonsola+="\nPierwsza rejestracja. Wysylam zadanie dodania do lancucha";

                firstReg=false;
            }
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public void DeclarePinger()
    {
        pinger= new Thread() {
            public void run() {
                int ProbyElekcji=0;
      //  CoordinatorList broken= new CoordinatorList();
                while (DzialaAppka) {
                    if(ProbyElekcji>0)
                    {
                        ProbyElekcji--;
                    //    StartElection();
                      //  System.out.println(ProbyElekcji+"Zlecenie eleckji"+zleconaElekcja);
                    }
                    if (started) {
                        // System.out.println("tik");
                        String nextIP="";
                        try {
                          //  System.out.println("nextip"+nextIP);
                           nextIP=coordinatorList.getNextNotBroken(IP.getText()).IP;

                            hello = (Hello) Naming.lookup("rmi://" + nextIP + "/" +nextIP);
                            hello.sayHello();
                          //  System.out.println("nextip"+nextIP);



                        } catch (Exception e) {
                            coordinatorList.getMe(nextIP).IsBroken= true;
                          //  broken=coordinatorList.getBrokenListFor(IP.getText());
                            TekstKonsola+="\nWykryto usterkeKoordynatora:"+nextIP+" Rozpoczynnam nowa elekcja";
                            ProbyElekcji=3;
                            StartElection();
                        }
                        CoordinatorData tmpCord=coordinatorList.getNext(IP.getText());
                      /*  if(coordinatorList.getNext(IP.getText()).IsBroken)
                        {
                            try
                            {
                                hello = (Hello) Naming.lookup("rmi://" + tmpCord.IP + "/" +tmpCord.IP);
                                hello.sayHello();
                                tmpCord.IsBroken=false;
                                TekstKonsola+="\nWezel:"+nextIP+" jest znowu aktywny";
                                StartElection();
                            } catch (Exception e){
                            }

                        }*/

                        for(CoordinatorData bcor:coordinatorList.getBrokenListFor(IP.getText()))
                        {

                            try
                            {
                                hello = (Hello) Naming.lookup("rmi://" + bcor.IP + "/" +bcor.IP);
                                hello.sayHello();
                                bcor.IsBroken=false;
                                TekstKonsola+="\nWezel:"+nextIP+" jest znowu aktywny";

                                StartElection();
                        } catch (Exception e){
                        }

                        }


                    }
                    // System.out.println("elo");
                    try {
                        Thread.sleep(aktualizacjaguiCo);
                    } catch (InterruptedException e) {
                        return;
                    }
                }

            }
        };
    }
    boolean zleconaElekcja=false;
    Thread ElectionThread;
    public void declareElectionThread()
    {
        ElectionThread = new Thread() {
        public void run() {
            while(true)
            {
                if(zleconaElekcja&&started)
                {

                    zleconaElekcja=false;

                    if(coordinatorList.size()==1)
                    {
                        electedCoordinator=new CoordinatorData(IP.getText(),Priority.getText());
                        TekstKonsola+="\n"+"Nie znaleziono innych wezlow. Wezel:"+IP.getText()+";"+Priority.getText()+" zosal wybrany na koordynatora";
                    }
                    else
                    if(coordinatorList.getListNotBroken().size()==1)
                    {
                        electedCoordinator=coordinatorList.getListNotBroken().getFirst();
                        TekstKonsola+="\n"+"Zostal jeden koordynator wiec wygrywa on elekcjie";
                    }
                    else
                    {
                        Boolean udane=false;
                        CoordinatorData coord=coordinatorList.getNextNotBroken(IP.getText());
                        TekstKonsola+="\n"+"Rozpoczynnam elekcje."+coord;

                        while(!udane)
                        {
                            while(coord.IP.equals(IP.getText()))
                            {
                                coord=coordinatorList.getNextNotBroken(IP.getText());
                                try {
                                    Thread.sleep(ileczekac);
                                } catch (InterruptedException e1) {
                                }
                            }
                            // System.out.println("Wysylam do"+coord.IP);
                            TekstKonsola+="\n"+"Wysylam pakiet elekcji do:"+coord.IP;
                            try {
                                System.out.println("Rozpoczynam elekcje do"+coord+coordinatorList);
                                hello = (Hello) Naming.lookup("rmi://"+coord.IP+"/"+coord.IP);
                                //  System.out.println(IP.getText()+ttl+IP.getText()+coordinatorList);
                                hello.election( IP.getText(),ttl,IP.getText(),coordinatorList);

                                udane=true;

                            }
                            catch (Exception e) {
                                coord=coordinatorList.getNextNotBroken(IP.getText());
                                System.out.println("cos nie tak do"+coord+"blad"+e);
                                TekstKonsola+="\n"+"Wyslanie do:"+coord.IP+" Nie udane, ponowne wysylanie";
                                try {
                                    Thread.sleep(ileczekac);
                                } catch (InterruptedException e1) {
                                }
                                //  coord=coordinatorList.getNextNotBroken(this.IP.getText());
                            }
                        }

                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }

        }
        };

    }
    CoordinatorData electedCoordinator;
    public void StartElection(){
        zleconaElekcja=true;
    }
    public void rewriteControls()
    {
        tr= new Thread() {
            public void run() {

                while (DzialaAppka) {
                    Platform.runLater(new Runnable() {

                        public void run() {

                            if (!TekstKonsola.equals(change)) {
                                ObservableList<String> items = FXCollections.observableArrayList(coordinatorList.ShowCoordinatorInList());
                                IPLIst.setItems(items);
                                Konsola.setText("Konsola:\n" + TekstKonsola);
                                change = TekstKonsola + "";
                                if(electedCoordinator!=null)
                                electedCoordinatorLabel.setText(electedCoordinator.IP+","+electedCoordinator.Priority);
                              //  ttl= Integer.parseInt( Textttl.getText());
                            }

                        }
                    });
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
      tr.start();

    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        setLeyout(primaryStage);
        rewriteControls();
        DeclarePinger();
        declareElectionThread();
        ElectionThread.start();
        pinger.start();
    }
    @Override
    public void stop() {
        System.out.println("Closing app");
        System.exit(0);
    }
}
