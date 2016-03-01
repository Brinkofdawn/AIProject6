package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	// write your code here

        if (args.length != 3) {
            System.out.println("Incorrect Input");
            System.exit(1);
        }
        BufferedReader br = null;

        String inFile = args[0];
        String status = args[1];
        int samples = Integer.parseInt(args[2]);
        ArrayList<Node> BayesNet  = new ArrayList<Node>();
        String thisLine;

        // reading in network file
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

        // reading in query file
        try{
            // open input stream test.txt for reading purpose.
            br = new BufferedReader(new FileReader(status));
            while ((thisLine = br.readLine()) != null) {
                String[] tempArray = thisLine.split(",");
                for(int i = 0; i < tempArray.length; i++) {
                    BayesNet.get(i).setStatus(tempArray[i]);
                    BayesNet.get(i).setTempStatus(tempArray[i]);
                    // BayesNet.get(i).printData();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        // run rejection sampling
        for (int i = 0; i< BayesNet.size(); i++ ){
            if (BayesNet.get(i).getStatus().equals("q")|| BayesNet.get(i).getStatus().equals("?")){
                BayesNet.get(i).rejection(samples, BayesNet);
                break;
            }
        }

        // run weighted sampling
        for (int i = 0; i< BayesNet.size(); i++ ){
            if (BayesNet.get(i).getStatus().equals("q")|| BayesNet.get(i).getStatus().equals("?")){
                BayesNet.get(i).weighted(samples, BayesNet);
                break;
            }
        }
    }

}

