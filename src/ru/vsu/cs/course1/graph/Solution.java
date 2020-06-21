package ru.vsu.cs.course1.graph;

import java.lang.reflect.Array;
import java.util.*;

public class Solution {

    private static boolean[] used;
    private static boolean[] notUsed;
    private static List<Integer>[] parents;


    public static Graph pathForBook(int bookOwner, Graph friendlyGraph) {
        Graph newGraph = new AdjMatrixGraph();
        boolean[] visited = new boolean[friendlyGraph.vertexCount()];
/*        ArrayList<Integer> path = new ArrayList<>();
        getHamilton(friendlyGraph, bookOwner, visited, path);
        for (int i = 0; i < path.size() - 1; i++) {
            int v = path.get(i);
            int u = path.get(i + 1);
            newGraph.addAdge(v, u);
        }
        newGraph.addAdge(path.get(path.size() - 1), bookOwner);*/



        Graph newGraphfromStack = new AdjMatrixGraph();
        Stack<Integer> stackPath = new Stack<>();
        getHamiltonStack(friendlyGraph,bookOwner,visited,stackPath);
        int oneElement = stackPath.pop();
        int lastVertex = oneElement;

        HashMap<Edge,Byte> edgeMap = new HashMap<>();

        boolean first = true;
        while (!stackPath.empty()){
            if (first) {
                int twoElement = stackPath.pop();
                first = !first;
                edgeMap.put(new Edge(oneElement,twoElement), (byte) 0);
                newGraphfromStack.addAdge(oneElement,twoElement);
                oneElement = twoElement;
            }
            else{
                int twoElement = stackPath.pop();
                newGraphfromStack.addAdge(oneElement,twoElement);
                edgeMap.put(new Edge(oneElement,twoElement), (byte) 0);
                oneElement = twoElement;
            }

            }

        newGraphfromStack.addAdge(lastVertex,bookOwner);
        edgeMap.put(new Edge(lastVertex,bookOwner), (byte) 0);
        return newGraphfromStack;




   //     return newGraph;
    }



    public static  HashMap<Edge,Byte> pathWithHashForBook(int bookOwner, Graph friendlyGraph) {
        Graph newGraph = new AdjMatrixGraph();
        boolean[] visited = new boolean[friendlyGraph.vertexCount()];
/*        ArrayList<Integer> path = new ArrayList<>();
        getHamilton(friendlyGraph, bookOwner, visited, path);
        for (int i = 0; i < path.size() - 1; i++) {
            int v = path.get(i);
            int u = path.get(i + 1);
            newGraph.addAdge(v, u);
        }
        newGraph.addAdge(path.get(path.size() - 1), bookOwner);*/



        Graph newGraphfromStack = new AdjMatrixGraph();
        Stack<Integer> stackPath = new Stack<>();
        getHamiltonStack(friendlyGraph,bookOwner,visited,stackPath);
        int oneElement = stackPath.pop();
        int lastVertex = oneElement;

        HashMap<Edge,Byte> edgeMap = new HashMap<>();

        boolean first = true;
        while (!stackPath.empty()){
            if (first) {
                int twoElement = stackPath.pop();
                first = !first;
                edgeMap.put(new Edge(oneElement,twoElement), (byte) 0);
                newGraphfromStack.addAdge(oneElement,twoElement);
                oneElement = twoElement;
            }
            else{
                int twoElement = stackPath.pop();
                newGraphfromStack.addAdge(oneElement,twoElement);
                edgeMap.put(new Edge(oneElement,twoElement), (byte) 0);
                oneElement = twoElement;
            }

        }

        newGraphfromStack.addAdge(lastVertex,bookOwner);
        edgeMap.put(new Edge(lastVertex,bookOwner), (byte) 0);
        return edgeMap;

    }


    private static boolean getHamilton(Graph graph, int v, boolean[] visited, ArrayList<Integer> path) {
        path.add(v);
        if (path.size() == graph.vertexCount()) {
            if (hasEdge(path.get(0), path.get(path.size() - 1), graph))
                return true;
            else {
                path.remove(path.size() - 1);
                return false;
            }


        }
        visited[v] = true;
        for (int i = 0; i < graph.vertexCount(); i++) {
            if (hasEdge(v, i, graph) && !visited[i])
                if (getHamilton(graph, i, visited, path))
                    return true;

        }
        visited[v] = false;
        path.remove(path.size() - 1);
        return false;
    }

