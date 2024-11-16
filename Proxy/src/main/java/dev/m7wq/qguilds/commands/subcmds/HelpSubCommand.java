package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class HelpSubCommand implements SubCommand {

    ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();


    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {


        ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();

        List<String> helpMessage = cfg.getMessagesConfig().getStringList("help-message");

        Component helpComponent = Component.empty();

        for (String str : helpMessage) {


            str = str.replace("%prefix%", Plugin.getInstance().getPrefix());


            Component lineComponent = Component.text(Plugin.getInstance().colorize(str));


            lineComponent = lineComponent
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%help%")
                            .replacement(ChatUtil.makeComponent(get("help"), "&e/guild help", "/g help"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%mypermissions%")
                            .replacement(ChatUtil.makeComponent(get("mypermissions"), "&e/guild mypermissions", "/g mypermissions"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%kick%")
                            .replacement(ChatUtil.makeComponent(get("kick"), "&e/guild kick", "/g kick"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%unmute%")
                            .replacement(ChatUtil.makeComponent(get("unmute"), "&e/guild unmute", "/g unmute"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%mute%")
                            .replacement(ChatUtil.makeComponent(get("mute"), "&e/guild mute", "/g mute"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%list%")
                            .replacement(ChatUtil.makeComponent(get("list"), "&e/guild list", "/g list"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%rename%")
                            .replacement(ChatUtil.makeComponent(get("rename"), "&e/guild rename", "/g rename"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%settagcolor%")
                            .replacement(ChatUtil.makeComponent(get("settagcolor"), "&e/guild settagcolor", "/g settagcolor"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%settag%")
                            .replacement(ChatUtil.makeComponent(get("settag"), "&e/guild settag", "/g settag"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%promote%")
                            .replacement(ChatUtil.makeComponent(get("promote"), "&e/guild promote", "/g promote"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%demote%")
                            .replacement(ChatUtil.makeComponent(get("demote"), "&e/guild demote", "/g demote"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%setrole%")
                            .replacement(ChatUtil.makeComponent(get("setrole"), "&e/guild setrole", "/g setrole"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%transfer%")
                            .replacement(ChatUtil.makeComponent(get("transfer"), "&e/guild transfer", "/g transfer"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%toggle%")
                            .replacement(ChatUtil.makeComponent(get("toggle"), "&e/guild toggle", "/g toggle"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%chat%")
                            .replacement(ChatUtil.makeComponent(get("chat"), "&e/guild chat", "/g chat"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%leave%")
                            .replacement(ChatUtil.makeComponent(get("leave"), "&e/guild leave", "/g leave"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%ignore%")
                            .replacement(ChatUtil.makeComponent(get("ignore"), "&e/guild ignore", "/g ignore"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%accept%")
                            .replacement(ChatUtil.makeComponent(get("accept"), "&e/guild accept", "/g accept"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%create%")
                            .replacement(ChatUtil.makeComponent(get("create"), "&e/guild create", "/g create"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%disband%")
                            .replacement(ChatUtil.makeComponent(get("disband"), "&e/guild disband", "/g disband"))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("%invite%")
                            .replacement(ChatUtil.makeComponent(get("invite"), "&e/guild invite", "/g invite"))
                            .build());


            Plugin.getInstance().adventure().player(sender).sendMessage(lineComponent);

        }




    }



    public String get(String string){
        return cfg.getMessagesConfig().getString(string);

    }
}
