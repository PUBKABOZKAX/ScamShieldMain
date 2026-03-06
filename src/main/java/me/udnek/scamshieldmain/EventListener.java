package me.udnek.scamshieldmain;

import com.google.gson.JsonParser;
import me.udnek.coreu.custom.event.InitializationEvent;
import me.udnek.coreu.custom.event.ResourcepackInitializationEvent;
import me.udnek.coreu.custom.registry.InitializationProcess;
import me.udnek.coreu.resourcepack.file.RpJsonFile;
import me.udnek.coreu.util.SelfRegisteringListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@NullMarked
public class EventListener extends SelfRegisteringListener {

    public static final String RESOURCEPACK_VERSION = "2.1.5";

    public static final String RESOURCEPACK_MESSAGE = "resourcepack.scamshieldmain.check_for_installed."+RESOURCEPACK_VERSION;

    public EventListener(JavaPlugin plugin) {super(plugin);}

    @EventHandler
    public void onResourcepack(ResourcepackInitializationEvent event){
        event.addFile(new RpJsonFile(this,
                "assets/scamshieldmain/lang/en_us.json",
                JsonParser.parseString("""
                {
                    "%message%": "§a§lРесурспак установлен!"
                }
                """.replace("%message%", RESOURCEPACK_MESSAGE)).getAsJsonObject()));
    }

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
                    RESOURCEPACK_MESSAGE,
                    "Ресурспак не установлен! *клик*"
            ).applyFallbackStyle(
                    Style.style().clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, ClickEvent.Payload.string(resourcepack)))
                            .decorate(TextDecoration.UNDERLINED).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).build())
            );


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @EventHandler
    public void onWorldLoaded(WorldLoadEvent event){
        File spigotFile = new File("spigot.yml");
        FileConfiguration spigotConfig = YamlConfiguration.loadConfiguration(spigotFile);

        spigotConfig.set("settings.moved-too-quickly-multiplier", 9999.0);

        try {
            spigotConfig.save(spigotFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save spigot.yml", e);
        }
        /*Bukkit.getServer()
        Bukkit.spigot().getSpigotConfig().set("settings.moved-too-quickly-multiplier", 9999);
        try {
            Bukkit.spigot().getSpigotConfig().save("spigot.yml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    @EventHandler
    public void onInit(InitializationEvent event) {
        if (event.getStep() == InitializationProcess.Step.AFTER_GLOBAL_INITIALIZATION) AdvancementRegistering.run();
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event){
        if (event.getEntityType() == EntityType.CREEPER) event.blockList().clear();
    }
}
