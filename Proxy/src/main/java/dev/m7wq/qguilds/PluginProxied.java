package dev.m7wq.qguilds;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;


import dev.m7wq.qguilds.events.ChatListener;
import dev.m7wq.qguilds.events.PlayerJoinListener;
import dev.m7wq.qguilds.events.PlayerLeaveListener;
import dev.m7wq.qguilds.mysql.MySQL;
import dev.m7wq.qguilds.commands.Executor;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import dev.m7wq.qguilds.managers.MembersManager;
import lombok.Getter;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;


public final class PluginProxied extends net.md_5.bungee.api.plugin.Plugin {

    private String prefix;

    private @Getter MySQL mySQL;

    private BungeeAudiences adventure;



    private @Getter GuildsManager guildsManager;
    private @Getter ConfigurationManager configurationManager;
    private @Getter InvitingManager invitingManager;
    private @Getter MembersManager membersManager;

    private static @Getter PluginProxied instance;





    @Override
    public void onEnable() {

        // initializers
        init();
        instance = this;
        this.adventure = BungeeAudiences.create(this);
        connect();
        load();


        // Opening
        getLogger().log(Level.ALL,
                "[----------------------------]/n" +
                        "!- Platform: Proxy/n"+
                        "!- Program: qGuilds/n"+
                        "!- Description: Free customized guilds plugin/n"+
                        "/n"+
                        "- Developer: [m7wq]/n"+
                        "- Development: DevVerse Minecraft/n"+
                        "- Good luck while using my plugin :D/n"+
                     "[----------------------------]");


        // registers
        getProxy().getPluginManager().registerCommand(this,new Executor());
        getProxy().getPluginManager().registerListener(this,new PlayerJoinListener(invitingManager));
        getProxy().getPluginManager().registerListener(this,new PlayerLeaveListener());
        getProxy().getPluginManager().registerListener(this,new ChatListener());




    }

    @Override
    public void onDisable() {





        getMySQL().saveCacheData(getGuildsManager().getGuildMap());

        close();



    }



    private void close(){

        try {
            mySQL.getConnection().close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        mySQL.disconnect();

        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }


    private void init(){

        this.mySQL = new MySQL();
        this.guildsManager = new GuildsManager();
        this.configurationManager = new ConfigurationManager();

        this.invitingManager = new InvitingManager();
        this.membersManager = new MembersManager(this.guildsManager);

        prefix = configurationManager.getConfig().getString("prefix");
    }

    private void connect() {
        try {
            getMySQL().connect(configurationManager.getConfig());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void load(){
        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (!getInvitingManager().getInvitedMap().containsKey(player.getName())) {
                getInvitingManager().getInvitedMap().put(player.getName(), new HashSet<>());
            }
        });

        getGuildsManager().load(getMySQL());

        getConfigurationManager().loadConfigurations(this);
    }


    public @NotNull BungeeAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public String getPrefix(){
        return colorize(this.prefix);
    }

    public String colorize(String string){
        return ChatColor.translateAlternateColorCodes('&',string);
    }



}
