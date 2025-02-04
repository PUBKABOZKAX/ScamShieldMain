package me.udnek.scamshieldmain.effect;

import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.scamshieldmain.ScamShieldMain;

public class Effects {

    public static final DisableInteractionEffect DISABLE_INTERACTION = CustomRegistries.EFFECT.register(ScamShieldMain.getInstance(), new DisableInteractionEffect());
}
