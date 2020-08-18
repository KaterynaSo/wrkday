package impl;

import services.Ids;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Logger;

public class IdsImpl implements Ids {
    PriorityQueue<Short> ids ; // use to keep Ids sorted

    public IdsImpl()
    {
       // ids  = new ArrayList<Short>();
        ids = new PriorityQueue<>();
    }

    public void add(short index)
    {
        ids.add(index);

    }

    public short nextId() // as we have max ~32k or values, we can use short
    {
        if(!ids.isEmpty()) {
            Short id = ids.poll();
            return id;
        }
        else
            return Ids.END_OF_IDS;
    }
    public int size()
    {
        return ids.size();
    }
}
