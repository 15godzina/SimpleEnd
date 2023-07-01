package pl.karol.end;

import org.bukkit.plugin.java.JavaPlugin;
import pl.karol.end.configuration.ConfigurationFactory;
import pl.karol.end.configuration.PluginConfiguration;
import pl.karol.end.listener.WaterListener;

import java.io.File;


public class EndPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        final ConfigurationFactory configurationFactory = new ConfigurationFactory();
        final PluginConfiguration pluginConfiguration = configurationFactory.create(PluginConfiguration.class, new File(this.getDataFolder(), "config.yml"), true);

        getServer().getPluginManager().registerEvents(new WaterListener(pluginConfiguration, this), this);
    }

    @Override
    public void onDisable() {

    }
}
