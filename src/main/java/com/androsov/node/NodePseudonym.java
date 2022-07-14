package com.androsov.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class NodePseudonym {
    @Getter @Setter private Node node;
    @Getter @Setter private String phrase;
}
