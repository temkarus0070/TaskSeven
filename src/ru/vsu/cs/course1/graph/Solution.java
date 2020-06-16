package ru.vsu.cs.course1.graph;

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


    private static void getPath(Stack<Integer> stack,int v, Graph graph){
        for(int i=0;i<graph.vertexCount();i++){
            if(hasEdge(v,i,graph)){
                graph.removeAdge(v,i);
                getPath(stack,i,graph);
            }
        }

        stack.push(v);
    }

    private static boolean hasEdge(int v,int u,Graph graph){
        for(Iterator<Integer> iterator = graph.adjacencies(v).iterator();iterator.hasNext();){
            int obj = iterator.next();
            if(obj == u)
                return true;
        }
        return false;
    }

    private static Integer intersection(Graph graph, int v){
        Integer num = null;
        for(Iterator<Integer> iter = graph.adjacencies(v).iterator();iter.hasNext();)
        {
            int n = iter.next();
            if(notUsed[n])
            {
                num = n;
                return num;
            }
        }
        return num;
    }
}
