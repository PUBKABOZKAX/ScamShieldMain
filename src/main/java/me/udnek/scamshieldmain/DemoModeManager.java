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
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import me.udnek.scamshieldmain.effect.Effects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
            System.out.println(player.getName() + " is not whitelisted");
            setDemo(player);
        } else {
            removeDemo(player);
        }
        SuperVanish vanish = SuperVanish.getPlugin(SuperVanish.class);
        if (isDemo(player)){
            vanish.getVisibilityChanger().hidePlayer(player);
            Effects.DISABLE_INTERACTION.applyInvisible(player, -1, 0);

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setGameMode(GameMode.ADVENTURE);
                    vanish.getVisibilityChanger().hidePlayer(player);
                    player.setAllowFlight(true);
                }
            }.runTaskLater(ScamShieldMain.getInstance(), 20*2);

            new BukkitRunnable() {
                final Location spawnLocation = player.getWorld().getSpawnLocation();
                final Location spawnLocationY0 = spawnLocation.clone().subtract(0, spawnLocation.y(), 0);
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
                    if (spawnLocationY0.distanceSquared(playerLocationY0) < Math.pow(MAX_DISTANCE_FROM_CENTER, 2)) return;

                    Vector inRadiusDistance = playerLocationY0.subtract(spawnLocationY0).toVector().normalize().multiply(MAX_DISTANCE_FROM_CENTER);
                    Location tpLocation = spawnLocationY0.clone().add(inRadiusDistance).setDirection(playerLocationY0.getDirection());
                    tpLocation.setY(player.getLocation().y());
                    player.teleport(tpLocation);
                    player.sendMessage(TO_FAR_MESSAGE);
                }
            }.runTaskTimer(ScamShieldMain.getInstance(), 20*2, 20*2);


        } else {
            player.removePotionEffect(Effects.DISABLE_INTERACTION.getBukkitType());
            if (player.isOp()) return;
            player.setAllowFlight(false);
            vanish.getVisibilityChanger().showPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}



