    private static boolean getHamiltonStack(Graph graph, int v, boolean[] visited, Stack<Integer> path) {
        path.push(v);
        if (path.size() == graph.vertexCount()) {
            int u = path.pop();
            if (hasEdge(path.firstElement(), u, graph)) {
                path.push(u);
                return true;
            }
            else {
                return false;
            }

        }
        visited[v] = true;
        for (int i = 0; i < graph.vertexCount(); i++) {
            if (hasEdge(v, i, graph) && !visited[i])
                if (getHamiltonStack(graph, i, visited, path))
                    return true;

        }
        visited[v] = false;
        path.pop();
        return false;
    }


    private static boolean hasEdge(int v, int u, Graph graph) {
        for (Iterator<Integer> iterator = graph.adjacencies(v).iterator(); iterator.hasNext(); ) {
            int obj = iterator.next();
            if (obj == u)
                return true;
        }
        return false;
    }

    public static Graph groupingWithEnemy(Graph enemyGraph, int s, int p) {
        Graph newGraph = new AdjMatrixGraph();
        Graph copyGraph = copyGraph(enemyGraph);
        ArrayList<Integer> list = new ArrayList<>();
        int[] enemyCount = new int[enemyGraph.vertexCount()];
        HashMap<Integer, ArrayList<Integer>> vertex = new HashMap<>();
        for (int i = 0; i < enemyGraph.vertexCount(); i++) {
            list.add(i);
        }


        for (int i = 0; i < s; i++) {
            vertex.put(i, new ArrayList<>());
            list.remove(i);
        }
        while (list.size() > 0) {
            for (Integer element : list) {
                for (Iterator<Integer> iterator = vertex.keySet().iterator(); iterator.hasNext(); ) {
                    int u = iterator.next();

                    if (checkOneEnemy(enemyGraph, element, p, u, enemyCount)) {
                        ArrayList<Integer> listVertex = vertex.get(u);
                        listVertex.add(element);
                        list.remove(element);
                        continue;

                    }


                }
            }
        }
        return null;
    }

    public static ArrayList<Group> groupingUsers(Graph graph,int s,int p){
        ArrayList<Group> groups;
        int[]combArray = new int[s];
        LinkedList<Integer> linkedList = new LinkedList<>();
        for(int i = 0;i<graph.vertexCount();i++) {
            if(i>=s)
                linkedList.add(i);
        }

        for(int i=0;i<s;i++)
            combArray[i]=i;

        do
        {
            TreeSet<Integer> banned = new TreeSet<>();
            groups = new ArrayList<>();
            for(int i=0;i<s;i++){
                Group group = new Group();
                group.addFirstToGroup(combArray[i]);
                groups.add(group);
            }
            LinkedList<Integer> usedList = (LinkedList<Integer>) linkedList.clone();
            while (banned.size()!=s && usedList.size()!=0){
                for(int i=0;i<s;i++){
                    if(!(banned.contains(i)) && usedList.size()!=0)
                        groups.get(i).addToGroup(usedList.pop(),graph);
                    if(hasManyEnemies(groups.get(i),p)){
                        banned.add(i);
                    }
                }
            }
            if(usedList.size()==0)
                return groups;
            addArrayToList(combArray,linkedList);
        }
        while (nextCombinations(combArray,s));
        return null;


    }

    public static boolean hasManyEnemies(Group group,int p ){
        return group.getMaxEnemies()>=p;
    }

    public static void addArrayToList(int[]array,LinkedList<Integer> linkedList){
        for(int i=0;i<array.length;i++)
            linkedList.add(array[i]);
    }


    public static boolean nextCombinations(int[] combArray, int n){
        int k = combArray.length;
        for (int i=k-1; i>=0; --i)
            if (combArray[i] < n-k+i+1) {
                ++combArray[i];
                for (int j=i+1; j<k; ++j)
                    combArray[j] = combArray[j-1]+1;
                return true;
            }
        return false;
    }




    public static boolean checkEnemy(Graph graph, int v, int count, ArrayList<Integer> list, int[] enemies) {
        for (Integer element : list) {
            if (hasEdge(element, v, graph) && (enemies[element] + 1) > count)
                return false;
        }
        return true;
    }


    public static boolean checkOneEnemy(Graph graph, int v, int count, int u, int[] enemies) {
        if (hasEdge(v, u, graph) && (enemies[u] + 1) > count)
            return false;
        return true;
    }

    public static Graph copyGraph(Graph graph) {
        Graph newGraph = new AdjMatrixGraph();
        for (int i = 0; i < graph.vertexCount(); i++) {
            for (Iterator<Integer> iterator = graph.adjacencies(i).iterator(); iterator.hasNext(); ) {
                newGraph.addAdge(i, iterator.next());
            }
        }
        return newGraph;
    }


}
