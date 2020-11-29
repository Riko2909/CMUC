package de.rikorick.main;

import de.rikorick.commands.TrackCommand;
import de.rikorick.config.CustomConfig;
import de.rikorick.listener.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CMUC extends JavaPlugin {

    public static CMUC plugin;
    public static String prefix = "§c[§3CMUC§c]§r ";

    @Override
    public void onEnable() {

        CMUC.plugin = this;

        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new Tracker(), this);

        getCommand("track").setExecutor(new TrackCommand());

        CustomConfig.loadConfig();


        Bukkit.getConsoleSender().sendMessage("§3§lCMUC loaded");

    }

    @Override
    public void onDisable() {
        CustomConfig.saveConfig();
    }

    public static CMUC getInstance() {
        return CMUC.plugin;
    }
}
