import com.sun.xml.internal.bind.v2.runtime.Coordinator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Marcin on 24.05.2017.
 */
public class CoordinatorData implements Serializable {
    public String IP;
    public String Priority;
    public boolean IsBroken=false;
    public int getRealPriority()
    {

        //  String[] parts = IP.split(".");;

        int ret= Integer.parseInt(Priority);//*1000+Integer.parseInt(parts[parts.length-1]);
        return ret;
    }

   public  CoordinatorData(String IP)
    {
        this.IP=IP;
        this.Priority="-1";
    }
    public CoordinatorData(String IP, String priority) {
        this.IP = IP;
        Priority = priority;
    }




    @Override
    public String toString() {
        return "CoordinatorData{" +
                "IP='" + IP + '\'' +
                ", Priority=" + Priority +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinatorData that = (CoordinatorData) o;

        if (IP != null ? !IP.equals(that.IP) : that.IP != null) return false;
        return Priority != null ? Priority.equals(that.Priority) : that.Priority == null;
    }

    @Override
    public int hashCode() {
        int result = IP != null ? IP.hashCode() : 0;
        result = 31 * result + (Priority != null ? Priority.hashCode() : 0);
        return result;
    }
}
