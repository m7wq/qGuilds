package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.utils.ChatUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.List;

public class ListSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();

        GuildsManager manager = Plugin.getInstance().getGuildsManager();


        boolean isAllowed = cfg.getConfig().getBoolean(ConfigurationManager.Configure.ALLOW_CHECKING_OTHER_GUILDS_LISTS.getKey());

        if (isAllowed){
            if (arg.length != 1 && arg.length != 2){
                ChatUtil.sendMessage(sender, ConfigurationManager.Message.CANT_SEND_LIST);
                return;
            }

            if (arg.length == 2){

                String taggedGuild = arg[1];

                if (!manager.getGuildMap().containsKey(taggedGuild)){
                    ChatUtil.sendMessage(sender, ConfigurationManager.Message.NOT_EXISTS_GUILD);
                    return;
                }


                sendList(sender,taggedGuild);
            }
            return;
        }

        if (arg.length != 1){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.UN_ALLOWED_TO_SEND_LIST);
            return;
        }

        if (!manager.onGuild(sender.getName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NOT_IN_GUILD_TO_LIST);
            return;
        }

        sendList(sender,manager.getPlayerGuild(sender.getName()).getGuildName());
    }

    public void sendList(ProxiedPlayer player,String guildName) {

        ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();
        GuildsManager mngr = Plugin.getInstance().getGuildsManager();
        HashMap<String, Guild> map = mngr.getGuildMap();


        String master = "";
        StringBuilder
                admins, moderators, specials, members;
        admins = new StringBuilder();
        moderators = new StringBuilder();
        specials = new StringBuilder();
        members = new StringBuilder();

        List<Member> list = map.get(guildName).getPlayersList();

        String onlinePlayerFormat = cfg.getMessage(null, ConfigurationManager.Message.ONLINE_PLAYER_IN_LIST);
        String offlinePlayerFormat = cfg.getMessage(null, ConfigurationManager.Message.OFFLINE_PLAYER_IN_LIST);
        String between = cfg.getMessage(null, ConfigurationManager.Message.BETWEEN_EVERY_PLAYER_IN_LIST);

        int totalPlayers = list.size();
        int onlinePlayers = 0;
        int offlinePlayers = 0;

        boolean firstAdmin = true;
        boolean firstModerator = true;
        boolean firstSpecial = true;
        boolean firstMember = true;

        for (Member member : list) {
            switch (member.getRole()) {
                case "MASTER":

                    if (isConnected(member)) {

                          master =  onlinePlayerFormat.replace("%player%", member.getIgn());

                        onlinePlayers++;
                    } else {

                      master=          offlinePlayerFormat.replace("%player%", member.getIgn());


                        offlinePlayers++;
                    }
                break;
                case "ADMIN":
                    if (firstAdmin) {
                        if (isConnected(member)) {
                            admins.append(
                                    onlinePlayerFormat.replace("%player%", member.getIgn())
                            );
                            onlinePlayers++;
                        } else {
                            admins.append(
                                    offlinePlayerFormat.replace("%player%", member.getIgn())

                            );
                            offlinePlayers++;

                        }
                        firstAdmin = false;
                    }else {
                        if (isConnected(member)) {
                            admins.append(between).append(
                                    onlinePlayerFormat.replace("%player%", member.getIgn())
                            );
                            onlinePlayers++;
                        } else {
                            admins.append(between).append(
                                    offlinePlayerFormat.replace("%player%", member.getIgn())

                            );
                            offlinePlayers++;
                        }
                    }
                    break;
                case "MODERATOR":
                    if (firstModerator) {
                        if (isConnected(member)) {
                            moderators.append(
                                    onlinePlayerFormat.replace("%player%", member.getIgn())
                            );
                            onlinePlayers++;
                        } else {
                            moderators.append(
                                    offlinePlayerFormat.replace("%player%", member.getIgn())

                            );
                            offlinePlayers++;

                        }
                        firstModerator = false;
                    }else {
                        if (isConnected(member)) {
                            moderators.append(between).append(
                                    onlinePlayerFormat.replace("%player%", member.getIgn())
                            );
                            onlinePlayers++;
                        } else {
                            moderators.append(between).append(
                                    offlinePlayerFormat.replace("%player%", member.getIgn())

                            );
                            offlinePlayers++;
                        }
                    }
                    break;
                case "SPECIAL":
                    if (firstSpecial) {
                        if (isConnected(member)) {
                            specials.append(
                                    onlinePlayerFormat.replace("%player%", member.getIgn())
                            );
                            onlinePlayers++;
                        } else {
                            specials.append(
                                    offlinePlayerFormat.replace("%player%", member.getIgn())

                            );
                            offlinePlayers++;

                        }
                        firstSpecial = false;
                    }else {
                        if (isConnected(member)) {
                            specials.append(between).append(
                                    onlinePlayerFormat.replace("%player%", member.getIgn())
                            );
                            onlinePlayers++;
                        } else {
                            specials.append(between).append(
                                    offlinePlayerFormat.replace("%player%", member.getIgn())

                            );
                            offlinePlayers++;
                        }
                    }
                    break;
                case "MEMBER":
                    if (firstMember) {
                        if (isConnected(member)) {
                            members.append(
                                    onlinePlayerFormat.replace("%player%", member.getIgn())
                            );
                            onlinePlayers++;
                        } else {
                            members.append(
                                    offlinePlayerFormat.replace("%player%", member.getIgn())

                            );
                            offlinePlayers++;

                        }
                        firstMember = false;
                    }else {
                        if (isConnected(member)) {
                            members.append(between).append(
                                    onlinePlayerFormat.replace("%player%", member.getIgn())
                            );
                            onlinePlayers++;
                        } else {
                            members.append(between).append(
                                    offlinePlayerFormat.replace("%player%", member.getIgn())

                            );
                            offlinePlayers++;
                        }
                    }
                    break;

            }
        }

        List<String> listMessage = cfg.getMessages(
                player, ConfigurationManager.Message.LIST_MESSAGE
        );


        for (String str : listMessage) {
            str = str.replace("%prefix%", Plugin.getInstance().getPrefix())
            .replace("%guild%", guildName)
            .replace("%master%", master)
            .replace("%admins%", admins)
            .replace("%moderators%", moderators)
            .replace("%specials%", specials)
                    .replace("%members%",members)
            .replace("%online_players%", String.valueOf(onlinePlayers))
            .replace("%offline_players%", String.valueOf(offlinePlayers))
            .replace("%total_players%", String.valueOf(totalPlayers));

            player.sendMessage(Plugin.getInstance().colorize(str));
        }
    }

    public boolean isConnected(Member member){
        ProxiedPlayer player = Plugin.getInstance().getProxy().getPlayer(member.getIgn());
        if (player==null){
            return false;
        }
        return true;
    }
}


