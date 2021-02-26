package me.opaque.player;

public class LPlayer {

    private int level;
    private int xp;

    public LPlayer(int level, int xp) {
        this.xp = xp;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void addLevel(int level) {
        this.level = getLevel() + level;
    }

    public void addXp(int xp) {
        this.xp = getXp() + xp;
    }

    public void removeLevel(int level) {
        this.level = getLevel() - level;
    }

    public void removeXp(int xp) {
        this.xp = getXp() - xp;
    }
}
