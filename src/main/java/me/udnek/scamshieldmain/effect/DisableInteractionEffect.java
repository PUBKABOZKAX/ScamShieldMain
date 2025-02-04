package me.udnek.scamshieldmain.effect;

import me.udnek.itemscoreu.customeffect.ConstructableCustomEffect;
import me.udnek.scamshieldmain.ScamShieldMain;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisableInteractionEffect extends ConstructableCustomEffect {
    @Override
    public @NotNull PotionEffectTypeCategory getCategory() {
        return PotionEffectTypeCategory.NEUTRAL;
    }

    @Override
    public @Nullable PotionEffectType getVanillaDisguise() {
        return null;
    }

    @Override
    public void addAttributes(@NotNull AttributeConsumer consumer) {
        consumer.accept(Attribute.BLOCK_INTERACTION_RANGE,
                new NamespacedKey(ScamShieldMain.getInstance(), getRawId() + "_block"), -999999, AttributeModifier.Operation.ADD_NUMBER);
        consumer.accept(Attribute.ENTITY_INTERACTION_RANGE,
                new NamespacedKey(ScamShieldMain.getInstance(), getRawId() + "_entity"), -999999, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public @NotNull String getRawId() {
        return "disable_interaction";
    }
}
