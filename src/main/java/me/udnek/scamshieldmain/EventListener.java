package me.udnek.scamshieldmain;

import me.udnek.itemscoreu.customevent.InitializationEvent;
import me.udnek.itemscoreu.customregistry.InitializationProcess;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class EventListener extends SelfRegisteringListener {
    public EventListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Bukkit.recipeIterator().forEachRemaining(recipe ->{
            if (recipe instanceof Keyed keyed){
                event.getPlayer().discoverRecipe(keyed.getKey());
            }
        });
    }

    @EventHandler
    public void onWorldLoaded(WorldLoadEvent event){
        Bukkit.spigot().getSpigotConfig().set("settings.moved-too-quickly-multiplier", 9999);
        try {
            Bukkit.spigot().getSpigotConfig().save("spigot.yml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onInit(InitializationEvent event) {
        if (event.getStep() == InitializationProcess.Step.AFTER_REGISTRIES_INITIALIZATION) AdvancementRegistering.run();
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event){
        if (event.getEntityType() == EntityType.CREEPER) event.blockList().clear();
    }
}
