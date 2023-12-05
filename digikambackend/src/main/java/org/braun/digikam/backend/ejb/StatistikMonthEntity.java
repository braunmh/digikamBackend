package org.braun.digikam.backend.ejb;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.io.Serializable;

/**
 *
 * @author mbraun
 */
@Entity
public class StatistikMonthEntity {
    
    @EmbeddedId
    private PrimaryKey key;
    
    @Column(name = "count")
    private int count;

    public PrimaryKey getKey() {
        return key;
    }

    public void setKey(PrimaryKey key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    @Embeddable
    public static class PrimaryKey implements Serializable {
        
        @Column(name = "year", nullable = false)
        private int year;
        
        @Column(name = "month", nullable = false)
        private int month;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }
    }
}
