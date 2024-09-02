package dev.m7wq.qguilds.proxy.utils.config;
import net.md_5.bungee.config.Configuration;

import java.io.File;



    public interface Config {
        boolean exists();

        void delete();

        Configuration getConfig();

        void save();

        File getFile();

}
