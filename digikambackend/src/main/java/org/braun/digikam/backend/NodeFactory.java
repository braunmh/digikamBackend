package org.braun.digikam.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.ejb.Tags;
import org.braun.digikam.backend.model.Keyword;

/**
 *
 * @author mbraun
 */
public class NodeFactory {

    private static final Logger LOG = LogManager.getLogger();
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = LOCK.readLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();
    private List<Node> nodes;

    private static final NodeFactory INSTANCE = new NodeFactory();

    private NodeFactory() {
    }

    public static NodeFactory getInstance() {
        return INSTANCE;
    }

    public void refresh(List<Tags> tags) {
        try {
            WRITE_LOCK.lock();
            Node root = new Node(0, "");
            root.setFullName("");
            root.setQualifiedName("");
            Map<Long, Node> map = new HashMap<>();
            map.put(0l, root);

            List<Tags> list = new ArrayList<>();

            for (Tags t : tags) {
                if (t.getPid() == 1 || t.getPid() == -1
                        || "_Digikam_Internal_Tags_".equals(t.getName()) || "EXIF".equals(t.getName())) {
                    continue;
                }
                map.put(t.getId(), new Node(t.getId(), t.getName()));
                list.add(t);
            }

            for (Tags t : list) {
                Node parent = map.get(t.getPid());
                Node child = map.get(t.getId());
                child.setParent(parent);
                parent.getChildren().add(child);
            }

            nodes = new ArrayList(map.values());
            initFullNames(root);
            initQualifiedNames();
        } catch (Exception e) {
            LOG.error("Aquiring WriteLock failed", e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }
    
    public List<Node> list() {
        try {
            READ_LOCK.lock();
            return nodes;
        } finally {
            READ_LOCK.unlock();
        }
    }

    private void initFullNames(Node p) {
        p.setFullName("");
        for (Node c : p.getChildren()) {
            initFullNames(c, p.getFullName());
        }
    }
    private void initFullNames(Node p, String prefix) {
        p.setFullName(prefix + "/" + p.getName());
        for (Node c : p.getChildren()) {
            initFullNames(c, p.getFullName());
        }
    }

    private void initQualifiedNames() {
        Collections.sort(nodes, (Node n1, Node n2) -> n1.getName().compareTo(n2.getName()));
        for (Node n : nodes) {
            if (n.getParent() == null || n.getQualifiedName() != null) {
                continue;
            }

            List<Node> siblings = getNodeByName(n);
            if (siblings.size() == 1) {
                n.setQualifiedName(n.getName());
            } else {
                for (Node s : siblings) {
                    s.setQualifiedName(s.getParent().getName() + "/" + s.getName());
                }
            }
        }
    }

    private List<Node> getNodeByName(Node curNode) {
        List<Node> result = new ArrayList<>();
        for (Node n : nodes) {
            int res = n.getName().compareTo(curNode.getName());
            if (res > 0) {
                break;
            } else if (res == 0) {
                result.add(n);
            }
        }
        return result;
    }

    public List<Keyword> getKeywordByName(String name) {
        List<Keyword> result = new ArrayList<>();
        try {
            READ_LOCK.lock();
            if (name == null || name.isEmpty()) {
                for (Node n : nodes) {
                    Keyword k = new Keyword();
                    k.setId(n.getId());
                    k.setName(n.getQualifiedName());
                    k.setFullName(n.getFullName());
                    result.add(k);
                }
            } else {
                name = name.toLowerCase();
                for (Node n : nodes) {
                    if (n.getName().toLowerCase().contains(name)) {
                        Keyword k = new Keyword();
                        k.setId(n.getId());
                        k.setName(n.getQualifiedName());
                        result.add(k);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Aquiring ReadLock failed", e);
        } finally {
            READ_LOCK.unlock();
        }
        return result;
    }

    public Keyword getKeywordById(long id) {
        for (Node n : nodes) {
            if (id == n.getId()) {
                return new Keyword().fullName(n.getFullName()).id(n.getId()).name(n.getQualifiedName());
            }
        }
        return null;
    }
    
    public String getKeywordQualById(long id) {
        for (Node n : nodes) {
            if (id == n.getId()) {
                return n.getQualifiedName();
            }
        }
        return null;
    }
    
    public List<Long> getChildrensRec(long id) {
        for (Node n : nodes) {
            if (id == n.getId()) {
                List<Long> result = new ArrayList<>();
                getChildren(result, n);
                return result;
            }
        }
        return Collections.emptyList();
    }
    
    private void getChildren(List<Long> children, Node parent) {
        children.add(parent.getId());
        for (Node c : parent.getChildren()) {
            getChildren(children, c);
        }
    }
}
