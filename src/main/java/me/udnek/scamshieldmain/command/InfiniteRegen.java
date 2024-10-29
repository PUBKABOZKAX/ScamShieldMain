package me.udnek.scamshieldmain.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfiniteRegen implements @Nullable CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return false;
        if (args.length != 1 ) return false;
        if (args[0].equals("on")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, -1, 1, false, false));
            return true;
        }
        else if (args[0].equals("off")) {
            player.removePotionEffect(PotionEffectType.INSTANT_HEALTH);
            return true;
        }
        return false;
    }
}

