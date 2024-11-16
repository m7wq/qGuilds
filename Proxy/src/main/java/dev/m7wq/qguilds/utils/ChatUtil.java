package dev.m7wq.qguilds.utils;



import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.RoleType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class ChatUtil {

    public static Set<String> chatToggleSet = new HashSet<>();

    public static void sendJoinGuildMessage(ProxiedPlayer player){
        Guild guild = Plugin.getInstance().getGuildsManager().getPlayerGuild(player.getName());

        guild.getPlayersList().forEach(member -> {

            if (BungeeHook.isConnected(member.getIgn())) {

                ProxiedPlayer memberPlayer = Plugin.getInstance().getProxy().getPlayer(member.getIgn());

                memberPlayer.sendMessage(Plugin.getInstance().colorize(Plugin.getInstance().getConfigurationManager()
                        .getMessage(memberPlayer, ConfigurationManager.Message.JOIN_GUILD_MESSAGE).replace("%player%",player.getName())));
            }
        });
    }

    public static void sendPlayerIsOffline(ProxiedPlayer player){
        player.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(player,ConfigurationManager.Message.PLAYER_OFFLINE));
    }

    public static Component makeComponent(String text, String hoverText, String command) {
        return Component.text(Plugin.getInstance().colorize(text))
                .hoverEvent(HoverEvent.showText(Component.text(Plugin.getInstance().colorize(hoverText))))
                .clickEvent(ClickEvent.suggestCommand(command));
    }

    // For leave or disband
    public static void sendConfirming(String playerRole, String string, ProxiedPlayer sender){
        String confirmText = Plugin.getInstance().colorize(Plugin.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.CONFIRM));

        String confirmLine = Plugin.getInstance().colorize(string.replace("%prefix%", Plugin.getInstance().getPrefix()));

        String command = playerRole.equalsIgnoreCase(RoleType.MASTER.name()) ? "/g disband" : "/g leave";
        String hoverText = playerRole.equalsIgnoreCase(RoleType.MASTER.name()) ? "&cDisband the guild" : "&cLeave the guild";
        Component confirmButton = Component.text(confirmText).hoverEvent(HoverEvent.showText(Component.text(Plugin.getInstance().colorize(hoverText)))).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,command));

        Component confirmComponent = Component.text(confirmLine);
        Component finalComponent = ChatUtil.replaceComponents(confirmComponent,"%confirm%",confirmButton).color(NamedTextColor.GREEN);

        Plugin.getInstance().adventure().player(sender).sendMessage(finalComponent);
    }

    public static @NotNull Component replaceComponents(Component mainComponent, String placeholder, Component replacement) {


        Component finalComponent = mainComponent.hoverEvent(null).clickEvent(null);




        finalComponent = finalComponent.replaceText(TextReplacementConfig.builder()
                .matchLiteral(placeholder)
                .replacement(replacement)
                .build());

        return finalComponent;
    }

    public static void sendGuildMessage(ProxiedPlayer sender, String finalFormat, Guild playerGuild){
        String playerName = sender.getName();
        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();
        String guildDisplayName = playerGuild.getDisplayName();
        String playerRole = RoleType.getByName(guildsManager.getPlayerRole(playerName)).getDisplayRole();


        String format = Plugin.getInstance().getConfigurationManager()
                .getMessage(sender,ConfigurationManager.Message.GUILD_MESSAGE_FORMAT)
                .replace("%prefix%", Plugin.getInstance().getPrefix())
                .replace("%player%",sender.getName())
                .replace("%message%",finalFormat)
                .replace("%role%",playerRole)
                .replace("%guild_name%",guildDisplayName);



        playerGuild.getPlayersList().forEach(member -> {

            if (BungeeHook.isConnected(member.getIgn())){
                ProxiedPlayer player = Plugin.getInstance().getProxy().getPlayer(member.getIgn());
                player.sendMessage(Plugin.getInstance().colorize(format));
            }
        });
    }

    public static void sendSections(ProxiedPlayer player, String guildName, ConfigurationManager.Message message) {
            String line = null;
            for (String string : Plugin.getInstance().getConfigurationManager().getMessages(player,message)){
                if (string.contains("%yes%")||string.contains("%no%")){
                    line = string.replace("%prefix%", Plugin.getInstance().getPrefix());
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

        Plugin.getInstance().adventure().player(player).sendMessage(finalComponent);

    }

    public static boolean containsSpecialCharacter(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        String specialCharactersPattern = "[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?~-]";
        return input.matches(".*" + specialCharactersPattern + ".*");
    }



    public static void sendMessage(ProxiedPlayer player, ConfigurationManager.Message message) {
        ConfigurationManager configManager = Plugin.getInstance().getConfigurationManager();
        Object configMessage = configManager.getMessagesConfig().get(message.getKey());


        if (configMessage instanceof List) {
            List<String> messages =  configManager.getMessages(player, message);
            for (String msg : messages) {
                String formattedMessage = Plugin.getInstance().colorize(msg).replace("%prefix%", Plugin.getInstance().getPrefix());
                player.sendMessage(new TextComponent(formattedMessage));
            }
        } else {
            String singleMessage = configManager.getMessage(player, message);
            String formattedMessage = Plugin.getInstance().colorize(singleMessage).replace("%prefix%", Plugin.getInstance().getPrefix());
            player.sendMessage(new TextComponent(formattedMessage));
        }
    }




}






