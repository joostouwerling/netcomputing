package nl.rug.netcompuring.sportperformancemanagement;

/**
 * Created by joostouwerling on 24/02/16.
 */
public class Match {

    private String name;
    private String id;

    public Match(String n, String id)
    {
        setName(n);
        setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Match[] generateArray (int num) {
        Match[] matches = new Match[num];
        for (int i = 0; i < num; i++)
            matches[i] = new Match("Match " + i, "id" + i);
        return matches;
    }
}
