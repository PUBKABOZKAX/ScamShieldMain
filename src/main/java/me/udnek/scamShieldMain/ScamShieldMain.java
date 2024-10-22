package me.udnek.scamShieldMain;

import me.udnek.toughasnailsu.item.Items;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScamShieldMain extends JavaPlugin {

    @Override
    public void onEnable() {
        AdvancementRegistering.run();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
