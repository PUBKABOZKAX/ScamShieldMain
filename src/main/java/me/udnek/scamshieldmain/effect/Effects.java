package me.udnek.scamshieldmain.effect;

import me.udnek.coreu.custom.registry.CustomRegistries;
import me.udnek.scamshieldmain.ScamShieldMain;

public class Effects {

    public static final DisableInteractionEffect DISABLE_INTERACTION = CustomRegistries.EFFECT.register(ScamShieldMain.getInstance(), new DisableInteractionEffect());
    public static final TemperatureInvulnerability TEMPERATURE_INVULNERABILITY = CustomRegistries.EFFECT.register(ScamShieldMain.getInstance(), new TemperatureInvulnerability());
}
