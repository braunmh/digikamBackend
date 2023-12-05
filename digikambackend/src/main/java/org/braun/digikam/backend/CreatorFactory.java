package org.braun.digikam.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.model.Creator;

/**
 *
 * @author mbraun
 */
public class CreatorFactory {
    private static final Logger LOG = LogManager.getLogger();
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = LOCK.readLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();
    private List<String> creators;
    
    private static final CreatorFactory INSTANCE = new CreatorFactory();
    
    private CreatorFactory() {}
    
    public static CreatorFactory getInstance() {
        return INSTANCE;
    }
    
    public List<Creator> findByName(String name) {
        try {
            READ_LOCK.lock();
            List<Creator> result = new ArrayList<>();
            if (name == null || name.isEmpty()) {
                for (String c : creators) {
                    result.add(new Creator().name(c));
                }
            } else {
                name = name.toLowerCase();
                for (String c : creators) {
                    if (c.toLowerCase().contains(name))
                    result.add(new Creator().name(c));
                }
            }
            return result;
        } catch (Exception e) {
            LOG.error("Aquire creator-ReadLock failed", e);
            return Collections.emptyList();
        } finally {
            READ_LOCK.unlock();
        }
        
    }
    
    public void refresh(List<String> creators) {
        try {
            WRITE_LOCK.lock();
            if (this.creators == null) {
                this.creators = new ArrayList<>();
            } else {
                this.creators.clear();
            }
            this.creators.addAll(creators);
        } catch (Exception e) {
            LOG.error("Aquire creator-WriteLock failed", e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }
}
