package org.braun.digikam.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author mbraun
 * @param <T>
 */
public class ListBuilder<T> {

    private final ArrayList<T> list;

    public ListBuilder() {
        list = new ArrayList<>();
    }

    public ListBuilder<T> add(T entry) {
        list.add(entry);
        return this;
    }

    public ListBuilder<T> addArray(T... entries) {
        for (T entry : entries) {
            list.add(entry);
        }
        return this;
    }

    public ListBuilder<T> addAll(Collection<T> entries) {
        list.addAll(entries);
        return this;
    }

    public List<T> build() {
        return list;
    }
}
