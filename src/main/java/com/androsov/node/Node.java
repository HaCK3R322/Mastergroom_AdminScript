package com.androsov.node;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Node {
    @Expose
    @Getter @Setter private Integer id;
    @Expose
    @Getter @Setter String phrase;
    @Getter @Setter private List<NodePseudonym> children;

    public Node(String phrase) {
        this.id = null;
        this.phrase = phrase;
        this.children = new ArrayList<>();
    }

    public Node(Integer id, String phrase) {
        this.id = id;
        this.phrase = phrase;
        this.children = new ArrayList<>();
    }

    public void addChild(Node node, String pseudonym) {
        children.add(new NodePseudonym(node, pseudonym));
    }

    public void removeChildById(Integer id) {
        children.removeIf(nodePseudonym -> nodePseudonym.getNode().getId().equals(id));
    }

    public void removeChild(NodePseudonym nodePseudonym) {
        children.remove(nodePseudonym);
    }

    public NodePseudonym getChildPseudonymByPhrase(String pseudonym) {
        return children.stream()
                .filter(nodePseudonym -> nodePseudonym.getPhrase().equals(pseudonym))
                .findFirst()
                .get();
    }

    @Override
    public String toString() {
        return "{\"id\":" + id + ", \"phrase\":\"" + phrase + "\"}";
    }
}
