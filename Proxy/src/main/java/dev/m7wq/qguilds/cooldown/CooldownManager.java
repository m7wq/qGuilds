package dev.m7wq.qguilds.cooldown;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    public HashMap<String,Cooldown> guildCooldown = new HashMap<>();

    public HashMap<String,Long> ownerCooldown = new HashMap<>();

    public static String convertSecondsToTime(long totalSeconds) {
        long days = totalSeconds / 86400;
        long hours = (totalSeconds % 86400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }

    public static long daysToSeconds(long days){
        long i = days*24*60*60;
        return i;
    }

    public static long hoursToSeconds(long hours){
        long i = hours*60*60;
        return i;
    }

    public void saveData(){
        Configuration cfg = Plugin.getInstance().getConfigurationManager().getCooldowns();
        GuildsManager guildsManager= Plugin.getInstance().getGuildsManager();


        for (String key : guildsManager.getGuildsNames()){

            if (!guildCooldown.containsKey(key)){
                cfg.set("guilds."+key,null);
                continue;
            }

            Cooldown cooldown = guildCooldown.get(key);

            cfg.set("guilds."+key+".set-tag-color-cooldown",cooldown.setTagColorCooldown);
            cfg.set("guilds."+key+".set-tag-cooldown",cooldown.setTagCooldown);
            cfg.set("guilds."+key+".rename-cooldown",cooldown.renameCooldown);




        }



        for (String key : guildsManager.getMastersNames()){

            if (!ownerCooldown.containsKey(key)){
                cfg.set("owners."+key,null);
                continue;
            }

            cfg.set("owners."+key,ownerCooldown.get(key));

        }




        System.out.println("Guilds - Saving cooldowns...");

        Plugin.getInstance().getConfigurationManager().cooldowns.saveConfig();

        System.out.println("Guilds - Cooldowns saved!");

    }

    public void load(){
        HashMap<String,Cooldown> map = new HashMap<>();

        ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();



        Configuration guildsSections = cfg.getCooldowns().getSection("guilds");

        for (String guildName : guildsSections.getKeys()){
            Configuration guild = guildsSections.getSection(guildName);

            long tagCooldown = guild.getLong("set-tag-cooldown");
            long tagColorCooldown = guild.getLong("set-tag-color-cooldown");
            long renameCooldown = guild.getLong("rename-cooldown");

            map.put(guildName,new Cooldown(tagCooldown,tagColorCooldown,renameCooldown));
        }

        guildCooldown.putAll(map);

        HashMap<String,Long> ownersMap = new HashMap<>();

        Configuration ownersSection = cfg.getCooldowns().getSection("owners");

        for (String owner : guildsSections.getKeys()){
            long ownerCl = ownersSection.getInt(owner);

            ownersMap.put(owner,ownerCl);

        }

        this.ownerCooldown.putAll(ownersMap);




        Plugin.getInstance().getProxy().getScheduler().schedule(Plugin.getInstance(), () -> {

            for (Guild guild : Plugin.getInstance().getGuildsManager().getGuildMap().values()){

                if (guildCooldown.containsKey(guild.getGuildName())) {

                    Cooldown cd = this.guildCooldown.get(guild.getGuildName());

                    if (cd.renameCooldown != 0) {
                        cd.setRenameCooldown(cd.renameCooldown - 1);
                    }

                    if (cd.setTagColorCooldown != 0) {
                        cd.setSetTagColorCooldown(cd.setTagColorCooldown - 1);
                    }
                    if (cd.setTagCooldown != 0) {
                        cd.setSetTagCooldown(cd.setTagCooldown - 1);
                    }

                }


                String master = guild.getPlayersList().get(0).getIgn();

                if (ownerCooldown.containsKey(master)) {
                    if (ownerCooldown.get(master) != 0) {

                        this.ownerCooldown.put(master, ownerCooldown.get(master) - 1);

                    }
                }
            }

        },0,1, TimeUnit.SECONDS);

    }
}
