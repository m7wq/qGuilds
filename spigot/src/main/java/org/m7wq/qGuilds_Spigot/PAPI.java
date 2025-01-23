package org.m7wq.qGuilds_Spigot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.m7wq.qGuilds_Spigot.data.PlayerData;

public class PAPI extends PlaceholderExpansion {
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

        PlayerData data = Plugin.getInstance().getManager().getDataMap().get(player.getName());

        switch (identifier.toLowerCase()) {

            case "name":
                return data.getGuildName();
            case "tag":
                return data.getTag();
            case "role":
                return data.getRoleName();
            case "role_color":
                return data.getRoleColor();
            default:
                return "";
        }
    }
}
