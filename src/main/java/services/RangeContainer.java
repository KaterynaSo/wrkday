package services;

import java.util.concurrent.CopyOnWriteArrayList;

public interface RangeContainer  {
    /**

     * @return the Ids of all instances found in the container that * have data value between fromValue and toValue with optional * inclusivity. The ids should be returned in ascending order when retrieved

     * using nextId().

     */
    //short CONTAINER_SIZE = 1000;
    short CONTAINER_SIZE = 3;
    Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive);
}
