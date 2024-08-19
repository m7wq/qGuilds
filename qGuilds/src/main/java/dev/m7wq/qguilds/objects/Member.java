package dev.m7wq.qguilds.objects;

public class Member {

    private String role;
    private String ign;
    private boolean muted;

    public Member(String role,String ign,boolean muted) {
        this.role = role;this.ign = ign; this.muted = muted;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIgn() {
        return ign;
    }

    public void setIgn(String ign) {
        this.ign = ign;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
