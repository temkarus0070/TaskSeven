package ru.vsu.cs.course1.graph;

import java.awt.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.management.Query;

/**
 * Интерфейс для описания неориентированного графа (н-графа)
 * с реализацией некоторых методов графа
 */
public interface Graph {
    /**
     * Кол-во вершин в графе
     * @return
     */
    int vertexCount();

    /**
     * Кол-во ребер в графе
     * @return
     */
    int edgeCount();

    /**
     * Добавление ребра между вершинами с номерами v1 и v2
     * @param v1
     * @param v2
     */
    void addAdge(int v1, int v2);

    /**
     * Удаление ребра/ребер между вершинами с номерами v1 и v2
     * @param v1
     * @param v2
     */
    void removeAdge(int v1, int v2);

    /**
     * @param v Номер вершины, смежные с которой необходимо найти
     * @return Объект, поддерживающий итерацию по номерам связанных с v вершин
     */
    Iterable<Integer> adjacencies(int v);

    /**
     * Проверка смежности двух вершин
     * @param v1
     * @param v2
     * @return
     */
    default boolean isAdj(int v1, int v2) {
        for (Integer adj : adjacencies(v1)) {
            if (adj == v2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Поиск в глубину, реализованный рекурсивно
     * (начальная вершина также включена)
     * @param from Вершина, с которой начинается поиск
     * @param visitor Посетитель
     */
    default void dfsRecursionImpl(int from, Consumer<Integer> visitor) {
        boolean[] visited = new boolean[vertexCount()];

        class Inner {
            void visit(Integer curr) {
                visitor.accept(curr);
                visited[curr] = true;
                for (Integer v : adjacencies(curr)) {
                    if (!visited[v]) {
                        visit(v);
                    }
                }
            }
        }
        new Inner().visit(from);
    }

    /**
     * Поиск в глубину, реализованный с помощью стека
     * (не совсем "правильный"/классический, т.к. "в глубину" реализуется только "план" обхода, а не сам обход)
     * @param from Вершина, с которой начинается поиск
     * @param visitor Посетитель
     */
    default void dfsStackImpl(int from, Consumer<Integer> visitor) {
        boolean[] visited = new boolean[vertexCount()];
        Stack<Integer> stack = new Stack<>();
        stack.push(from);
        visited[from] = true;
        while (!stack.empty()) {
            Integer curr = stack.pop();
            visitor.accept(curr);
            for (Integer v : adjacencies(curr)) {
                if (!visited[v]) {
                    stack.push(v);
                    visited[v] = true;
                }
            }
        }
    }

    /**
     * Поиск в ширину, реализованный с помощью очереди
     * (начальная вершина также включена)
     * @param from Вершина, с которой начинается поиск
     * @param visitor Посетитель
     */
    default void bfsQueueImpl(int from, Consumer<Integer> visitor) {
        boolean[] visited = new boolean[vertexCount()];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(from);
        visited[from] = true;
        while (queue.size() > 0) {
            Integer curr = queue.remove();
            visitor.accept(curr);
            for (Integer v : adjacencies(curr)) {
                if (!visited[v]) {
                    queue.add(v);
                    visited[v] = true;
                }
            }
        }
    }

    /**
     * Поиск в глубину в виде итератора
     * (начальная вершина также включена)
     * @param from Вершина, с которой начинается поиск
     * @return Итератор
     */
    default Iterable<Integer> dfs(int from) {
        return new Iterable<Integer>() {
            private Stack<Integer> stack = null;
            private boolean[] visited = null;

            @Override
            public Iterator<Integer> iterator() {
                stack = new Stack<>();
                stack.push(from);
                visited = new boolean[Graph.this.vertexCount()];
                visited[from] = true;

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return ! stack.isEmpty();
                    }

                    @Override
                    public Integer next() {
                        Integer result = stack.pop();
                        for (Integer adj : Graph.this.adjacencies(result)) {
                            if (!visited[adj]) {
                                visited[adj] = true;
                                stack.add(adj);
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }

    /**
     * Поиск в ширину в виде итератора
     * (начальная вершина также включена)
     * @param from Вершина, с которой начинается поиск
     * @return Итератор
     */
    default Iterable<Integer> bfs(int from) {
        return new Iterable<Integer>() {
            private Queue<Integer> queue = null;
            private boolean[] visited = null;

            @Override
            public Iterator<Integer> iterator() {
                queue = new LinkedList<>();
                queue.add(from);
                visited = new boolean[Graph.this.vertexCount()];
                visited[from] = true;

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return ! queue.isEmpty();
                    }

                    @Override
                    public Integer next() {
                        Integer result = queue.remove();
                        for (Integer adj : Graph.this.adjacencies(result)) {
                            if (!visited[adj]) {
                                visited[adj] = true;
                                queue.add(adj);
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }

    /**
     * Получение dot-описяния графа (для GraphViz)
     * @return
     */



    default String toDot() {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = this instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : this.adjacencies(v1)) {
                sb.append(String.format("  %d %s %d", v1, (isDigraph ? "->" : "--"), v2)).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        System.out.println(sb.toString());
        return sb.toString();
    }


    default String toDotWithEnemies(Graph enemies) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = this instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : this.adjacencies(v1)) {
                sb.append(String.format("  %d %s %d", v1, (isDigraph ? "->" : "--"), v2)).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }

        for (int v1 = 0; v1 < enemies.vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : enemies.adjacencies(v1)) {
                sb.append(String.format("  %d %s %d[color=red]", v1, (isDigraph ? "->" : "--"), v2)).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }



        sb.append("}").append(nl);

        System.out.println(sb.toString());
        return sb.toString();
    }


    default String toDotWithPath( HashMap<Edge,Byte> edgeHashMap) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = this instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : this.adjacencies(v1)) {
                if(edgeHashMap.get(new Edge(v1,v2))!=null)
                    sb.append(String.format("  %d %s %d[style=dotted]", v1, (isDigraph ? "->" : "--"), v2)).append(nl);
                else
                    sb.append(String.format("  %d %s %d", v1, (isDigraph ? "->" : "--"), v2)).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }


    public static String toDotFromGroups( ArrayList<Group> groups) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = false;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for(int i=0;i<groups.size();i++){
            Group group = groups.get(i);
            int first = group.users.get(0);
            for(int j=1;j<group.users.size();j++) {
                int element = group.users.get(j);
                sb.append(String.format("  %d %s %d[color=%s]", first, (isDigraph ? "->" : "--"), element, Colors.colors[i])).append(nl);
              //  sb.append(String.format("  %d %s %d", first, (isDigraph ? "->" : "--"), element)).append(nl);
              //  sb.append(String.format("  %d %s %d", element, (isDigraph ? "->" : "--"), first)).append(nl);
            }
            if(group.users.size()==1)
                sb.append(first).append(nl);

        }
        sb.append("}").append(nl);
        System.out.println(sb.toString());

        return sb.toString();
    }
}
