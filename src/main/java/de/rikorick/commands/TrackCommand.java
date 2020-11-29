package de.rikorick.commands;

import de.rikorick.listener.Tracker;
import de.rikorick.main.CMUC;
import lombok.extern.log4j.Log4j;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static de.rikorick.listener.Tracker.schedulerId;

public class TrackCommand implements CommandExecutor {

    public static Map<Player, Player> players = new HashMap<Player, Player>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player && strings.length == 1) {
            Player player = (Player) commandSender;

            if (strings[0].equals("rl")){
                Bukkit.getScheduler().cancelTasks(CMUC.getInstance());
            }

            Player target = Bukkit.getPlayer(strings[0]);

            if (target != null) {

                System.out.println(players);

                //Wenn der Player schon jemaden Trackt dann:
                if(players.containsKey(player)){
                    if(players.get(player) == target) {
                        untrackPlayer(player);
                        player.sendMessage(CMUC.prefix + "§2Du trackst jetzt §4NICHT §2mehr §b" + target.getName());
                        return true;
                    }
                    untrackPlayer(player);
                }

                trackPlayer(player, target);
                player.sendMessage(CMUC.prefix + "§2Du trackst jetzt §b" + target.getName());

            } else {
                player.sendMessage(CMUC.prefix + "§4Spieler nicht verfügbar");
            }
            return true;
        }
        return false;
    }

    // Track the Player
    public void trackPlayer(Player player, Player target) {
        player.setCompassTarget(target.getLocation());

        ItemStack item = player.getInventory().getItemInMainHand();

        System.out.println("Item: " + item);

        if(item.getType() == Material.COMPASS) {
            schedulerId.put(player, Tracker.track(player));
        }

        players.put(player, target);
    }

    // Untrack the Player
    public static void untrackPlayer(Player playerKey){

        players.remove(playerKey);
        if (schedulerId.get(playerKey) != null){
            Bukkit.getScheduler().cancelTask(schedulerId.get(playerKey));
            schedulerId.remove(playerKey);
        }


        for (Map.Entry<Player, Player> entry : players.entrySet()) {
            if (entry.getValue() == playerKey) {
                //entry.getKey() -> TargetKey
                Bukkit.getScheduler().cancelTask(schedulerId.get(entry.getKey()));
                schedulerId.remove(entry.getKey(), playerKey);
                players.remove(entry.getKey(), playerKey);
            }
        }
    }

    public static Player getTarget(Player player){
        return players.get(player);
    }
}
