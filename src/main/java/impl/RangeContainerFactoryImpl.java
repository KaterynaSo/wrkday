package impl;

import services.RangeContainer;
import services.RangeContainerFactory;

import java.util.logging.Logger;


public class RangeContainerFactoryImpl implements RangeContainerFactory {
    Logger LOG = Logger.getLogger(this.getClass().toString());
    public RangeContainer createContainer(long[] data) {
        //check if any data was passed into factory
        if(data == null) {
            LOG.info("No data was passed into factory");
            return null;
        }
        return new RangeContainerImpl(data);
    }
}
