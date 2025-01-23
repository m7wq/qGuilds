package dev.m7wq.qguilds.cooldown;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Cooldown {

    @Getter
    long setTagCooldown;
    @Getter @Setter
    long setTagColorCooldown;
    @Getter @Setter
    long renameCooldown;

    public void setSetTagCooldown(long setTagCooldown){
        this.setTagCooldown =  setTagCooldown;
    }

}
