package ru.vsu.cs.course1.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Group {
    ArrayList<Integer> users;
    HashMap<Integer,Integer> usersWithEnemiesCountMap;
    private int maxEnemies = 0;

    public Group(){
        users = new ArrayList<>();
        usersWithEnemiesCountMap = new HashMap<>();

    }

    public void addFirstToGroup(int user){
        users.add(user);
        usersWithEnemiesCountMap.put(user,0);
    }

    public void addToGroup(int user,Graph graph){
        for(Iterator<Integer> iterator = usersWithEnemiesCountMap.keySet().iterator();iterator.hasNext();){
            int u = iterator.next();
            if(graph.isAdj(user,u)) {
                int count = usersWithEnemiesCountMap.get(u);
                usersWithEnemiesCountMap.put(u,count + 1);
                if((count + 1)>maxEnemies)
                    maxEnemies = count + 1;
            }
        }
        users.add(user);
    }

    public int getMaxEnemies(){
        return maxEnemies;
    }





}
