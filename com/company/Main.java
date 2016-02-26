package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	// write your code here
        if (args.length != 2) {
            System.out.println("Incorrect Input");
            System.exit(1);
        }
        BufferedReader br = null;

        String inFile = args[0];
        ArrayList<Node> BayesNet  = new ArrayList<Node>();
        String thisLine;

        try{
            // open input stream test.txt for reading purpose.
           br = new BufferedReader(new FileReader(inFile));
            while ((thisLine = br.readLine()) != null) {
                String[] tempArray = thisLine.split("\\[");
                String[] nodeName = tempArray[0].split(":|\\s");
                String[] parentsarray = tempArray[1].split("\\s|]");
                ArrayList<String> parents = new ArrayList<String>(Arrays.asList(parentsarray));
                String[] probsName = tempArray[2].split("\\s|]");
                Double[] numbers = new Double[probsName.length];

                for(int i = 0;i < probsName.length;i++) {
                    numbers[i] = Double.parseDouble(probsName[i]);
                }
                ArrayList<Double> probs = new ArrayList<Double>(Arrays.asList(numbers));
                Node tempNode = new Node(nodeName[0], parents, probs);
                BayesNet.add(tempNode);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }



    }

    }

