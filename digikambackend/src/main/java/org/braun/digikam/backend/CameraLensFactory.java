package org.braun.digikam.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.model.CameraLens;

/**
 *
 * @author mbraun
 */
public class CameraLensFactory {
    private static final Logger LOG = LogManager.getLogger();
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = LOCK.readLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();
    
    private static final CameraLensFactory INSTANCE = new CameraLensFactory();
    private final List<CameraLens> combinations;
    private final List<String> lenses;
    
    private CameraLensFactory() {
        combinations = new ArrayList<>();
        lenses = new ArrayList<>();
    }
    
    public static final CameraLensFactory getInstance() {
        return INSTANCE;
    }
    
    public void refresh(List<CameraLens> list) {
        try {
            WRITE_LOCK.lock();
            combinations.clear();
            combinations.addAll(list);
            Set<String> set = combinations.stream()
                    .map(c -> c.getLens())
                    .collect(Collectors.toSet());
            lenses.addAll(set.stream()
                    .sorted((l1, l2) -> 
                            l1.compareTo(l2))
                    .toList());
        } catch (Exception e) {
            LOG.error("Aquiring WriteLock failed", e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }
    
    public List<CameraLens> getAll() {
        try {
            READ_LOCK.lock();
            return combinations;
        } catch (Exception e) {
            LOG.error("Aquiring ReadLock failed", e);
        } finally {
            READ_LOCK.unlock();
        }
        return Collections.emptyList();
    }
    
    public List<String> getLensByCameraAndLens(String camera, String lens) {
        String tcamera = (StringUtils.isBlank(camera)) ? "" : camera.toLowerCase();
        String tlens = (StringUtils.isBlank(lens)) ? "" : lens.toLowerCase();
        try {
            READ_LOCK.lock();
            if (StringUtils.isBlank(tcamera)) {
                if (StringUtils.isBlank(tlens)) {
                    return lenses;
                } else {
                    return lenses
                            .stream()
                            .filter(l -> l.toLowerCase().contains(tlens))
                            .toList();
                }
            }
            if (StringUtils.isBlank(tlens)) {
                return combinations
                        .stream()
                        .filter(c -> c.getCamera().toLowerCase().equals(tcamera))
                        .map(c -> c.getLens())
                        .toList();
            } else {
                return combinations
                        .stream()
                        .filter(c -> 
                                c.getCamera().toLowerCase().equals(tcamera)
                                && c.getLens().toLowerCase().contains(tlens)
                        )
                        .map(c -> c.getLens())
                        .toList();
            }
        } catch (Exception e) {
            LOG.error("Aquiring ReadLock failed", e);
        } finally {
            READ_LOCK.unlock();
        }
        return Collections.emptyList();
    }
}
