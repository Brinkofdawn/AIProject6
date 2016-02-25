package com.company;

import java.util.ArrayList;

/**
 * Created by Arun on 2/24/2016.
 */
public class Node {
    String name;
    ArrayList<String> parents;
    ArrayList<Double> prob;

    public Node(String name, ArrayList<String> parents, ArrayList<Double> prob){
        this.name = name;
        this.parents = parents;
        this.prob = prob;
    }

    public void printData(){
        System.out.println(name);
        for (int i = 0; i < parents.size(); i++) {
            System.out.print(parents.get(i).toString());
            System.out.print(" ");
        }
        System.out.println(" ");

        for (int i = 0; i < prob.size(); i++) {
            System.out.print(prob.get(i).toString());
            System.out.print(" ");
        }
        System.out.println(" ");
        System.out.println(" ");
    }
}
