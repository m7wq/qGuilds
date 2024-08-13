package dev.m7wq.qguilds;

import java.sql.SQLException;

import dev.m7wq.qguilds.entity.TabCompleter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import dev.m7wq.qguilds.executor.Executor;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.mysql.MySQL;


public final class Plugin extends JavaPlugin {

    private String prefix;

    private MySQL mySQL;

    private GuildsManager guildsManager;
    private ConfigurationManager configurationManager;

    private static  Plugin INSTANCE;

    @Override
    public void onEnable() {

        INSTANCE = this;

        init();
        connect();

        getGuildsManager().load(getMySQL());

        getConfigurationManager().loadConfigurations(this);

        getCommand("guilds").setExecutor(new Executor());
        getCommand("guilds").setTabCompleter(new TabCompleter());

    }

    @Override
    public void onDisable() {



        getMySQL().saveCacheData(getGuildsManager().getGuildMap());
        System.out.println("Guilds - SAVED CACHE");

        try {
            mySQL.getConnection().close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        mySQL.disconnect();


    }

    public static Plugin getInstance(){return INSTANCE;}

    private void init(){

        prefix = getConfig().getString("options.prefix");

        this.mySQL = new MySQL();
        this.guildsManager = new GuildsManager();
        this.configurationManager = new ConfigurationManager();
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



}
