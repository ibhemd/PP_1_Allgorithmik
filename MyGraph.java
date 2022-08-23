package vertexcover;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MyGraph implements Graph {

    public HashMap<Integer, LinkedList<Integer>> adjacencyList;

    public MyGraph(String filename) throws FileNotFoundException {
        adjacencyList = new HashMap<>();
        Scanner scanner = new Scanner(new File("data/" + filename));
        while (scanner.hasNext()) {
            String s = scanner.nextLine();

            int index = s.indexOf(" ");
            if (index == -1) {
                index = s.indexOf("\t");
            }

            Integer v = Integer.parseInt(s.substring(0,index));
            Integer w = Integer.parseInt(s.substring(index+1).replaceAll("\\s",""));
            addEdge(v,w);
        }
    }

    public MyGraph(MyGraph g) {
        this.adjacencyList = new HashMap<>();
        for (Integer key : g.adjacencyList.keySet()) {
            this.adjacencyList.put(key, new LinkedList<Integer>());
            for (Integer i : g.adjacencyList.get(key)) {
                this.adjacencyList.get(key).add(i);
            }
        }
    }

    @Override
    public void addVertex(Integer v) {
        this.adjacencyList.put(v, new LinkedList<Integer>());
    }

    @Override
    public void addEdge(Integer v, Integer w) {
        if (this.adjacencyList == null || !this.adjacencyList.containsKey(v)) {
            addVertex(v);
        }
        if (!this.adjacencyList.containsKey(w)) {
            addVertex(w);
        }
        this.adjacencyList.get(v).add(w);
        this.adjacencyList.get(w).add(v);
    }

    @Override
    public void deleteVertex(Integer v) {
        if (this.adjacencyList.containsKey(v)) {
            for (Integer key : adjacencyList.keySet()) {
                deleteEdge(key, v);
            }
            this.adjacencyList.remove(v);
        }
    }

    @Override
    public void deleteEdge(Integer u, Integer v) {
        if (this.adjacencyList.containsKey(u) && this.adjacencyList.containsKey(v)) {
            this.adjacencyList.get(u).remove(v);
            this.adjacencyList.get(v).remove(u);
        }
    }

    @Override
    public boolean contains(Integer v) {
        if (this.adjacencyList.containsValue(v)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int degree(Integer v) {
        if (this.adjacencyList.get(v) == null) {
            return 0;
        } else {
            return this.adjacencyList.get(v).size();
        }
    }

    @Override
    public boolean adjacent(Integer v, Integer w) {
        if(v.equals(w)) return false;
        if(this.adjacencyList.get(v).contains(w) || this.adjacencyList.get(w).contains(v)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public MyGraph getCopy() {
        return new MyGraph(this);
    }

    @Override
    public Set<Integer> getNeighbors(Integer v) {
        Set<Integer> res = new HashSet<Integer>();
        for (Integer key : this.adjacencyList.keySet()) {
            if (adjacent(key,v)) {
                res.add(key);
            }
        }
        return res;
    }

    @Override
    public int size() {
        return this.adjacencyList.size();
    }

    @Override
    public int getEdgeCount() {
        if (size() == 0) {
            return 0;
        } else {
            int res = 0;
            Set s = getVertices();
            for (Object i : s) {
                res += degree((Integer) i);
            }
            return res/2;
        }
    }

    @Override
    public Set<Integer> getVertices() {
        Set<Integer> res = new HashSet<Integer>();
        for (Integer key : this.adjacencyList.keySet()) {
            res.add(key);
        }
        return res;
    }

    public String toString() {
        return this.adjacencyList.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        long begin = System.nanoTime();

        String[] arr = new String[15];
        //arr[0] = "bio-dmelamtx.sec";                          // |V|:  7393 || |E|:  25569 || k:      ->  2630
        //arr[1] = "ca-sandi_authsmtx.sec";                     // |V|:    86 || |E|:    124 || k:      ->    38
        //arr[2] = "inf-openflightsedges.sec";                  // |V|:  2939 || |E|:  30501 || k:      ->  1088
        //arr[3] = "inf-powermtx.sec";                          // |V|:  4941 || |E|:   6594 || k:      ->  2203
        //arr[4] = "inf-USAir97mtx.sec";                        // |V|:   332 || |E|:   2126 || k:      ->   149
        //arr[5] = "outadjnoun_adjacency_adjacency.sec";        // |V|:   112 || |E|:    425 || k:      ->    59
        //arr[6] = "outarenas-email.sec";                       // |V|:  1133 || |E|:   5451 || k:      ->   594
        //arr[7] = "outarenas-jazz.sec";                        // |V|:   198 || |E|:   2742 || k:      ->   157
        //arr[8] = "outcontiguous-usa.sec";                     // |V|:    49 || |E|:    107 || k:      ->    30
        //arr[9] = "outdolphins.sec";                           // |V|:    62 || |E|:    159 || k:      ->    34
        //arr[10] = "outmoreno_zebra_zebra.sec";                // |V|:    27 || |E|:    111 || k: 20   ->    20
        //arr[11] = "outucidata-zachary.sec";                   // |V|:    34 || |E|:     78 || k: 14   ->    14
        arr[12] = "sample.sec";                               // |V|:    17 || |E|:     22 || k: 10   ->    10
        //arr[13] = "sample2.sec";                              // |V|:    12 || |E|:     12 || k:  4   ->     4
        //arr[14] = "soc-brightkitemtx.sec";                    // |V|: 56739 || |E|: 212945 || k:      -> 21190

        int i = 1;
        for (String s : arr) {
            if (s != null) {
                System.out.println("");
                System.out.print("File #" + i + ": " + s + " || ");
                MyGraph sample = new MyGraph(s);
                SearchTree.solve(sample);
            }
            i += 1;
        }

        long end = System.nanoTime();
        long diff = end - begin;

        System.out.println("Duration: " + diff + "ns < 1 min. : " + (diff < (6*Math.pow(10,10))));
    }

}
