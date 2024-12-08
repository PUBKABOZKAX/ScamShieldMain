package me.udnek.scamshieldmain;

import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.scamshieldmain.command.InfiniteRegen;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScamShieldMain extends JavaPlugin implements ResourcePackablePlugin {

    private static ScamShieldMain instance;

    public static ScamShieldMain getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;

        new EventListener(ScamShieldMain.getInstance());

        getCommand("infiniteregen").setExecutor(new InfiniteRegen());
    }
}
