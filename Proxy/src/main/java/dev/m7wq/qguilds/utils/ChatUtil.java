package dev.m7wq.qguilds.utils;


import java.util.HashSet;
import java.util.Set;

import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.enums.RoleType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;


public class ChatUtil {

    public static Set<String> chatToggleSet = new HashSet<>();

    public static void sendJoinGuildMessage(ProxiedPlayer player){
        Guild guild = PluginProxied.getInstance().getGuildsManager().getPlayerGuild(player.getName());

        guild.getPlayersList().forEach(member -> {
            ProxiedPlayer memberPlayer = PluginProxied.getInstance().getProxy().getPlayer(member.getIgn());
            if (memberPlayer.isConnected()) {
                memberPlayer.sendMessage(PluginProxied.getInstance().colorize(PluginProxied.getInstance().getConfigurationManager()
                        .getMessage(memberPlayer, ConfigurationManager.Message.JOIN_GUILD_MESSAGE).replace("%player%",player.getName())));
            }
        });
    }

    public static void sendPlayerIsOffline(ProxiedPlayer player){
        player.sendMessage(PluginProxied.getInstance().getConfigurationManager().getMessage(player,ConfigurationManager.Message.PLAYER_OFFLINE));
    }

    // For leave or disband
    public static void sendConfirming(String playerRole, String string, ProxiedPlayer sender){
        String confirmText = PluginProxied.getInstance().colorize(PluginProxied.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.CONFIRM));

        String confirmLine = PluginProxied.getInstance().colorize(string.replace("%prefix%", PluginProxied.getInstance().getPrefix()));

        String command = playerRole.equalsIgnoreCase(RoleType.MASTER.name()) ? "/g disband" : "/g leave";
        String hoverText = playerRole.equalsIgnoreCase(RoleType.MASTER.name()) ? "&cDisband the guild" : "&cLeave the guild";
        Component confirmButton = Component.text(confirmText).hoverEvent(HoverEvent.showText(Component.text(PluginProxied.getInstance().colorize(hoverText)))).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,command));

        Component confirmComponent = Component.text(confirmLine);
        Component finalComponent = ChatUtil.replaceComponents(confirmComponent,"%confirm%",confirmButton).color(NamedTextColor.GREEN);

        PluginProxied.getInstance().adventure().player(sender).sendMessage(finalComponent);
    }

    public static @NotNull Component replaceComponents(Component mainComponent, String placeholder, Component component) {


        Component finalComponent = mainComponent.hoverEvent(null).clickEvent(null);


        finalComponent = finalComponent.replaceText(TextReplacementConfig.builder()
                .matchLiteral(placeholder)
                .replacement(component)
                .build());

        return finalComponent;
    }

    public static void sendGuildMessage(ProxiedPlayer sender, String finalFormat, Guild playerGuild){
        String format = PluginProxied.getInstance().getConfigurationManager()
                .getMessage(sender,ConfigurationManager.Message.GUILD_MESSAGE_FORMAT)
                .replace("%prefix%", PluginProxied.getInstance().getPrefix())
                .replace("%player%",sender.getName())
                .replace("%message%",finalFormat);



        playerGuild.getPlayersList().forEach(member -> {
            ProxiedPlayer player = PluginProxied.getInstance().getProxy().getPlayer(member.getIgn());
            if (player.isConnected()){
                player.sendMessage(PluginProxied.getInstance().colorize(finalFormat));
            }
        });
    }

    public static void sendSections(ProxiedPlayer player, String guildName, ConfigurationManager.Message message) {
            String line = null;
            for (String string : PluginProxied.getInstance().getConfigurationManager().getMessages(player,message)){
                if (string.contains("%yes%")||string.contains("%no%")){
                    line = string.replace("%prefix%", PluginProxied.getInstance().getPrefix());
                }
            }

        String yes = "[YES]";
        String no = "[NO]";

        Component yesComponent = Component.text(yes)
                .color(NamedTextColor.GREEN)

                .clickEvent(ClickEvent.runCommand("/g accept "+guildName))
                .hoverEvent(HoverEvent.showText(Component.text("Accept the invitation").color(NamedTextColor.GREEN)));


        Component noComponent = Component.text(no)
                .color(NamedTextColor.RED)

                .clickEvent(ClickEvent.runCommand("/g ignore "+guildName))
                .hoverEvent(HoverEvent.showText(Component.text("Ignore the invitation").color(NamedTextColor.RED)));


        Component finalComponent = Component.text(line)
                .replaceText(builder -> builder.matchLiteral("%yes%").replacement(yesComponent))
                .replaceText(builder -> builder.matchLiteral("%no%").replacement(noComponent));

        PluginProxied.getInstance().adventure().player(player).sendMessage(finalComponent);

    }

}






