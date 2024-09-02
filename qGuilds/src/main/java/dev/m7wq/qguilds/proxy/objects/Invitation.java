package dev.m7wq.qguilds.proxy.objects;




public class Invitation {

    private Guild guild;
    private String inviterName;

    public Invitation(Guild guild, String inviterName) {
        this.guild = guild;
        this.inviterName = inviterName;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
    }
}
