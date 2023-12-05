package org.braun.digikam.backend.ejb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 *
 * @author mbraun
 */
@Entity
public class StatisticKeywordEntity {
    @Id
    @Column(name = "id")
    int id;
    
    @Column(name = "name")
    String name;
    
    @Column(name = "cnt")
    int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
