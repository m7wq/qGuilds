package dev.m7wq.qguilds.subcmds;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.entity.Commandable;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.roles.RoleType;
import dev.m7wq.qguilds.utils.ChatUtil;
import net.md_5.bungee.api.chat.TextComponent;


public class CreateSubCommand implements Commandable {

    Set<String> nameSet = new HashSet<>();

    // COOLDOWN

    @Override
    public void jobLoad(Player sender, String... arg) {
        if (!arg[0].equalsIgnoreCase("create"))
            throw new IllegalStateException("SubCreateGuild issue on executing");




        if (arg.length != 4){
            for (String string : Plugin.getInstance().getConfigurationManager()
                    .getMessages(ConfigurationManager.Message.FAILED_CREATING_GUILD)){
                sender.sendMessage(Plugin.getInstance().colorize(
                        string.replace("%prefix%",Plugin.getInstance().getPrefix())
                ));
            }
            return;
        }

        if (arg[3].length() > 3){
            sender.sendMessage(Plugin.getInstance().colorize(
                    Plugin.getInstance().getConfigurationManager()
                            .getMessage(ConfigurationManager.Message.LONG_TAG)
                            .replace("%prefix%",Plugin.getInstance().getPrefix())
            ));
            return;
        }
        if (Plugin.getInstance().getGuildsManager().onGuild(sender.getName())) {

            if (!nameSet.contains(sender.getName())) {
                nameSet.add(sender.getName());

                boolean confirmationMessageSent = false;
                String playerRole = Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName());

                for (String string : Plugin.getInstance().getConfigurationManager().getMessages(ConfigurationManager.Message.ALREADY_ON_GUILD)) {


                    if (string.contains("%confirm%")) {
                        String confirmText = Plugin.getInstance().colorize(Plugin.getInstance().getConfigurationManager().getMessage(ConfigurationManager.Message.CONFIRM));
                        String confirmLine = Plugin.getInstance().colorize(string.replace("%prefix%", Plugin.getInstance().getPrefix()).replace("%confirm%", confirmText));

                        String command = playerRole.equalsIgnoreCase(RoleType.MASTER.name()) ? "/g disband" : "/g leave";
                        String hoverText = playerRole.equalsIgnoreCase(RoleType.MASTER.name()) ? "&cDisband the guild" : "&cLeave the guild";

                        TextComponent confirmComponent = ChatUtil.createCommandClickableText(confirmLine, command);
                        TextComponent finalComponent = ChatUtil.hoverText(confirmComponent, Plugin.getInstance().colorize(hoverText));
                        sender.spigot().sendMessage(finalComponent);

                        confirmationMessageSent = true;
                        continue;
                    }

                    sender.sendMessage(Plugin.getInstance().colorize(string.replace("%prefix%", Plugin.getInstance().getPrefix())));
                }

                if (confirmationMessageSent) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (nameSet.contains(sender.getName())) {
                                nameSet.remove(sender.getName());
                                sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(ConfigurationManager.Message.TIME_OVER));
                            }
                        }
                    }.runTaskLater(Plugin.getInstance(), Plugin.getInstance().getConfig().getInt("options.selecting-time")*20);
                }

                return;
            }


        }

        String name = arg[1];
        String displayName = arg[2];
        String tag = arg[3].toUpperCase();
        Member master = new Member(RoleType.MASTER.name(), sender.getName(), false);

        Plugin.getInstance().getMySQL().createGuild(
                master,
                new Guild(displayName, Plugin.getInstance().getConfigurationManager()
                        .getConfigure(ConfigurationManager.Configure.DEFAULT_TAG_COLOR), tag, name, Arrays.asList(master))
        );

        sender.sendMessage(Plugin.getInstance().colorize(
                Plugin.getInstance().getConfigurationManager().getMessage(ConfigurationManager.Message.SUCCESSFULLY_CREATED)
                        .replace("%prefix%", Plugin.getInstance().getPrefix())
        ));

        nameSet.remove(sender.getName());

    }
}
