package me.udnek.scamshieldmain;

import me.udnek.itemscoreu.customevent.InitializationEvent;
import me.udnek.itemscoreu.customregistry.InitializationProcess;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EventListener extends SelfRegisteringListener {

    public static final String RESOURCEPACK_VERSION = "2.1.2";

    public EventListener(@NotNull JavaPlugin plugin) {super(plugin);}

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Bukkit.recipeIterator().forEachRemaining(recipe ->{
            if (recipe instanceof Keyed keyed){
                event.getPlayer().discoverRecipe(keyed.getKey());
            }
        });

        try {
            Properties properties = new Properties();
            FileInputStream inputStream = new FileInputStream("server.properties");
            properties.load(inputStream);
            inputStream.close();
            String resourcepack = properties.getProperty("resource-pack");
            if (resourcepack.isEmpty()) return;

            event.getPlayer().sendMessage(Component.translatable(
                    "resourcepack.scamshieldmain.check_for_installed."+RESOURCEPACK_VERSION,
                    "Ресурспак не установлен! *клик*"
            ).applyFallbackStyle(
                    Style.style().clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, resourcepack)).decorate(TextDecoration.UNDERLINED).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).build())
            );


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
