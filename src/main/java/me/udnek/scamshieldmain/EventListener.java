package me.udnek.scamshieldmain;

import me.udnek.itemscoreu.customevent.InitializationEvent;
import me.udnek.itemscoreu.util.InitializationProcess;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener extends SelfRegisteringListener {
    public EventListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInit(InitializationEvent event) {
        if (event.getStep() == InitializationProcess.Step.END) AdvancementRegistering.run();
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event){
        if (event.getEntityType() == EntityType.CREEPER) event.blockList().clear();
    }
}
