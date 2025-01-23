package dev.m7wq.qguilds.events.custom;

import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChangeHappenListener implements Listener {

    @EventHandler
    public void onChangeHappen(ChangeHappenEvent e){
        if (e.getMaster() != null){
            BungeeHook.sendData(e.getMaster());
        }

        if (e.getMember()!=null){
            BungeeHook.sendData(e.getMember());
        }
    }
}
