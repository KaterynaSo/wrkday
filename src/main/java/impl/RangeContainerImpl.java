package impl;

import services.Ids;
import services.RangeContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class RangeContainerImpl implements RangeContainer {

    Logger LOG = Logger.getLogger(this.getClass().toString());
    final long[] data;


    public RangeContainerImpl(final long[] data) {
        this.data = data;
    }

    //method to populate data in containers. as we have maximum 32k or records we can consider we have max 32 containers
    // and split data between them, basic case(data less then 1000), all data will go in to the one container
    public List<List<Long>> populateContainers(long[] data) {
        List<List<Long>> containers = new ArrayList<>();
        if(data == null){
            return containers;
        }
        int i = 1;
        int k = 0;// to add data in order of input array
        // number of containers depends on number of data in container(as we taking 32k as max, I took 1k records in container)
        int numberOfContainers = (data.length-1)/CONTAINER_SIZE +1;
        LOG.info("Populate " + numberOfContainers + " containers with data");
        while (i <= numberOfContainers ) {
            List<Long> lst = new ArrayList<>();
            int l = 0;//to keep track of number or records in container
            while ( k < data.length && l < CONTAINER_SIZE) {
                lst.add(data[k]);
                k++;
                l++;
            }
            containers.add(lst);
            i++;
        }
        return containers;
    }

    @Override
    // find Ids that meet requirements
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        //call container population
        List<List<Long>> containers = new ArrayList<>();
        try {
            containers = populateContainers(data);
        }catch (Exception e){ // better to use custopm exception, this is for example only
            LOG.info("Population of containers failed with Exception: " + e.getMessage());
        }
        IdsImpl ids = new IdsImpl();
        //if no containers were created, return empty result
        if(containers.size()==0){
            LOG.info("No containers were populated");
            return ids;
        }
        // create number of threads equal to number of containers
        int threadNumber = (containers.size());
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
        List<Callable<List<Short>>> todo = new ArrayList<Callable<List<Short>>>(containers.size());

        //create lists of tasks, one task for every thread
        for(List<Long> cnt: containers){
            todo.add(new IdsLookup(fromValue, toValue, fromInclusive, toInclusive, cnt, containers.indexOf(cnt)));
        }
        try {
            // execute our tasks
            List<Future<List<Short>>> results = executor.invokeAll(todo);
           // waits for prev tasks and terminates the executor
            executor.shutdown();
            //populate ids with result list
            if(results != null && results.size()>0) {
                for(Future<List<Short>> l : results){
                    List<Short> val = l.get();
                    for(Short v: val)
                    ids.add(v);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            LOG.info("Task execution failed with Exception: " + e.getMessage());
        }
        LOG.info("Were found " + ids.size() + " Ids");
        return ids;
    }



}
