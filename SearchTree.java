package vertexcover;

import java.io.FileNotFoundException;

public class SearchTree {

    static class Instance {
        MyGraph G;
        int k;

        Instance(MyGraph g, int k) {
            this.G = g;
            this.k = k;
        }
    }

    private static boolean solve(Instance i) {

        if (i.k < 0) {
            return false;
        }
        if (i.G.getEdgeCount() == 0) {
            return true;
        }

        Integer u = null;
        Integer v = null;

        for (Integer x : i.G.getVertices()) {
            if (i.G.degree(x) != 0) {
                u = x;
                v = i.G.getNeighbors(u).iterator().next();
            }
        }

        removeSingletons(i.G);

        Instance i1 = new Instance(i.G.getCopy(), i.k-1);
        if (i.G.degree(u) == 1) {
            removeDegOne(i);
            int n = i1.G.getNeighbors(u).iterator().next();
            i1.G.deleteVertex(n);
        } else if (i.G.degree(v) > i.k) {
            removeHighDeg(i1);
        } else {
            i1.G.deleteVertex(u);
        }

        Instance i2 = new Instance(i.G.getCopy(), i.k-1);
        if (i.G.degree(v) == 1) {
            removeDegOne(i);
            int n = i2.G.getNeighbors(v).iterator().next();
            i2.G.deleteVertex(n);
        } else if (i.G.degree(v) > i.k) {
            removeHighDeg(i2);
        } else {
            i2.G.deleteVertex(v);
        }

        if (solve(i1)) return true;
        if (solve(i2)) return true;
        return false;
    }

    private static void removeSingletons(MyGraph g) {
        for (Integer key : g.getVertices()) {
            if (g.degree(key) == 0) {
                g.deleteVertex(key);
            }
        }
    }

    private static void removeDegOne(Instance i) {
        for (int v = 0; v < i.G.size(); v++) {
            if (i.G.degree(v) == 1) {
                int u = i.G.getNeighbors(v).iterator().next();
                i.G.deleteVertex(u);
                i.k -= 1;
                v += 1;
            }
        }
    }

    private static void removeHighDeg(Instance i) {
        for (int v = 0; v < i.G.size(); v++) {
            if (i.G.degree(v) > i.k) {
                i.G.deleteVertex(v);
                i.k -= 1;
                v += 1;
            }
        }
    }

    public static int solve(MyGraph g) {
        System.out.print("|V|: " + g.size() + " || ");
        System.out.print("|E|: " + g.getEdgeCount() + " || ");
        int size = g.size();
        for (int i = 1; i < size; i++) {
            Instance in = new Instance(g,i);
            if (solve(in)) {
                System.out.println("k: " + i);
                return i;
            }
        }
        System.out.println("k: 0");
        return 0;
    }

}
