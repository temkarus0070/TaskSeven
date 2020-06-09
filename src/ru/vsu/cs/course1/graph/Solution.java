package ru.vsu.cs.course1.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Solution {

    private static boolean[] used;
    private static Integer[]parents;

    public static Graph pathForBook(int bookOwner, Graph friendlyGraph){
        Graph newGraph = new AdjMatrixGraph();
        Graph newGraph1 = new AdjListsGraph();
        used = new boolean[friendlyGraph.vertexCount()];
        parents = new Integer[friendlyGraph.vertexCount()];
        CycleData data = hasCycle(friendlyGraph,bookOwner,-1,bookOwner);
        if(data.hasCycle)
        {
            for(int i=parents.length-1;i>=0;i--){
                if(parents[i]!=null)
                    newGraph.addAdge(i,parents[i]);
            }
            newGraph.addAdge(data.nodeNum,bookOwner);
            return newGraph;
        }
        return null;
    }

    private static CycleData hasCycle(Graph graph,int v,int p, int owner){
        used[v] = true;
        for(int u:graph.adjacencies(v)){
            if(!used[u]){
                parents[u] = v;
                hasCycle(graph,u,v,owner);
                /*CycleData data =
                if (data.hasCycle && checkParents(parents,graph.vertexCount()) && checkBookOwner(graph.adjacencies(data.nodeNum),owner))
                    return data;*/
            }
            else if(u!=p){
                if(checkParents(parents,graph.vertexCount()) && checkBookOwner(graph.adjacencies(u),owner)) {
                    System.out.println(String.format("graph has cycle %d  %d %d", u ,p,v));
                    return new CycleData(true,u);
                }
            }
        }
        return new CycleData(false,0);

    }

    private static boolean checkParents(Integer[] array,int countNodes){
        int count = 0;
        for(Integer element:array){
            if(element != null)
                count++;
        }
        if(countNodes - 1 == count ){
            return true;
        }
        else
            return false;
    }

    private static boolean checkBookOwner(Iterable<Integer> vertes,int n){
        for(Iterator iterator = vertes.iterator();iterator.hasNext();)
        {
            Integer node = (Integer)iterator.next();
            if(node.equals(n))
                return true;

        }
        return false;
    }
}
