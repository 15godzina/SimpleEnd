package pl.karol.end.configuration;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;

import java.io.File;

public class ConfigurationFactory {
    public <T extends OkaeriConfig> T create(Class<T> type, File file, boolean bukkitTypes) {
        return bukkitTypes ? ConfigManager.create(type, (config) -> {
            config.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit(), new SerdesCommons());
            config.withBindFile(file);
            config.saveDefaults();
            config.load();
        }) : ConfigManager.create(type, (config) -> {
            config.withConfigurer(new YamlBukkitConfigurer());
            config.withBindFile(file);
            config.saveDefaults();
            config.load();
        });
    }

    public <T extends OkaeriConfig> T createDefault(Class<T> type, File file) {
        return this.create(type, file,false);
    }
}
