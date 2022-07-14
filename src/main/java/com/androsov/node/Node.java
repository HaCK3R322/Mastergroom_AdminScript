package com.androsov.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
public class Node {
    @Getter @Setter private Integer id;
    @Getter @Setter String phrase;
    @Getter @Setter private List<NodePseudonym> children;

    public void addChildren(Node node, String pseudonym) {
        children.add(new NodePseudonym(node, pseudonym));
    }

    public void removeChildById(Integer id) {
        children.removeIf(nodePseudonym -> nodePseudonym.getNode().getId().equals(id));
    }

    public NodePseudonym getChildPseudonymByPhrase(String pseudonym) {
        return children.stream()
                .filter(nodePseudonym -> nodePseudonym.getPhrase().equals(pseudonym))
                .findFirst()
                .get();
    }
}
