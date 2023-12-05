package org.braun.digikam.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.model.Camera;

/**
 *
 * @author mbraun
 */
public class CameraFactory {
    private static final Logger LOG = LogManager.getLogger();
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = LOCK.readLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();
    private List<CameraEntry> cameras;
    
    private static final CameraFactory INSTANCE = new CameraFactory();
    
    private CameraFactory() {}
    
    public static CameraFactory getInstance() {
        return INSTANCE;
    }
    
    public CameraEntry getByName(String name) {
        try {
            READ_LOCK.lock();
            if (name != null && !name.isEmpty()) {
                name = name.toLowerCase();
                for (CameraEntry c : cameras) {
                    if (c.getName().toLowerCase().equals(name))
                    return c;
                }
            }
        } catch (Exception e) {
            LOG.error("Aquire cameras-ReadLock failed", e);
        } finally {
            READ_LOCK.unlock();
        }
        return CameraEntry.newEmptyEntry();
    }
    
    public List<Camera> findByMakerAndModel(String makeAndModel) {
        try {
            READ_LOCK.lock();
            List<Camera> result = new ArrayList<>();
            if (makeAndModel == null || makeAndModel.isEmpty()) {
                for (CameraEntry c : cameras) {
                    result.add(new Camera().name(c.getName()));
                }
            } else {
                makeAndModel = makeAndModel.toLowerCase();
                for (CameraEntry c : cameras) {
                    if (c.getName().toLowerCase().contains(makeAndModel))
                    result.add(new Camera().name(c.getName()));
                }
            }
            return result;
        } catch (Exception e) {
            LOG.error("Aquire cameras-ReadLock failed", e);
            return Collections.emptyList();
        } finally {
            READ_LOCK.unlock();
        }
        
    }
    
    public void refresh(List<CameraEntry> cameras) {
        try {
            WRITE_LOCK.lock();
            if (this.cameras == null) {
                this.cameras = new ArrayList<>();
            } else {
                this.cameras.clear();
            }
            this.cameras.addAll(cameras);
        } catch (Exception e) {
            LOG.error("Aquire cameras-WriteLock failed", e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }
    
    public static class CameraEntry {
        
        private final String make;
        private final String model;
        private final String name;
        
        public CameraEntry(String make, String model) {
            this.make = make;
            this.model = model;
            if (model == null || model.isEmpty()) {
                name = make;
            } else {
                name = make + ", " + model;
            }
        }

        public String getMake() {
            return make;
        }

        public String getModel() {
            return model;
        }

        public String getName() {
            return name;
        }
        
        public static CameraEntry newEmptyEntry() {
            return new CameraEntry("", "");
        }
    }
}
