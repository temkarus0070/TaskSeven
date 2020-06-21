package ru.vsu.cs.course1.graph;

public class Edge {
    int v1;
    int v2;

    public Edge(int v1,int v2){
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Edge))
            return false;
        Edge edge = (Edge) o;
        boolean equals = false;
        if(edge.v1 == this.v1 && edge.v2 == this.v2)
            equals = true;
        if(edge.v2 == this.v1 && edge.v1 == this.v2)
            equals = true;

        return equals;
    }

    @Override
    public int hashCode(){
        int res = 0;
        res+=(this.v1 * this.v2);
        return res;
    }
}
