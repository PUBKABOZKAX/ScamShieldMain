package me.udnek.scamshieldmain;

import me.udnek.coreu.resourcepack.ResourcePackablePlugin;
import me.udnek.scamshieldmain.command.InfiniteRegen;
import me.udnek.scamshieldmain.effect.DisableInteractionEffect;
import me.udnek.scamshieldmain.effect.Effects;
import org.bukkit.Bukkit;
import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;


public final class ScamShieldMain extends JavaPlugin implements ResourcePackablePlugin {
    
    private static ScamShieldMain instance;

    public static ScamShieldMain getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;

        new EventListener(ScamShieldMain.getInstance());
        DemoModeManager demoModeInstance = DemoModeManager.getInstance();
        DisableInteractionEffect effect = Effects.DISABLE_INTERACTION;

        getCommand("infinite_regen").setExecutor(new InfiniteRegen());

        new BukkitRunnable(){
            @Override
            public void run() {
                World world = Bukkit.getWorld("world");
                world.setGameRule(GameRules.RESPAWN_RADIUS, 0);
            }
        }.runTaskLater(ScamShieldMain.getInstance(), 100);

        File file = new File("spigot.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("moved-too-quickly-multiplier", 9999);
    }

    @NotNull
    @Override
    public Priority getPriority() {
        return Priority.MAIN;
    }
}
