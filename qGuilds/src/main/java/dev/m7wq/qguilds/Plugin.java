package dev.m7wq.qguilds;

import java.sql.SQLException;
import java.util.HashSet;

import dev.m7wq.qguilds.events.ChatListener;
import dev.m7wq.qguilds.events.PlayerLeaveListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.validation.NotNull;

import dev.m7wq.qguilds.events.PlayerJoinListener;
import dev.m7wq.qguilds.executor.Executor;
import dev.m7wq.qguilds.executor.TabCompleter;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.mysql.MySQL;
import dev.m7wq.qguilds.objects.Invitation;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;


public final class Plugin extends JavaPlugin {

    private String prefix;

    private MySQL mySQL;

    private BukkitAudiences adventure;

    public @NotNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    private GuildsManager guildsManager;
    private ConfigurationManager configurationManager;
    private InvitingManager invitingManager;
    private MembersManager membersManager;

    private static  Plugin INSTANCE;





    @Override
    public void onEnable() {



        INSTANCE = this;

        init();
        this.adventure = BukkitAudiences.create(this);
        connect();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!getInvitingManager().getInvitedMap().containsKey(player.getName())) {
                getInvitingManager().getInvitedMap().put(player.getName(), new HashSet<>());
            }
        });

        getGuildsManager().load(getMySQL());

        getConfigurationManager().loadConfigurations(this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Identifiers().register();
        }else{
            throw new IllegalStateException("Please add PlaceholderAPI Plugin so this plugin can launch successfully.");
        }




        

        getCommand("guilds").setExecutor(new Executor());
        getCommand("guilds").setTabCompleter(new TabCompleter());
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(invitingManager), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(),this);
        getServer().getPluginManager().registerEvents(new ChatListener(),this);


    }

    @Override
    public void onDisable() {



        getMySQL().saveCacheData(getGuildsManager().getGuildMap());


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

    public static Plugin getInstance(){return INSTANCE;}

    private void init(){

        prefix = getConfig().getString("options.prefix");

        this.mySQL = new MySQL();
        this.guildsManager = new GuildsManager();
        this.configurationManager = new ConfigurationManager();

        this.invitingManager = new InvitingManager();
        this.membersManager = new MembersManager(this.guildsManager);


    }

    private void connect() {
        try {
            getMySQL().connect(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public MySQL getMySQL(){
        if(this.mySQL == null)
            throw new IllegalStateException("mySQL is not initialized!");
        return this.mySQL;
    }

    public GuildsManager getGuildsManager() {
        if(this.guildsManager == null)
            throw new IllegalStateException("GuildsManager is not initialized!");
        return guildsManager;
    }

    public String getPrefix(){
        return colorize(this.prefix);
    }
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }
    public String colorize(String string){
        return ChatColor.translateAlternateColorCodes('&',string);
    }


    public InvitingManager getInvitingManager() {
        return invitingManager;
    }

    public MembersManager getMembersManager() {
        return membersManager;
    }
}
