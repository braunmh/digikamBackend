/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.braun.digikam.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mbraun
 */
public class TopicDisplayView implements Serializable {
    
    private String title;
    private String description;
    private Date from;
    private Date to;
    private final List<EntryPerDay> entries;
    private int latestEntry = -1;

    public void newEntry(Date date) {
        latestEntry++;
        EntryPerDay e = new EntryPerDay();
        e.setDate(date);
        entries.add(e);
    }

    public EntryPerDay getLatest() {
        return entries.get(latestEntry);
    }

    public TopicDisplayView() {
        this.entries = new ArrayList<>();
    }

    public List<EntryPerDay> getEntries() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
    
}
