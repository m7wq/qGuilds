package dev.m7wq.qguilds;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;


import dev.m7wq.qguilds.cooldown.CooldownManager;
import dev.m7wq.qguilds.events.ChatListener;
import dev.m7wq.qguilds.events.PlayerJoinListener;
import dev.m7wq.qguilds.events.PlayerLeaveListener;
import dev.m7wq.qguilds.events.custom.ChangeHappenListener;
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


public final class Plugin extends net.md_5.bungee.api.plugin.Plugin {

    private String prefix;

    private @Getter MySQL mySQL;

    private BungeeAudiences adventure;


    private @Getter CooldownManager cooldownManager;
    private @Getter GuildsManager guildsManager;
    private @Getter ConfigurationManager configurationManager;
    private @Getter InvitingManager invitingManager;
    private @Getter MembersManager membersManager;

    private static @Getter Plugin instance;





    @Override
    public void onEnable() {

        // initializers
        getProxy().registerChannel("qGuilds");
        init();
        instance = this;
        this.adventure = BungeeAudiences.create(this);
        connect();
        load();


        getLogger().log(Level.INFO,
                "\n"+
                        "24:60:60 [INFO] [QGUILDS] [--------------------- Q-GUILDS ---------------------]\n" +
                        "24:60:60 [INFO] [QGUILDS] !- Platform: Proxy\n" +
                        "24:60:60 [INFO] [QGUILDS] !- Program: qGuilds\n" +
                        "24:60:60 [INFO] [QGUILDS] !- Description: Free customized guilds plugin\n" +
                        "24:60:60 [INFO] [QGUILDS] \n" +
                        "24:60:60 [INFO] [QGUILDS] - Developer: [m7wq]\n" +
                        "24:60:60 [INFO] [QGUILDS] - Development Team: DevVerse Minecraft\n" +
                        "24:60:60 [INFO] [QGUILDS] - <3 Good luck using my plugin <3\n" +
                        "24:60:60 [INFO] [QGUILDS] [--------------------- Q-GUILDS ---------------------]");




        // registers

        getProxy().getPluginManager().registerCommand(this,new Executor());
        getProxy().getPluginManager().registerListener(this,new PlayerJoinListener(invitingManager));
        getProxy().getPluginManager().registerListener(this,new PlayerLeaveListener());
        getProxy().getPluginManager().registerListener(this,new ChatListener());
        getProxy().getPluginManager().registerListener(this,new ChangeHappenListener());





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

        getCooldownManager().saveData();
    }


    private void init(){

        this.mySQL = new MySQL();
        this.guildsManager = new GuildsManager();
        this.configurationManager = new ConfigurationManager(this);

        this.invitingManager = new InvitingManager();
        this.membersManager = new MembersManager(this.guildsManager);
        this.cooldownManager = new CooldownManager();

        prefix = configurationManager.getConfig().getString("options.prefix");
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
        getCooldownManager().load();

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
