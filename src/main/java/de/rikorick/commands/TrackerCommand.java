package de.rikorick.commands;

import de.rikorick.listener.Tracker;
import de.rikorick.main.CMUC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;


public class TrackerCommand implements CommandExecutor {


    public static HashMap<Player, Player> players = new HashMap();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player && strings.length == 1) {
            Player player = (Player) commandSender;
            Player target = Bukkit.getPlayer(strings[0]);

            if (target != null) {

                if(players.containsValue(target)) {
                    players.remove(player, target);
                    player.sendMessage(CMUC.prefix + "§2Du trackst jetzt §4nicht§2 mehr §b" + target.getName());
                }else {
                    player.setCompassTarget(target.getLocation());
                    if (Tracker.stacker != null && Tracker.stacker.getType() == Material.COMPASS)
                        Tracker.schedulerId.put(player, Tracker.track(player));
                    players.put(player, target);
                    player.sendMessage(CMUC.prefix + "§2Du trackst jetzt §b" + target.getName());
                }
                return true;
            }else {
                player.sendMessage(CMUC.prefix + "§4Spieler nicht verfügbar");
                return true;
            }
        }
        return false;
    }
}
