import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

/**
 * Created by Marcin on 30.05.2017.
 */
public class CoordinatorList extends LinkedList<CoordinatorData> {

    public CoordinatorData bestCoordinator()
    {

        CoordinatorList tmpList=getListNotBroken();
        CoordinatorData ret= tmpList.getFirst();
        System.out.println(tmpList);
        for (CoordinatorData check:tmpList)
        {
            if(ret.getRealPriority()<check.getRealPriority())
            {

                ret=check;
            }
        }
        return ret;
    }
    public CoordinatorData getMe(String ip)
    {
      return get( indexOfFromIP(ip));
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
    public void margeList(CoordinatorList List,int startIndex)
    {
        int index=startIndex+1;
        while(index!=startIndex)
        {
            if(index>=List.size()) index=0;
            else
            {
                System.out.println(index+"Start="+startIndex);
                addNoRepeads(List.get(index));
                index++;
            }

        }
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
    public CoordinatorData getNextNotBroken(String me)
    {
        return getNextNotBroken(getMe(me));
    }
    public CoordinatorData getNext(CoordinatorData me)
    {
        if(indexOfFromIP(me.IP)+1>=size()) return get(0);
        else
            return get(indexOfFromIP(me.IP)+1);
    }
    public CoordinatorData getNext(String me)
    {
        if(indexOfFromIP(me)+1>=size()) return get(0);
        else
            return get(indexOfFromIP(me)+1);
    }
    public CoordinatorData getNextNotBroken(CoordinatorData me)
    {
        CoordinatorData ret= getNext(me);
        while(ret.IsBroken)
        {
            ret= getNext(ret);
        }
        return ret;
    }
    public CoordinatorList getListNotBroken()
    {

        CoordinatorList ret= new CoordinatorList();
        for(CoordinatorData cor:this)
        {
            if(!cor.IsBroken) ret.add(cor);
        }

        return ret;
    }
    public CoordinatorList getBrokenListFor(String me)
    {
        return getBrokenListFor(getMe(me));
    }
    public CoordinatorList getBrokenListFor(CoordinatorData me)
    {
        boolean end=false;
        CoordinatorList ret= new CoordinatorList();
        CoordinatorData last=me;
        while(!end)
        {
            last=getNext(last);
            if(last.IsBroken) ret.add(last);
            else end=true;
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
    public LinkedList<String> ShowCoordinatorInList()
    {
        LinkedList<String> ret= new LinkedList<String>();
        for(CoordinatorData cor:this)
        {
            if(cor.IsBroken)
                ret.add(cor.IP+" "+cor.Priority+" Popsuty" );
            else
                ret.add(cor.IP+" "+cor.Priority);

        }
        return ret;
    }
}
