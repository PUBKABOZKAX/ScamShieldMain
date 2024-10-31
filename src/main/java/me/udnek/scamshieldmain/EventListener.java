package me.udnek.scamshieldmain;

import me.udnek.itemscoreu.customevent.InitializationEvent;
import me.udnek.itemscoreu.util.InitializationProcess;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import org.bukkit.event.EventHandler;
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
    public void deleteSeed(PlayerCommandPreprocessEvent event){
        if (event.getMessage().equals("/seed")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Ой ты чё ахуел? Команда работает в консоли)))", "Если нет доступа то зачем пишиешь это?");
        }
    }
}
