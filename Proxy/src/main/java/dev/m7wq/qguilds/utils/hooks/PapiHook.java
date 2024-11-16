package dev.m7wq.qguilds.utils.hooks;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.william278.papiproxybridge.api.PlaceholderAPI;
import net.william278.papiproxybridge.user.OnlineUser;

import java.util.concurrent.ExecutionException;

public class PapiHook {

    private static PlaceholderAPI api;

    public static void initialize(){
       api = PlaceholderAPI.createInstance();
    }

    public static String format(String string, ProxiedPlayer player) throws ExecutionException,InterruptedException {
        return api.formatPlaceholders(string,player.getUniqueId()).get();

    }
}
