package com.androsov;

import com.androsov.node.Node;
import com.androsov.node.NodePseudonym;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    static void printNode(Node node) {
        System.out.println(node.getPhrase());
        int i = 1;
        for (NodePseudonym nodePseudonym : node.getChildren()) {
            System.out.println(Integer.toString(i) + ": " +  nodePseudonym.getPhrase());
            i++;
        }
    }

    public static void main(String[] args) {
        Node startNode = new Node(1, "Здравствуйте! Это зоосалон mastergroom. Хотите записаться?", new ArrayList<>());
        Node middleNode = new Node(2, "а нам похуй", new ArrayList<>());
        Node endNode = new Node(3, "Досвидания!", new ArrayList<>());

        startNode.addChildren(middleNode, "Da");
        startNode.addChildren(endNode, "Net");

        middleNode.addChildren(startNode, "Again!");
        middleNode.addChildren(endNode, "Idi nahuy");

        Scanner scanner = new Scanner(System.in);

        Node currentNode = startNode;
        while (true){
            printNode(currentNode);

            if(currentNode == endNode) {
                break;
            }

            String input = scanner.nextLine();
            try {
                currentNode = currentNode.getChildPseudonymByPhrase(input).getNode();
            } catch (NoSuchElementException e) {
                System.out.println("Нет такого варианта");
            }
        }
    }
}
