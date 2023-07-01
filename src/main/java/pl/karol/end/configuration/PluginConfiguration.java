package pl.karol.end.configuration;

import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;


public class PluginConfiguration extends OkaeriConfig {

    public Location voidSpawn = new Location(
            Bukkit.getWorld("world_the_end"),
            5,
            100,
            45
    );
}
