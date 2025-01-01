package me.udnek.scamshieldmain;

import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.scamshieldmain.command.InfiniteRegen;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class ScamShieldMain extends JavaPlugin implements ResourcePackablePlugin {
    
    public static final int timeOfRestart = 4 * 60;
    
    private static ScamShieldMain instance;

    public static ScamShieldMain getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;


        new EventListener(ScamShieldMain.getInstance());

        getCommand("infiniteregen").setExecutor(new InfiniteRegen());

        //restartTimer(); todo FIX

        new BukkitRunnable(){
            @Override
            public void run() {
                World world = Bukkit.getWorld("world");
                world.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, 0);
                world.setGameRule(GameRule.SPAWN_RADIUS, 0);
            }
        }.runTaskLater(ScamShieldMain.getInstance(), 100);
    }

    private void restartTimer() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa"));
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable restart = () -> Bukkit.getServer().spigot().restart();

        int nowTime = now.getHour() * 60 + now.getMinute();
        int timeToReload;
        if (nowTime >= timeOfRestart) {
            timeToReload = 24 * 60 - (nowTime - timeOfRestart);
        }else {
            timeToReload = timeOfRestart - nowTime;
        }

        scheduler.schedule(restart, timeToReload, TimeUnit.MINUTES);
    }
}
