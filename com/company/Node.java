package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Arun on 2/24/2016.
 */
public class Node {
    int DEBUG = 0;
    String name;
    ArrayList<String> parents;
    ArrayList<Double> prob;
    boolean needsTruth = false;
    String[][] truthTable;

    public Node(String name, ArrayList<String> parents, ArrayList<Double> prob){
        this.name = name;
        this.parents = parents;
        this.prob = prob;

        if (parents.size() > 0){
            this.needsTruth = true;
            this.truthTable= new String[prob.size()][parents.size()];
            if (DEBUG == 1){
                printData();
            }
            initializeTruth();
        }
    }



    public void initializeTruth(){
        int x = prob.size();
        int y = parents.size();

        for (int i = 0; i < x; i++){
            String binary = Integer.toBinaryString(i);
            String flipped = new StringBuffer(binary).reverse().toString();
            int check = y - flipped.length();

            for (int a = 0; a < check; a++){
                flipped= flipped.concat("0");
            }
            for (int b = 0; b<flipped.length(); b++){
                if (flipped.charAt(b) == '1'){
                    truthTable[i][b] = "t";
                    if (DEBUG == 1) {
                        System.out.print("t ");
                    }
                }
                else if (flipped.charAt(b) == '0'){
                    truthTable[i][b] = "f";
                    if (DEBUG == 1) {
                        System.out.print("f ");
                    }
                }

                else{
                    System.out.print("Something went wrong, exiting program....");
                    System.exit(0);
                }
            }
            if (DEBUG == 1) {
                System.out.println("\n");
            }
        }

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
        System.out.println("\n");
    }
}
