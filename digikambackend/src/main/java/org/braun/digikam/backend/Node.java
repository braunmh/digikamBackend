package org.braun.digikam.backend;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mbraun
 */
public class Node {
    private final int id;
    private final  String name;
    private String fullName;
    private String qualifiedName;
    
    private final List<Node> children;

    private Node parent;
    
    public Node(int id, String name) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
