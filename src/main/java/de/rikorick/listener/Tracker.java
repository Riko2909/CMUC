package de.rikorick.listener;


import de.rikorick.commands.TrackCommand;

import de.rikorick.main.CMUC;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;


public class Tracker implements Listener  {

    public static Map<Player, Integer> schedulerId = new HashMap<Player, Integer>();
    public static ItemStack stacker;

    public static int track(Player player) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(CMUC.plugin, new Runnable() {

            @Override
            public void run() {
                Player target = TrackCommand.getTarget(player);

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
        System.out.println(stacker);

        try {

            if(stacker != null && stacker.getType() == Material.COMPASS || player.getInventory().getItemInOffHand().getType() == Material.COMPASS) {

                if(schedulerId.get(player) != null && schedulerId.get(player) != -1){
                    return;
                }

                schedulerId.put(player, track(player));

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
    public void onPlayerDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        if (event.getItemDrop().getItemStack().getType() == Material.COMPASS) {
            Bukkit.getScheduler().cancelTask(schedulerId.get(player));
            schedulerId.replace(player, -1);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TrackCommand.untrackPlayer(player);
        if (schedulerId.get(player) != null){
            Bukkit.getScheduler().cancelTask(schedulerId.get(player));
            schedulerId.replace(player, -1);
        }
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            schedulerId.put((Player) entity, track((Player) entity));
        }

    }
}
