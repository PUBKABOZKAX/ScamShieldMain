package me.udnek.scamshieldmain;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import de.myzelyam.supervanish.SuperVanish;
import de.myzelyam.supervanish.SuperVanishPlugin;
import de.myzelyam.supervanish.VanishPlayer;
import de.myzelyam.supervanish.commands.subcommands.VanishedList;
import de.myzelyam.supervanish.visibility.VanishStateMgr;
import me.udnek.itemscoreu.util.LogUtils;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import me.udnek.scamshieldmain.effect.Effects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DemoModeManager extends SelfRegisteringListener {

    public static final int MAX_DISTANCE_FROM_CENTER = 150;

    public static final Component TG_MESSAGE =
            Component.text("Напишите в тг " )
                    .append(Component.text("@dezzo4q (*клик*)")
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://t.me/dezzo4q"))
                                    .color(NamedTextColor.AQUA).decorate(TextDecoration.UNDERLINED)
                    )
                    .append(Component.text(" для добавления в вайтлист"));

    public static final Component TO_FAR_MESSAGE =
            Component.text("Вы не можете уйти дальше чем " + MAX_DISTANCE_FROM_CENTER + " блоков от спавна!")
            .color(NamedTextColor.RED);


    private static DemoModeManager instance;

    private final List<Player> players = new ArrayList<>();

    public DemoModeManager(@NotNull Plugin plugin) {super(plugin);}

    public static @NotNull DemoModeManager getInstance() {
        if (instance == null) instance = new DemoModeManager(ScamShieldMain.getInstance());
        return instance;
    }

    public boolean isDemo(@NotNull Player player){return players.contains(player);}
    public void setDemo(@NotNull Player player){if (!isDemo(player)) players.add(player);}
    public void removeDemo(@NotNull Player player){players.remove(player);}

    @EventHandler
    public void onPortalTeleport(EntityPortalEvent event){
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (isDemo((Player) event.getEntity())) event.setCancelled(true);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST){
            event.allow();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (!player.isWhitelisted()) {
            LogUtils.log(player.getName() + " is not whitelisted");
            setDemo(player);
        } else {
            removeDemo(player);
        }
        SuperVanish vanish = SuperVanish.getPlugin(SuperVanish.class);
        if (isDemo(player)){
            vanish.getVisibilityChanger().hidePlayer(player);
            Effects.DISABLE_INTERACTION.applyInvisible(player, -1, 0);

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
            player.removePotionEffect(Effects.DISABLE_INTERACTION.getBukkitType());
            player.setWorldBorder(null);
            if (player.isOp()) return;
            player.setAllowFlight(false);
            vanish.getVisibilityChanger().showPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}



















