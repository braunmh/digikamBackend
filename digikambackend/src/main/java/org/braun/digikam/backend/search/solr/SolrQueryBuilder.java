package org.braun.digikam.backend.search.solr;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.braun.digikam.backend.ejb.DateWrapper;

/**
 *
 * @author mbraun
 */
public class SolrQueryBuilder {
    
    private final SolrQuery solrQuery;
    private final StringBuilder query;
    
    public SolrQueryBuilder() {
        solrQuery = new SolrQuery();
        solrQuery.setStart(0);
        solrQuery.setRows(2000);
        query = new StringBuilder();
    }
    
    public SolrQuery build() {
        solrQuery.setQuery(query.toString());
        return solrQuery;
    }
    
    public SolrQueryBuilder addQuery(String fieldName, DateWrapper value) {
        if (value.isEmpty()) {
            return this;
        }
        addOperator();
        query.append(fieldName).append(":[")
            .append(value.getLowerBoundZdtFormatted())
            .append(" TO ")
            .append(value.getUpperBoundZdtFormatted())
            .append("]");
        return this;
    }
    
    public SolrQueryBuilder addQuery(String fieldName, Integer from, Integer to) {
        if (from == null && to == null) {
            return this;
        }
        if (from == null) {
            return addQuery(fieldName, to);
        }
        if (to == null) {
            return addQuery(fieldName, from);
        }
        if (from == to) {
            return addQuery(fieldName, from);
        }
        addOperator();
        query.append(fieldName).append(":[")
            .append(String.valueOf(from))
            .append(" TO ")
            .append(String.valueOf(to))
            .append("]");
        return this;
    }
    
    public SolrQueryBuilder addQuery(String fieldName, Double from, Double to) {
        if (from == null && to == null) {
            return this;
        }
        if (from == null) {
            return addQuery(fieldName, to);
        }
        if (to == null) {
            return addQuery(fieldName, from);
        }
        if (from == to) {
            return addQuery(fieldName, from);
        }
        addOperator();
        query.append(fieldName).append(":[")
            .append(String.valueOf(from))
            .append(" TO ")
            .append(String.valueOf(to))
            .append("]");
        return this;
    }
    
    public SolrQueryBuilder addQuery(String fieldName, DateWrapper from, DateWrapper to) {
        if (from.isEmpty() && to.isEmpty()) {
            return this;
        }
        if (from.isEmpty()) {
            return addQuery(fieldName, to);
        }
        if (to.isEmpty()) {
            return addQuery(fieldName, from);
        }
        if (from == to) {
            return addQuery(fieldName, from);
        }
        addOperator();
        query.append(fieldName).append(":[")
            .append(from.getLowerBoundZdtFormatted())
            .append(" TO ")
            .append(to.getUpperBoundZdtFormatted())
            .append("]");
        return this;
    }
    
    public SolrQueryBuilder addQuery(String fieldName, String value) {
        if (StringUtils.isEmpty(value)) {
            return this;
        }
        addOperator();
        query.append(fieldName).append(":")
            .append("\"")
            .append(value)
            .append("\"");
        return this;
    }
    
    public SolrQueryBuilder addQuery(String fieldName, Integer value) {
        if (value == null) {
            return this;
        }
        addOperator();
        query.append(fieldName).append(":")
            .append(String.valueOf(value));
        return this;
    }
    
    public SolrQueryBuilder addQuery(String fieldName, Double value) {
        if (value == null) {
            return this;
        }
        addOperator();
        query.append(fieldName).append(":")
            .append(String.valueOf(value));
        return this;
    }
    
    public SolrQueryBuilder addQuery(String fieldName, Collection<Long> ids, boolean operatorOr) {
        if (ids == null || ids.isEmpty()) {
            return this;
        }
        List<String> values = ids.stream().map(i -> String.valueOf(i)).toList();
        if (operatorOr) {
            addOperator();
            query.append(fieldName).append(":")
                .append("(")
                .append(String.join(" ", values))
                .append(")");
        } else {
            addOperator();
            query.append(fieldName).append(":")
                .append(String.join(" AND " + fieldName + ":", values));
        }
        return this;
    }
    
    /**
     * Adds an Geo-Search
     * 
     * @param fieldName
     * @param latitude
     * @param longitude
     * @param distance
     * @return SolrQueryBuilder
     */
    public SolrQueryBuilder addQuery(String fieldName, Double latitude, Double longitude, Double distance) {
        if (latitude == null || longitude == null || distance == null) {
            return this;
        }
        solrQuery.setFilterQueries("{!geofilt}");
        solrQuery.set("pt", String.valueOf(latitude) + "," + String.valueOf(longitude));
        solrQuery.set("sfield", fieldName);
        solrQuery.set("d", String.valueOf(distance));
        return this;
    }
    
    /**
     * 
     * @param fieldName to include in result
     * @return SolrQueryBuilder
     */
    public SolrQueryBuilder addField(String fieldName) {
        solrQuery.addField(fieldName);
        return this;
    }
    
    public SolrQueryBuilder setStart(int start) {
        solrQuery.setStart(start);
        return this;
    }

    /**
     * 
     * @param rows number of rows to return
     * @return SolrQueryBuilder
     */
    public SolrQueryBuilder setRows(int rows) {
        solrQuery.setRows(rows);
        return this;
    }
    
    private void addOperator() {
        if (query.length() > 0) {
            query.append(" AND ");
        }
    }
}
