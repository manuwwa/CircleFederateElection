import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

/**
 * Created by Marcin on 30.05.2017.
 */
public class CoordinatorList extends LinkedList<CoordinatorData> {

    public CoordinatorData bestCoordinator()
    {

        CoordinatorData ret= getFirst();
        for (CoordinatorData check:this)
        {
            if(ret.getRealPriority()<check.getRealPriority())
            {
                ret=check;
            }
        }
        return ret;
    }

    public int indexOfFromIP(String Ip) {
        for(int i=0;i<this.size();i++)
        {
            if(this.get(i).IP.equals(Ip))
            {
                return i;
            }
        }
        return -1;
    }
    public boolean addNoRepeads(CoordinatorData cor)
    {
        for(CoordinatorData test:this)
        {
           if(test.equals(cor)) return false;

        }
        this.add(cor);
        return true;
    }
    public boolean addNoRepeads(CoordinatorData cor,int index)
    {
        for(CoordinatorData test:this)
        {
            if(test.equals(cor)) return false;

        }
        this.add(index,cor);
        return true;
    }
    public void addCoordinatorAfterAdres(String newIP,String priority,String AfterAdres)
    {
        int index= indexOfFromIP(AfterAdres);
        this.add(index+1,new CoordinatorData(newIP,priority));
    }
    public CoordinatorList getBrokenListFor(CoordinatorData me)
    {
        int index= indexOf(me)+1;
        boolean foundAll=false;
        int hop=0;

        CoordinatorList ret= new CoordinatorList();
        while(foundAll&&hop<this.size()+3)
        {
            if(!(index<this.size()))
            {
                index=0;
            }
            if(this.get(index).IsBroken)
                ret.addNoRepeads(this.get(index));
            hop++;
        }
        return ret;
    }
    public  LinkedList<String> CoordinatorsIpOnlyInString()
    {
        LinkedList<String> ret=new LinkedList<>();
        for(CoordinatorData tmp:this)
        {
            ret.add(tmp.IP);
        }
        return ret;
    }
    public  ObservableList<String> toObsrvableList ()
    {
        return FXCollections.observableArrayList (this.CoordinatorsIpOnlyInString());
    }
}
