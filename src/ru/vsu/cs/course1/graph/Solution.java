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
                    boolean thisBanned = true;
                    if(!(banned.contains(i)) && usedList.size()!=0 && p!=0) {
                        thisBanned=false;
                        groups.get(i).addToGroup(usedList.pop(), graph);
                    }

                    if(hasManyEnemies(groups.get(i),p)){
                        banned.add(i);
                    }
                    if(!thisBanned  && usedList.size()!=0){
                        int user = usedList.pop();
                        if(groups.get(i).canAdd(user,graph,p)){
                            groups.get(i).addToGroup(user,graph);
                        }
                        else usedList.add(user);
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


    public static Graph[] generator(int n,int k){
        Graph[] graphs = new Graph[2];
        Graph friendlyGraph = new AdjMatrixGraph();
        Graph enemyGraph = new AdjMatrixGraph();
        HashMap<Integer,Integer> enemiesCountMap = new HashMap<>();
        List<int[]> sets = new ArrayList<>();
        for(int i=0;i<n;i += 2){
            int[] array = new int[2];
            for(int j=0;j<2;j++){
                enemiesCountMap.put(i,0);
                enemiesCountMap.put(i+1,0);

                array[0] = i;
                array[1] = i+1;
            }
            sets.add(array);
        }

        for(int i=0;i<n/2;i++){
            int plus = 2;
            int[] array  =  sets.get(i);
            int one = array[0];
            int two = array[1];
            friendlyGraph.addAdge(one,two);
            while( i + plus <(n/2)) {
                int[] secondArray  = sets.get(i + plus);
                for(int j=0;j<2;j++){
                    friendlyGraph.addAdge(secondArray[j],array[j]);
                }
                plus++;
            }

        }

        for(int i=0;i<n/2-1;i++){
            int[] oneArray = sets.get(i);
            int[] twoArray = sets.get(i+1);
            for(int j=0;j<2;j++){
                friendlyGraph.addAdge(oneArray[j],twoArray[j]);
            }
        }




        graphs[0] = friendlyGraph;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(i!=j && !friendlyGraph.isAdj(i,j) ){
                    int enemiesToFirst = enemiesCountMap.get(i);
                    int enemiesToSecond = enemiesCountMap.get(j);

                    if(enemiesToFirst + 1 <= k && enemiesToSecond + 1 <= k) {
                        enemyGraph.addAdge(i, j);
                        enemiesCountMap.put(i,enemiesToFirst+1);
                        enemiesCountMap.put(j,enemiesToSecond+1);
                    }

                }
            }
        }
        graphs[1] = enemyGraph;





        return graphs;
        
    }


}
