package org.braun.digikam.web.ui;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.braun.digikam.backend.ejb.ImageFacade;
import org.braun.digikam.backend.model.StatisticMonth;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author mbraun
 */
@Named("statisticYearBean")
@ViewScoped
public class StatisticYearBean implements Serializable {
    
    private List<Year> years;
    
    private int maxYearCount;
    private int maxMonthCount;

    @Inject
    private ImageFacade facade;
    
    public List<Year> getYears() {
        if (years == null) {
            years = initialize();
        }
        return years;
    }
    
    private List<Year> initialize() {
        List<Year> res = new ArrayList<>();
        List<StatisticMonth> list = facade.statMonth(null);
        maxYearCount = 0;
        maxMonthCount = 0;
        int currentYear = 0;
        res.add(new Year(list.get(0).getYear()));
        for (StatisticMonth m : list) {
            if (m.getYear() != res.get(currentYear).getName()) {
                res.add(new Year(m.getYear()));
                currentYear++;
            }
            if (m.getCnt() > maxMonthCount) {
                maxMonthCount = m.getCnt();
            }
            res.get(currentYear).add(m.getCnt());
            res.get(currentYear).getMonth().add(new Month(m.getMonth(), m.getCnt()));
        }
        for (Year y : res) {
            if (y.getCount() > maxYearCount) {
                maxYearCount = y.getCount();
            }
        }
        return res;
    }

    public int getMaxYearCount() {
        return maxYearCount;
    }

    public int getMaxMonthCount() {
        return maxMonthCount;
    }
    
    public class Year {
        private final int name;
        private final List<Month> month;
        private int count;

        public Year(int name) {
            this.name = name;
            month = new ArrayList<>();
            count = 0;
        }

        public int getName() {
            return name;
        }

        public List<Month> getMonth() {
            return month;
        }

        public int getCount() {
            return count;
        }
        
        public void add(int count) {
            this.count+= count;
        }
    }
    
    public class Month {
        private final int name;
        
        private final int count;

        public Month(int name, int count) {
            this.name = name;
            this.count = count;
        }

        public int getName() {
            return name;
        }

        public int getCount() {
            return count;
        }
    }

}
