package me.udnek.scamshieldmain.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class InfiniteRegen implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return false;

        if (!player.hasPotionEffect(PotionEffectType.INSTANT_HEALTH)){
            player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, -1, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, -1, 1, false, false));
        }
        else {
            player.removePotionEffect(PotionEffectType.INSTANT_HEALTH);
            player.removePotionEffect(PotionEffectType.SATURATION);
        }
        return true;
    }
}

