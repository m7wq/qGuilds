package dev.m7wq.qguilds.utils.config;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class QConfig {

    @Getter
    public Configuration config;
    private final File configFile;

    public QConfig(File dataFolder, String name) {
        this.configFile = new File(dataFolder, name);


        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }


        if (!configFile.exists()) {
            try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(name)) {
                if (resourceStream == null) {
                }
                Files.copy(resourceStream, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
