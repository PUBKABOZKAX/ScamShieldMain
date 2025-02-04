package me.udnek.scamshieldmain;

import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.scamshieldmain.command.InfiniteRegen;
import me.udnek.scamshieldmain.effect.DisableInteractionEffect;
import me.udnek.scamshieldmain.effect.Effects;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public final class ScamShieldMain extends JavaPlugin implements ResourcePackablePlugin {
    
    private static ScamShieldMain instance;

    public static ScamShieldMain getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;

        new EventListener(ScamShieldMain.getInstance());
        DemoModeManager.getInstance();
        DisableInteractionEffect effect = Effects.DISABLE_INTERACTION;

        getCommand("infinite_regen").setExecutor(new InfiniteRegen());

        new BukkitRunnable(){
            @Override
            public void run() {
                World world = Bukkit.getWorld("world");
                world.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, 0);
                world.setGameRule(GameRule.SPAWN_RADIUS, 0);
            }
        }.runTaskLater(ScamShieldMain.getInstance(), 100);
    }
}
