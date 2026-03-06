package me.udnek.scamshieldmain;

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import de.myzelyam.api.vanish.VanishAPI;
import de.myzelyam.supervanish.SuperVanish;
import me.udnek.coreu.util.LogUtils;
import me.udnek.coreu.util.SelfRegisteringListener;
import me.udnek.scamshieldmain.effect.Effects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public class DemoModeManager extends SelfRegisteringListener {

    public static final int MAX_DISTANCE_FROM_CENTER = 150;

    public static final Component TG_MESSAGE =
            Component.text("Напишите в тг " )
                    .append(Component.text("@dezzo4q (*клик*)")
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, ClickEvent.Payload.string("https://t.me/dezzo4q")))
                                    .color(NamedTextColor.AQUA).decorate(TextDecoration.UNDERLINED)
                    )
                    .append(Component.text(" для добавления в вайтлист"));

    public static final Component TO_FAR_MESSAGE =
            Component.text("Вы не можете уйти дальше чем " + MAX_DISTANCE_FROM_CENTER + " блоков от спавна!")
            .color(NamedTextColor.RED);


    private static @Nullable DemoModeManager instance;

    private final List<Player> players = new ArrayList<>();

    public DemoModeManager(Plugin plugin) {super(plugin);}

    public static DemoModeManager getInstance() {
        if (instance == null) instance = new DemoModeManager(ScamShieldMain.getInstance());
        return instance;
    }

    public boolean isDemo(Player player){return players.contains(player);}
    public void setDemo(Player player){if (!isDemo(player)) players.add(player);}
    public void removeDemo(Player player){players.remove(player);}

    @EventHandler
    public void onPortalTeleport(EntityPortalEvent event){
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (isDemo((Player) event.getEntity())) event.setCancelled(true);
    }

    @EventHandler
    public void onLogin(ProfileWhitelistVerifyEvent event){
        event.setWhitelisted(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (!player.isWhitelisted()) {
            LogUtils.log(ScamShieldMain.getInstance(), player.getName() + " is not whitelisted");
            setDemo(player);
        } else {
            removeDemo(player);
        }
        SuperVanish vanish = VanishAPI.getPlugin();
        if (isDemo(player)){
            vanish.getVisibilityChanger().hidePlayer(player);
            Effects.DISABLE_INTERACTION.applyInvisible(player, -1, 0);
            Effects.TEMPERATURE_INVULNERABILITY.applyInvisible(player, -1, 0);

            final Location spawnLocation = player.getWorld().getSpawnLocation();
            final Location spawnLocationY0 = spawnLocation.clone().subtract(0, spawnLocation.y(), 0);

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setGameMode(GameMode.ADVENTURE);
                    vanish.getVisibilityChanger().hidePlayer(player);
                    player.setAllowFlight(true);
                    WorldBorder worldBorder = Bukkit.createWorldBorder();
                    worldBorder.setCenter(spawnLocation);
                    worldBorder.setSize(MAX_DISTANCE_FROM_CENTER*2);
                    player.setWorldBorder(worldBorder);
                }
            }.runTaskLater(ScamShieldMain.getInstance(), 20*2);

            new BukkitRunnable() {

                int count = -1;
                @Override
                public void run() {
                    count ++;
                    if (!isDemo(player)) {
                        player.kick();
                        cancel();
                        return;
                    }
                    if (!player.isOnline()){
                        cancel();
                        return;
                    }

                    if (count % 6 == 0){
                        player.sendMessage(TG_MESSAGE);
                    }

                    Location playerLocationY0 = player.getLocation();
                    playerLocationY0.setY(0);

                    BoundingBox boundingBox = BoundingBox.of(spawnLocationY0, MAX_DISTANCE_FROM_CENTER, 999999999, MAX_DISTANCE_FROM_CENTER);
                    if (boundingBox.contains(playerLocationY0.toVector())) return;

                    RayTraceResult rayTrace = boundingBox.rayTrace(playerLocationY0.toVector(), boundingBox.getCenter().subtract(playerLocationY0.toVector()), 99999);
                    if (rayTrace == null){
                        player.teleport(spawnLocation);
                    } else {
                        playerLocationY0.set(rayTrace.getHitPosition().getX(), player.getLocation().getY(), rayTrace.getHitPosition().getZ());
                        player.teleport(playerLocationY0);
                    }

                    player.sendMessage(TO_FAR_MESSAGE);
                }
            }.runTaskTimer(ScamShieldMain.getInstance(), 20*2, 20*2);


        } else {
            Effects.TEMPERATURE_INVULNERABILITY.remove(player);
            Effects.DISABLE_INTERACTION.remove(player);
            player.setWorldBorder(null);
            if (player.isOp()) return;
            player.setAllowFlight(false);
            vanish.getVisibilityChanger().showPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}



















