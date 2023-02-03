package util;

public class TeamsInfo {
    public String name;
    public int w;
    public int l;
    public int r;
    public int[] against;

    public TeamsInfo(String name, int w, int l, int r, int[] against) {
        this.name = name;
        this.w = w;
        this.l = l;
        this.r = r;
        this.against = against;
    }
}
