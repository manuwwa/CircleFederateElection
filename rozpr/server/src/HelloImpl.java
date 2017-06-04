import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Marcin on 30.05.2017.
 */
public class HelloImpl extends UnicastRemoteObject implements Hello {
    JavaFX window;

    public HelloImpl(JavaFX window) throws RemoteException {
        this.window = window;
    }

    @Override
    public void sayHello() throws RemoteException {

    }

    @Override
    public void election(String whoStarts, int ttl, String whoSends, CoordinatorList list) throws RemoteException {

        int myindex = list.indexOfFromIP(window.IP.getText());

        if (whoStarts.equals( window.IP.getText())) {
            window.coordinatorList = list;
            window.electedCoordinator=window.coordinatorList.bestCoordinator();
            window.TekstKonsola += "\n" + "Elekcje wygrywa:" + window.electedCoordinator;
            SendElected(whoStarts,window.ttl,window.electedCoordinator);
        } else {

            if (myindex == -1) {
                list.addNoRepeads(new CoordinatorData(window.IP.getText(), window.Priority.getText()), list.indexOfFromIP(whoSends));
                myindex = list.indexOfFromIP(window.IP.getText());
            } else {
                list.getMe(window.IP.getText()).Priority = window.Priority.getText();
            }
            int NextIndex = list.indexOfFromIP(window.RegisterServerIP.getText());

            if (NextIndex == -1) {
                list.addNoRepeads(new CoordinatorData(window.RegisterServerIP.getText(), "-1"), list.indexOfFromIP(whoSends));
               // NextIndex = list.indexOfFromIP(window.IP.getText());
                NextIndex = list.indexOfFromIP(window.RegisterServerIP.getText());
            }
            if(list.get(myindex).IsBroken) list.get(myindex).IsBroken=false;
            list.margeList(window.coordinatorList,window.coordinatorList.indexOfFromIP(window.IP.getText()));
            window.coordinatorList=list;
            Boolean udane = false;
            CoordinatorData coord = list.getNextNotBroken(window.IP.getText());
            window.TekstKonsola += "\n" + "Odebrano zadanie elekcji od:" + whoSends;


            while (!udane) {
                window.TekstKonsola += "\n" + "Wysylam pakiet elekcji do:" + coord.IP;
                try {
                    System.out.println("Dostalem zacza:"+whoSends+"od:"+whoSends+"ttl"+ttl+"do:"+coord.IP+"Lista:"+list+"indeksy:"+NextIndex);
                    window.hello = (Hello) Naming.lookup("rmi://" + coord.IP + "/" + coord.IP);
                    window.hello.election(whoStarts, ttl-1, window.IP.getText(), window.coordinatorList);
                    udane = true;
                } catch (Exception e) {
                    window.TekstKonsola += "\n" + "Wyslanie do:" + coord.IP + " Nie udane, ponowne wysylanie";
                    coord = window.coordinatorList.getNextNotBroken(window.IP.getText());
                    System.out.println("asdas:"+coord);
                    try {
                        Thread.sleep(window.ileczekac);
                    } catch (InterruptedException e1) {
                    }

                }

            }


        }

    }
private void SendElected(String whoStarts, int ttl, CoordinatorData winner)
{
    window.electedCoordinator=winner;
    Boolean udane = false;

    CoordinatorData coord = window.coordinatorList.getNextNotBroken(window.IP.getText());
    window.TekstKonsola += "\n" + "Odebrano wiadomosc o zwyciescy elekcji\n jest to:"+winner;


    while (!udane) {
        window.TekstKonsola += "\n" + "Wysylam wiadomosc o zwyciescy elekcji do:" + coord.IP;
        try {
            window.hello = (Hello) Naming.lookup("rmi://" + coord.IP + "/" + coord.IP);
            window.hello.elected(whoStarts, ttl-1,winner);
            udane = true;
        } catch (Exception e) {
            window.TekstKonsola += "\n" + "Wyslanie do:" + coord.IP + " Nie udane, ponowne wysylanie";
            coord = window.coordinatorList.getNextNotBroken(window.IP.getText());
            try {
                Thread.sleep(window.ileczekac);
            } catch (InterruptedException e1) {
            }

        }

    }
}
    @Override
    public void elected(String whoStarts, int ttl, CoordinatorData winner) throws RemoteException {
        if(whoStarts.equals(window.IP.getText()))
        {

        }
        else
        {
            SendElected(whoStarts,ttl,winner);
        }

    }
}
