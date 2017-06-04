import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.lang.reflect.Array;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 * Created by Zoufland on 18.05.2017.
 */
public interface Hello extends Remote {
    void sayHello() throws RemoteException;
    void election(String whoStarts,int ttl,String whoSends,CoordinatorList list) throws RemoteException;
    void elected(String whoStarts,int ttl,CoordinatorData winner) throws RemoteException;
//    LinkedList<String> Register(String IP)throws RemoteException;
//    void SendUpdate(LinkedList<String> IPLIST)throws RemoteException;
//    void SendUpdateInCircle(LinkedList<String> IPLIST,String wykryl)throws RemoteException;
//    void election(LinkedList<CoordinatorData> Coordinators,CoordinatorData whoStarts) throws RemoteException;
//    void coordinator(CoordinatorData winner,CoordinatorData whoStarts) throws RemoteException;
//    LinkedList<String> AskForList() throws RemoteException;
//    void SendList(LinkedList<String> ListIP) throws RemoteException;

}
