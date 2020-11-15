package de.rikorick.listener;


import de.rikorick.commands.TrackerCommand;

import de.rikorick.main.CMUC;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;



public class Tracker implements Listener  {

    public static HashMap<Player, Integer> schedulerId = new HashMap();
    public static ItemStack stacker;

    public static int track(Player player) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(CMUC.plugin, new Runnable() {

            @Override
            public void run() {
                Player target = TrackerCommand.players.get(player);

                if (target != null){
                    player.setCompassTarget(target.getLocation());
                    player.sendMessage(CMUC.prefix + "§2" + target.getName() + "§8 \u00BB §b" + ((int) player.getLocation().distance(target.getLocation()) +" §bBlöcke"));
                }else {
                    Bukkit.getScheduler().cancelTask(schedulerId.get(player));
                }
            }
        }, 20*2, 20*2);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        stacker = player.getInventory().getItem(event.getNewSlot());

        try {

            if(stacker != null && stacker.getType() == Material.COMPASS || player.getInventory().getItemInOffHand().getType() == Material.COMPASS) {

                if(schedulerId.get(player) != null && schedulerId.get(player) != -1){
                    return;
                }

                int id = track(player);
                schedulerId.put(player, id);

            }else{
                if(schedulerId.get(player) != null && schedulerId.get(player) != -1){
                    Bukkit.getScheduler().cancelTask(schedulerId.get(player));
                    schedulerId.replace(player, -1);
                }
            }

        } catch (Exception exception){
            player.sendMessage("§4Error, siehe Konsole");
            Bukkit.getConsoleSender().sendMessage(String.valueOf(exception));
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        if (event.getItemDrop().getItemStack().getType() == Material.COMPASS) {
            Bukkit.getScheduler().cancelTask(schedulerId.get(player));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (schedulerId.get(player) != null)
            Bukkit.getScheduler().cancelTask(schedulerId.get(player));
    }
}
