package dev.m7wq.qguilds;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.roles.RoleType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Identifiers extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "qguilds";
    }

    @Override
    public @NotNull String getAuthor() {
        return "m7wq";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }


  @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null || identifier == null) {
            return null;
        }

        GuildsManager manager = Plugin.getInstance().getGuildsManager();
        String ign = player.getName();

        switch (identifier.toLowerCase()) {
            case "name":
                return manager.getPlayerGuild(ign).getGuildName();
            case "display_name":
                return manager.getPlayerGuild(ign).getDisplayName();
            case "tag":
                return manager.getPlayerGuild(ign).getFormatedGuildTag();
            case "role":
                return manager.getPlayerRole(ign);
            case "display_role":
                return RoleType.getByName(manager.getPlayerRole(ign)).getDisplayRole();

            default:
                return "";    
        }
    }


}
