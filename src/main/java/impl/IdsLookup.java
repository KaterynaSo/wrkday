package impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static services.RangeContainer.CONTAINER_SIZE;

class IdsLookup implements Callable<List<Short>> {
    private final long fromValue;
    private final long toValue;
    private final boolean fromInclusive;
    private final boolean toInclusive;
    private final List<Long> container;
    private final int shift;

    public IdsLookup(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive,
                     List<Long> container, int shift) {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.fromInclusive = fromInclusive;
        this.toInclusive = toInclusive;
        this.container = container;
        this.shift = shift;
    }


    @Override
    public List<Short> call() {
        List<Short> results = new ArrayList<>();
        for (Long val : container) {
            //check values in the container abd add to the result
            // as we don't want to sort data, we just iterate over limited count of numbers (<=1000, teh capacity of container)
            //but we can think about more efficient search
            if (( val > fromValue ||fromInclusive && val >= fromValue)
                    && (val < toValue || toInclusive && val <= toValue )) {
                // add ids to the result taking into consideration container number
                results.add((short) (container.indexOf(val) + CONTAINER_SIZE * shift));
            }
        }
        return results;
    }
}
