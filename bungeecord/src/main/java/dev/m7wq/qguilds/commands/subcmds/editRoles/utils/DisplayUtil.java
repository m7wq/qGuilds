package dev.m7wq.qguilds.commands.subcmds.editRoles.utils;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.roles.Role;
import dev.m7wq.qguilds.utils.ChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class DisplayUtil {
    public static void sendRolesList(ProxiedPlayer sender){

        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();
        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();

        String name = sender.getName();

        Guild guild = guildsManager.getPlayerGuild(name);

        List<String> rolesList = messages.getMessages(sender, ConfigurationManager.Message.ROLES_LIST);

        // lines
        String createdRoleLine = messages.getMessage(sender, ConfigurationManager.Message.CREATED_ROLE_LINE);
        String defaultRoleLine = messages.getMessage(sender, ConfigurationManager.Message.DEFAULT_ROLE_LINE);
        String emptyLine = messages.getMessage(sender, ConfigurationManager.Message.EMPTY_ROLES_LINE);

        // replacement
        String createRole = messages.getMessage(sender, ConfigurationManager.Message.CREATE_ROLE);
        String renameRole = messages.getMessage(sender, ConfigurationManager.Message.RENAME_ROLE);
        String recolorRole = messages.getMessage(sender, ConfigurationManager.Message.RECOLOR_ROLE);
        String permissions = messages.getMessage(sender, ConfigurationManager.Message.PERMISSION_EDITOR);
        String removeRole = messages.getMessage(sender, ConfigurationManager.Message.DELETE_ROLE);
        String upgradeArrow = messages.getMessage(sender, ConfigurationManager.Message.UPGRADE_ARROW);
        String degradeArrow = messages.getMessage(sender, ConfigurationManager.Message.DEGRADE_ARROW);


        /*
        * gr rename role
        * gr upgrade role
        * gr degrade role
        * gr recolor role
        * gr permissions role
        * gr remove role*/


        for (String str : rolesList){

            if (str.equalsIgnoreCase("{handleLines}")){

                for (int i = 0; i < guild.getRoles().size(); i++){

                    if (i==0 || i == guild.getRoles().size()-1){
                        Role role = guild.getRoles().get(i);

                        Component currentDefaultComponent = Component.text(defaultRoleLine);


                        currentDefaultComponent = currentDefaultComponent.replaceText(TextReplacementConfig.builder()

                                .matchLiteral("%role%")
                                .replacement(Component.text(
                                        Plugin.getInstance().colorize(role.getColor()+role.getName())
                                )).matchLiteral("%rename%").replacement(
                                        makeComponent(renameRole,"&eRename the role", "gr rename "+role.getName())
                                ).matchLiteral("%recolor%")
                                        .replacement(makeComponent(recolorRole,"&eRe-Color the role", "gr recolor "+role.getName()))
                                        .matchLiteral("permissions").replacement(makeComponent(permissions,"&eOpen Permissions Editor","gr permissions "+role.getName()))

                                .build()


                        );

                        Plugin.getInstance().adventure().player(sender).sendMessage(currentDefaultComponent);

                        continue;
                    }

                    Role role = guild.getRoles().get(i);

                    Component currentCreatedRoleComponent = Component.text(Plugin.getInstance().colorize(createRole)).replaceText(TextReplacementConfig.builder()

                            .matchLiteral("%role%")
                            .replacement(Component.text(
                                    Plugin.getInstance().colorize(role.getColor()+role.getName())
                            )).matchLiteral("%rename%").replacement(
                                    makeComponent(renameRole,"&eRename the role", "gr rename "+role.getName())
                            ).matchLiteral("%recolor%")
                            .replacement(makeComponent(recolorRole,"&eRe-Color the role", "gr recolor "+role.getName()))
                            .matchLiteral("%permissions%").replacement(makeComponent(permissions,"&eOpen Permissions Editor","gr permissions "+role.getName()))
                                    .matchLiteral("%arrows%")
                                    .replacement(getArrowsComponent(upgradeArrow,degradeArrow,role))
                                    .matchLiteral("%remove%")
                                    .replacement(makeComponent(removeRole,"&cRemove the role","gr remove "+role.getName()))
                            .build()


                    );

                    Plugin.getInstance().adventure().player(sender).sendMessage(currentCreatedRoleComponent);


                }

            }else {
                sender.sendMessage(
                        Plugin.getInstance().colorize(str.replace("%guild%",guild.getGuildName()).replace("%prefix%",Plugin.getInstance().getPrefix()))
                );
            }


        }

    }


    private static Component getArrowsComponent(String upgradeArrow, String degradeArrow, Role role){
        Component degradeComponent =  Component.text(Plugin.getInstance().colorize(degradeArrow))
                .clickEvent(ClickEvent.runCommand("gr degrade "+role.getName()))
                .hoverEvent(HoverEvent.showText(Component.text(Plugin.getInstance().colorize("&cDegrade the role"))));

        Component upgradeComponent = Component.text(
                        Plugin.getInstance().colorize(upgradeArrow)
                ).clickEvent(ClickEvent.runCommand("gr upgrade "+role.getName()))
                .hoverEvent(HoverEvent.showText(
                        Component.text(Plugin.getInstance().colorize("&aUpgrade the role"))
                ));

        Component arrowsComponent = upgradeComponent
                .append(Component.text(" "))
                .append(degradeComponent);

        return arrowsComponent;
    }

    private static Component makeComponent(String text, String hoverText, String command) {
        return Component.text(Plugin.getInstance().colorize(text))
                .hoverEvent(HoverEvent.showText(Component.text(Plugin.getInstance().colorize(hoverText))))
                .clickEvent(ClickEvent.runCommand(command));
    }

}
