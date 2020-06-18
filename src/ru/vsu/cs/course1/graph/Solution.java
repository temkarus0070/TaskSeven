package ru.vsu.cs.course1.graph;

import java.lang.reflect.Array;
import java.util.*;

public class Solution {

    private static boolean[] used;
    private static boolean[] notUsed;
    private static List<Integer>[]parents;



    public static Graph pathForBook(int bookOwner, Graph friendlyGraph) {
        Graph newGraph = new AdjMatrixGraph();
        boolean[] visited = new boolean[friendlyGraph.vertexCount()];
        ArrayList<Integer> path = new ArrayList<>();
        getHamilton(friendlyGraph,bookOwner,visited,path);
        for(int i=0;i<path.size()-1;i++) {
            int v = path.get(i);
            int u = path.get(i+1);
            newGraph.addAdge(v,u);
        }
        newGraph.addAdge(path.get(path.size()-1),bookOwner);
        return newGraph;
    }



    private static boolean getHamilton(Graph graph, int v, boolean[] visited,ArrayList<Integer> path){
        path.add(v);
        if(path.size()== graph.vertexCount()) {
            if (hasEdge(path.get(0), path.get(path.size() - 1), graph))
                return true;
            else {
                path.remove(path.size() - 1);
                return false;
            }
        }
        visited[v] = true;
        for(int i=0;i<graph.vertexCount();i++){
            if(hasEdge(v,i,graph) && !visited[i])
                if(getHamilton(graph,i,visited,path))
                    return true;

        }
        visited[v]=false;
        path.remove(path.size()-1);
        return false;
    }



    private static boolean hasEdge(int v,int u,Graph graph){
        for(Iterator<Integer> iterator = graph.adjacencies(v).iterator();iterator.hasNext();){
            int obj = iterator.next();
            if(obj == u)
                return true;
        }
        return false;
    }

    public static Graph groupingWithEnemy(Graph enemyGraph, int s,int p){
        Graph newGraph = new AdjMatrixGraph();
        Graph copyGraph = copyGraph(enemyGraph);
        ArrayList<Integer> list = new ArrayList<>();
        int[] enemyCount = new int[enemyGraph.vertexCount()];
        HashMap<Integer,ArrayList<Integer>> vertex= new HashMap<>();
        for(int i =0;i<enemyGraph.vertexCount();i++)
            list.add(i);


        for(int i=0;i<s;i++){
            vertex.put(i,new ArrayList<>());
        }

        while (list.size()>0){
            int v = list.get(0);
           // newGraph.enemyGraph.adjacencies(v).iterator().next()
        }
        return null;
    }

    public static Graph copyGraph(Graph graph){
        Graph newGraph = new AdjMatrixGraph();
        for(int i=0;i<graph.vertexCount();i++){
            for(Iterator<Integer> iterator =  graph.adjacencies(i).iterator();iterator.hasNext();){
                newGraph.addAdge(i,iterator.next());
            }
        }
        return newGraph;
    }





}
