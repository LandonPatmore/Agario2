package Utils;

public class Score implements Comparable<Score> {

    private long timeAlive;
    private String name;

    public Score(long timeAlive, String name){
        this.timeAlive = timeAlive;
        this.name = name;
    }

    public int getSeconds(){
        return (int) (timeAlive / 1000);
    }

    @Override
    public String toString() {
        return "Name:" + name + "\nTime Alive " + timeAlive;
    }

    @Override
    public int compareTo(Score o) {
        return Integer.compare(o.getSeconds(), this.getSeconds());
    }
}
