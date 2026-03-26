package org.braun.digikam.web.ui;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.ejb.TopicFacade;
import org.braun.digikam.web.model.TopicView;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author mbraun
 */
@Named("topicOverviewBean")
@ApplicationScoped
public class TopicOverviewBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger();
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = LOCK.readLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();

    @Inject
    private TopicFacade topicFacade;

    private TreeNode root;

    public TreeNode getRoot() {
        try {
            READ_LOCK.lock();
            if (root != null) {
                return root;
            }
        } catch (Exception e) {
            LOG.error("Aquiring ReadLock failed for read", e);
        } finally {
            READ_LOCK.unlock();
        }
        init();
        return root;
    }

    public void refresh() {
        try {
            WRITE_LOCK.lock();
            root = null;
        } catch (Exception e) {
            LOG.error("Aquiring WriteLock failed for refresh", e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }
    
    private void init() {
        try {
            WRITE_LOCK.lock();
            List<TopicView> entries = topicFacade.findAll();
            root = new DefaultTreeNode("Einträge", null);
            if (entries.isEmpty()) {
                return;
            }
            List<DefaultTreeNode<TopicNode>> nodes = new ArrayList<>();
            Set<Integer> y = entries.stream().map(tv -> tv.getYear()).collect(Collectors.toSet());

            Map<Integer, DefaultTreeNode<TopicNode>> yearsTen = new HashMap<>();

            for (Integer year : y) {
                Integer yearTen = year / 10 * 10;
                if (!yearsTen.containsKey(yearTen)) {
                    yearsTen.put(yearTen, new DefaultTreeNode<>(new TopicNode().id(0).title("" + yearTen), root));
                }
            }
            nodes.addAll(yearsTen.values());

            Map<Integer, DefaultTreeNode<TopicNode>> years = new HashMap<>();
            for (Integer year : y) {
                if (!years.containsKey(year)) {
                    Integer yearTen = year / 10 * 10;
                    years.put(year, new DefaultTreeNode<>(new TopicNode().id(0).title("" + year), yearsTen.get(yearTen)));
                }
            }
            nodes.addAll(years.values());

            Map<YearKeyword, DefaultTreeNode<TopicNode>> yearKeywords = new HashMap<>();
            Set<YearKeyword> yk = entries.stream().map(tv -> new YearKeyword().year(tv.getYear()).keywordId(tv.getKeyword().getId())).collect(Collectors.toSet());
            for (TopicView tv : entries) {
                DefaultTreeNode<TopicNode> parent = years.get(tv.getYear());
                YearKeyword yearKeyword = new YearKeyword().keywordId(tv.getKeyword().getId()).year(tv.getYear());
                if (!yearKeywords.containsKey(yearKeyword)) {
                    yearKeywords.put(yearKeyword, new DefaultTreeNode<>(new TopicNode().id(0).title(tv.getKeyword().getName()), parent));
                }
            }
            nodes.addAll(yearKeywords.values());

            for (TopicView tv : entries) {
                YearKeyword yearKeyword = new YearKeyword().keywordId(tv.getKeyword().getId()).year(tv.getYear());
                DefaultTreeNode<TopicNode> parent = yearKeywords.get(yearKeyword);
                DefaultTreeNode<TopicNode> child = new DefaultTreeNode<>(new TopicNode().id(tv.getId()).title(formatTitle(tv.getBegin(), tv.getTitle())), parent);
                nodes.add(child);
            }
        } catch (Exception e) {
            LOG.error("Aquiring WriteLock failed init", e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    private String formatTitle(Date date, String title) {
        return (date == null) ? title : new SimpleDateFormat("dd.MM.yyyy").format(date) + " - " + title;
    }

    private class YearKeyword implements Comparable<YearKeyword> {

        private Integer year;
        private Long keywordId;

        @Override
        public int compareTo(YearKeyword o) {
            if (!Objects.equals(year, o.getYear())) {
                return year.compareTo(o.getYear());
            }
            return keywordId.compareTo(o.getKeywordId());
        }

        public Integer getYear() {
            return year;
        }

        public YearKeyword year(Integer value) {
            year = value;
            return this;
        }

        public YearKeyword keywordId(Long value) {
            keywordId = value;
            return this;
        }

        public Long getKeywordId() {
            return keywordId;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.year);
            hash = 53 * hash + Objects.hashCode(this.keywordId);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final YearKeyword other = (YearKeyword) obj;
            if (!Objects.equals(this.year, other.year)) {
                return false;
            }
            return Objects.equals(this.keywordId, other.keywordId);
        }

    }

    public static class TopicNode implements Serializable {

        long id;
        String title;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public TopicNode id(long value) {
            id = value;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public TopicNode title(String value) {
            title = value;
            return this;
        }

        public boolean isLeaf() {
            return id > 0;
        }
    }

}
