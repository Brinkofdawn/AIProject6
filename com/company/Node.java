package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Arun on 2/24/2016.
 */
public class Node {

    private int DEBUG = 0;
    private String name;
    private ArrayList<String> parents;
    private ArrayList<Double> prob;
    private boolean needsTruth = false;

    public String getName() {
        return name;
    }

    public ArrayList<String> getParents() {
        return parents;
    }

    public ArrayList<Double> getProb() {
        return prob;
    }

    public boolean isNeedsTruth() {
        return needsTruth;
    }

    public String[][] getTruthTable() {
        return truthTable;
    }

    private String[][] truthTable;

    public String getStatus() {
        return status;
    }
    public String getTempStatus() {
        return tempStatus;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setTempStatus(String status) {
        this.tempStatus = status;
    }

    private String status;

    private String tempStatus;

    static double weight = 1;


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

    public void rejection(int sample, ArrayList<Node> BayesNet){

        double numTrue = 0;
        double numFalse = 0;
        for(int i= 0; i<sample;i++) {
            int k = rejectionSampling(BayesNet);
            if (k == 1) {
                numTrue++;
                //System.out.println("TRUE");
            } else if (k == 0) {
                numFalse++;
                //System.out.println("FALSE");

            }
            resetStatus(BayesNet);
        }
        System.out.println("Rejection Sampling");
        System.out.println("Total # of Samples: " + sample+ " # of accepted samples: " + (numFalse+numTrue));
        System.out.println("True samples: " + numTrue + " False samples: " + numFalse);

        System.out.println("True %: " + numTrue/(numFalse+numTrue) + " False %: " + numFalse/(numFalse+numTrue)+"\n");

    }

    public void weighted(int sample, ArrayList<Node> BayesNet){
        int numTrue = 0;
        double trueWeight = 0;
        int numFalse = 0;
        double falseWeight = 0;

        for(int i= 0; i<sample;i++) {
            double k = weightedSampling(BayesNet);
            if (k >0) {
                numTrue++;
                trueWeight = trueWeight+ k;
                //System.out.println("TRUE");
            } else if (k < 0) {
                numFalse++;
                falseWeight = falseWeight- k;
                //System.out.println("FALSE");

            }
            resetStatus(BayesNet);
            weight = 1;
        }
        System.out.println("Likelihood-weighting Sampling");
        System.out.println("Total # of Samples: " + sample+ " # of accepted samples: " + (numFalse+numTrue));
        System.out.println("Total weight of true samples: " + trueWeight + " Total weight of false samples: " + falseWeight);
        System.out.println("True %: " + trueWeight/(falseWeight+trueWeight) + " False%: " + falseWeight/(falseWeight+trueWeight));

    }

    public double weightedSampling(ArrayList<Node> BayesNet){
        int numParents = parents.size();
        Random rndNumbers = new Random();


        if(status.equals("t")||status.equals("f")){
            // keep the truth value
            if (numParents == 0) {
                if (tempStatus.equals("t")) {
                    weight = weight * prob.get(0);
                }
                else if (tempStatus.equals("f")) {
                    weight = weight * (1 -prob.get(0));
                }

            }

            else{
                ArrayList<Node> par = new ArrayList<Node>();
                for (int i = 0; i < parents.size(); i++){
                    for (int j =0; j< BayesNet.size(); j++){
                        if(parents.get(i).equals(BayesNet.get(j).getName())){
                            par.add(BayesNet.get(j));
                            break;
                        }
                    }
                }

                for(int i = 0; i< par.size(); i++){
                    par.get(i).weightedSampling(BayesNet);
                }
                int temp1 = 0;
                for (int i = 0; i< truthTable.length; i++){
                    for(int j = 0; j < par.size(); j++){
                        if (truthTable[i][j].equals(par.get(j).getTempStatus())){

                        }
                        else {
                            break;
                        }

                        if(j == par.size()-1){
                            temp1 = 1;

                            if (tempStatus.equals("f")) {
                                weight = weight*(1-prob.get(i));
                            }
                            else if (tempStatus.equals("t")){
                                weight = weight*(prob.get(i));
                            }


                        }
                    }
                    if (temp1 == 1){
                        break;
                    }
                }


            }
        }

        else if(status.equals("-")) {
            // if it is a top level node
            if (numParents == 0) {
                double assign = rndNumbers.nextFloat();
                if (assign > prob.get(0)) {
                    tempStatus = "f";
                }
                else {
                    tempStatus = "t";
                }
            }

            else{
                // construct parent object list
                ArrayList<Node> par = new ArrayList<Node>();
                for (int i = 0; i < parents.size(); i++){
                    for (int j =0; j< BayesNet.size(); j++){
                        if(parents.get(i).equals(BayesNet.get(j).getName())){
                            par.add(BayesNet.get(j));
                            break;
                        }
                    }
                }

                for(int i = 0; i< par.size(); i++){

                    par.get(i).weightedSampling(BayesNet);
                }
                int temp1 = 0;
                for (int i = 0; i< truthTable.length; i++){
                    for(int j = 0; j < par.size(); j++){
                        if (truthTable[i][j].equals(par.get(j).getTempStatus())){

                        }
                        else {
                            break;
                        }

                        if(j == par.size()-1){
                            temp1 = 1;
                            double tempProb = getProb().get(i);
                            double assign = rndNumbers.nextFloat();
                            //System.out.println(assign);
                            if (assign > tempProb) {
                                tempStatus = "f";
                            }
                            else {
                                tempStatus = "t";
                            }


                        }
                    }
                    if (temp1 == 1){
                        break;
                    }
                }


            }
        }


        else if(status.equals("?")||status.equals("q")) {
            // if it is a top level node
            if (numParents == 0) {
                double assign = rndNumbers.nextFloat();
                // System.out.println(assign);
                if (assign > prob.get(0)) {
                    tempStatus = "f";
                }
                else {
                    tempStatus = "t";
                }
            }

            else{
                // construct parent object list
                ArrayList<Node> par = new ArrayList<Node>();
                for (int i = 0; i < parents.size(); i++){
                    for (int j =0; j< BayesNet.size(); j++){
                        if(parents.get(i).equals(BayesNet.get(j).getName())){
                            par.add(BayesNet.get(j));
                            break;
                        }
                    }
                }

                for(int i = 0; i< par.size(); i++){
                    par.get(i).weightedSampling(BayesNet);
                }
                int temp1 = 0;
                for (int i = 0; i< truthTable.length; i++){
                    for(int j = 0; j < par.size(); j++){
                        if (truthTable[i][j].equals(par.get(j).getTempStatus())){

                        }
                        else {
                            break;
                        }

                        if(j == par.size()-1){
                            temp1 = 1;
                            double tempProb = getProb().get(i);
                            double assign = rndNumbers.nextFloat();
                           // System.out.println(assign);

                            // In the case of weighted sampling this is unnecessary, however if we are returning -1 it means something is wrong with the rest of the function
                           /*
                            for(int k =0; k < BayesNet.size();k++){
                                if(BayesNet.get(k).getStatus().equals("f")|| BayesNet.get(k).getStatus().equals("t")){
                                    if(BayesNet.get(k).getStatus().equals(BayesNet.get(k).getTempStatus())){

                                    }
                                    else{
                                        return -1;
                                    }
                                }
                            }
                            */

                            if (assign > tempProb) {
                                tempStatus = "f";
                                return - weight;
                            }
                            else {
                                tempStatus = "t";
                                return weight;
                            }


                        }
                    }
                    if (temp1 == 1){
                        break;
                    }
                }


            }
        }
        return 0;

    }

    public int rejectionSampling(ArrayList<Node> BayesNet){
        int numParents = parents.size();
        Random rndNumbers = new Random();
        if(tempStatus.equals("-")||tempStatus.equals("t")||tempStatus.equals("f")) {
            // if it is a top level node
            if (numParents == 0) {
                double assign = rndNumbers.nextFloat();
               // System.out.println(assign);
                if (assign > prob.get(0)) {
                    tempStatus = "f";
                }
                else {
                    tempStatus = "t";
                }
            }

            else{
                // construct parent object list
                ArrayList<Node> par = new ArrayList<Node>();
                for (int i = 0; i < parents.size(); i++){
                    for (int j =0; j< BayesNet.size(); j++){
                        if(parents.get(i).equals(BayesNet.get(j).getName())){
                            par.add(BayesNet.get(j));
                            break;
                        }
                    }
                }

                for(int i = 0; i< par.size(); i++){

                        par.get(i).rejectionSampling(BayesNet);
                }
                int temp1 = 0;
                for (int i = 0; i< truthTable.length; i++){
                    for(int j = 0; j < par.size(); j++){
                        if (truthTable[i][j].equals(par.get(j).getTempStatus())){

                        }
                        else {
                            break;
                        }

                        if(j == par.size()-1){
                            temp1 = 1;
                            double tempProb = getProb().get(i);
                            double assign = rndNumbers.nextFloat();
                            //System.out.println(assign);
                            if (assign > tempProb) {
                                tempStatus = "f";
                            }
                            else {
                                tempStatus = "t";
                            }


                        }
                    }
                    if (temp1 == 1){
                        break;
                    }
                }


            }
        }

       else if(tempStatus.equals("?")||tempStatus.equals("q")) {
            // if it is a top level node
            if (numParents == 0) {
                double assign = rndNumbers.nextFloat();
               // System.out.println(assign);
                if (assign > prob.get(0)) {
                    tempStatus = "f";
                }
                else {
                    tempStatus = "t";
                }
            }

            else{
                // construct parent object list
                ArrayList<Node> par = new ArrayList<Node>();
                for (int i = 0; i < parents.size(); i++){
                    for (int j =0; j< BayesNet.size(); j++){
                        if(parents.get(i).equals(BayesNet.get(j).getName())){
                            par.add(BayesNet.get(j));
                            break;
                        }
                    }
                }

                for(int i = 0; i< par.size(); i++){
                        par.get(i).rejectionSampling(BayesNet);
                }
                int temp1 = 0;
                for (int i = 0; i< truthTable.length; i++){
                    for(int j = 0; j < par.size(); j++){
                        if (truthTable[i][j].equals(par.get(j).getTempStatus())){

                        }
                        else {
                            break;
                        }

                        if(j == par.size()-1){
                            temp1 = 1;
                            double tempProb = getProb().get(i);
                            double assign = rndNumbers.nextFloat();
                            //System.out.println(assign);

                            for(int k =0; k < BayesNet.size();k++){
                                if(BayesNet.get(k).getStatus().equals("f")|| BayesNet.get(k).getStatus().equals("t")){
                                    if(BayesNet.get(k).getStatus().equals(BayesNet.get(k).getTempStatus())){

                                    }
                                    else{
                                        return -1;
                                    }
                                }
                            }
                            if (assign > tempProb) {
                                tempStatus = "f";
                                return 0;
                            }
                            else {
                                tempStatus = "t";
                                return 1;
                            }


                        }
                    }
                    if (temp1 == 1){
                        break;
                    }
                }


            }
        }
    return -1;

    }

    public void resetStatus(ArrayList<Node> BayesNet) {
        for(int i =0; i<BayesNet.size();i++) {
            BayesNet.get(i).setTempStatus(BayesNet.get(i).getStatus());
        }
    }

    public void printData(){
        System.out.println(name);
        System.out.println(status);
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
