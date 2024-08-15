package org.braun.digikam.backend;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author mbraun
 */
public class StatusFactory {
    private static final Logger LOG = LogManager.getLogger();
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = LOCK.readLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();

    private static final StatusFactory INSTANCE = new StatusFactory();
    
    private boolean tumbnailGenerationStatus;
    
    private StatusFactory() {
        tumbnailGenerationStatus = false;
    }

    public static StatusFactory getInstance() {
        return INSTANCE;
    }
    
    public boolean getTumbnailGenerationStatus() {
        try {
            READ_LOCK.lock();
            return tumbnailGenerationStatus;
        } catch (Exception e) {
            LOG.error("Aquire StatusFactory-ReadLock failed", e);
            return tumbnailGenerationStatus;
        } finally {
            READ_LOCK.unlock();
        }
    }
    
    public boolean aquireTumbnailGenerationStatusBussy() {
        try {
            WRITE_LOCK.lock();
            if (!tumbnailGenerationStatus) {
                tumbnailGenerationStatus = true;
                return true;
            }
            return false;
        } catch (Exception e) {
            LOG.error("Aquire StatusFactory-WriteLock failed", e);
            return tumbnailGenerationStatus;
        } finally {
            WRITE_LOCK.unlock();
        }
    }
    
    public boolean aquireTumbnailGenerationStatusDone() {
        try {
            WRITE_LOCK.lock();
            if (tumbnailGenerationStatus) {
                tumbnailGenerationStatus = false;
                return true;
            }
            return false;
        } catch (Exception e) {
            LOG.error("Aquire StatusFactory-WriteLock failed", e);
            return tumbnailGenerationStatus;
        } finally {
            WRITE_LOCK.unlock();
        }
    }
    
}
