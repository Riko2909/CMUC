package de.rikorick.main;

import de.rikorick.commands.TrackerCommand;
import de.rikorick.listener.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CMUC extends JavaPlugin {

    public static CMUC plugin;
    public static String prefix = "§c[§3CMUC§c]§r ";

    @Override
    public void onEnable() {

        plugin = this;

        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new Tracker(), this);

        getCommand("track").setExecutor(new TrackerCommand());


        Bukkit.getConsoleSender().sendMessage("§3§lCMUC loaded");

    }
}
