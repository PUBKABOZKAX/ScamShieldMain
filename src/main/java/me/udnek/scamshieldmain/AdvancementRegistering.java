package me.udnek.scamshieldmain;


import me.udnek.itemscoreu.customadvancement.AdvancementCriterion;
import me.udnek.itemscoreu.customadvancement.ConstructableCustomAdvancement;
import me.udnek.itemscoreu.customadvancement.CustomAdvancementDisplayBuilder;
import me.udnek.itemscoreu.customitem.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.udnek.itemscoreu.customadvancement.ConstructableCustomAdvancement.RequirementsStrategy.AND;
import static me.udnek.itemscoreu.customadvancement.ConstructableCustomAdvancement.RequirementsStrategy.OR;
import static me.udnek.rpgu.item.Items.*;
import static org.bukkit.Material.*;


public class AdvancementRegistering {
    private static final List<ConstructableCustomAdvancement> advancements = new ArrayList<>();

    public static void run() {

        ConstructableCustomAdvancement root = generator("root", FABRIC.getItem(), null);
        Objects.requireNonNull(root.getDisplay()).background("textures/block/cobblestone.png");
        root.addCriterion("tick", AdvancementCriterion.TICK);
        root.getDisplay().announceToChat(true);
        advancements.add(root);

        ConstructableCustomAdvancement woodenTools = generator("first_tools", new ItemStack(Material.WOODEN_PICKAXE), root);
        addCriteria(woodenTools, OR, WOODEN_PICKAXE, WOODEN_AXE, WOODEN_SHOVEL, WOODEN_HOE, WOODEN_SWORD);
        advancements.add(woodenTools);

        ConstructableCustomAdvancement fabric = generator("fabric", FABRIC.getItem(), woodenTools);
        addCriteria(fabric, AND, FABRIC);
        advancements.add(fabric);

        ConstructableCustomAdvancement leatherArmor = generator("leather_armor", new ItemStack(LEATHER_CHESTPLATE), fabric);
        addCriteria(leatherArmor, OR, LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS);
        advancements.add(leatherArmor);

        ConstructableCustomAdvancement ironArmor = generator("iron_armor", new ItemStack(IRON_CHESTPLATE), leatherArmor);
        addCriteria(ironArmor, OR, IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS);
        advancements.add(ironArmor);

        ConstructableCustomAdvancement flint = generator("flint", new ItemStack(FLINT), woodenTools);
        addCriteria(flint, AND, FLINT);
        advancements.add(flint);

        ConstructableCustomAdvancement flintTools = generator("flint_tools", FLINT_PICKAXE.getItem(), flint);
        addCriteria(flintTools, OR, FLINT_PICKAXE, FLINT_AXE, FLINT_SHOVEL, FLINT_HOE, FLINT_SWORD);
        advancements.add(flintTools);

        ConstructableCustomAdvancement ironTools = generator("iron_tools", new ItemStack(IRON_PICKAXE), flintTools);
        addCriteria(ironTools, OR, IRON_PICKAXE, IRON_AXE, IRON_SHOVEL, IRON_HOE, IRON_SWORD);
        advancements.add(ironTools);

        ConstructableCustomAdvancement blastFurnace = generator("blast_furnace", new ItemStack(BLAST_FURNACE), ironTools);
        addCriteria(blastFurnace, AND, BLAST_FURNACE);
        advancements.add(blastFurnace);

        ConstructableCustomAdvancement ferrudamEquipment = generator("ferrudam_equipment", FERRUDAM_PICKAXE.getItem(), blastFurnace);
        addCriteria(ferrudamEquipment, OR, FERRUDAM_PICKAXE, FERRUDAM_AXE, FERRUDAM_SHOVEL, FERRUDAM_HOE, FERRUDAM_SWORD, FERRUDAM_HELMET, FERRUDAM_CHESTPLATE, FERRUDAM_LEGGINGS, FERRUDAM_BOOTS);
        advancements.add(ferrudamEquipment);

        ConstructableCustomAdvancement netheriteEquipment = generator("netherite_equipment", new ItemStack(NETHERITE_PICKAXE), ferrudamEquipment);
        addCriteria(netheriteEquipment, OR, NETHERITE_PICKAXE, NETHERITE_AXE, NETHERITE_SHOVEL, NETHERITE_HOE, NETHERITE_SWORD, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS);
        advancements.add(netheriteEquipment);

        ////////////////////////////////////////////////////////////

        ConstructableCustomAdvancement glass = generator("tough_as_nails_root", new ItemStack(GLASS), root);
        addCriteria(glass, AND, GLASS);
        advancements.add(glass);

        ConstructableCustomAdvancement bottle = generator("bottle", new ItemStack(GLASS_BOTTLE), glass);
        addCriteria(bottle, AND, GLASS_BOTTLE);
        advancements.add(bottle);

        //////////////////////////////////////////////////////////

        ConstructableCustomAdvancement totemSaving = generator("totem_saving", new ItemStack(TOTEM_OF_UNDYING), root);
        addCriteria(totemSaving, AND, TOTEM_OF_UNDYING);
        advancements.add(totemSaving);

        ConstructableCustomAdvancement fishermanSnorkel = generator("fisherman_snorkel", new ItemStack(GLASS_BOTTLE), totemSaving);
        addCriteria(fishermanSnorkel, AND, GLASS);
        advancements.add(fishermanSnorkel);

        ConstructableCustomAdvancement flowerWreath = generator("flower_wreath", new ItemStack(GLASS_BOTTLE), totemSaving);
        addCriteria(flowerWreath, AND, GLASS);
        advancements.add(flowerWreath);

        register();
    }

    private static @NotNull ConstructableCustomAdvancement generator(@NotNull String key, @NotNull ItemStack itemStack, @Nullable ConstructableCustomAdvancement parent){
        ConstructableCustomAdvancement advancement = new ConstructableCustomAdvancement(new NamespacedKey(ScamShieldMain.getInstance(), key));
        CustomAdvancementDisplayBuilder displayAdvancement = new CustomAdvancementDisplayBuilder(itemStack);
        displayAdvancement.title(Component.translatable("advancement.scamshieldmain."+ key +".title"));
        displayAdvancement.description(Component.translatable("advancement.scamshieldmain." + key + ".description"));
        displayAdvancement.announceToChat(false);
        advancement.display(displayAdvancement);
        advancement.parent(parent);

        return advancement;
    }

    private  static void addCriteria(@NotNull ConstructableCustomAdvancement advancement, @NotNull ConstructableCustomAdvancement.RequirementsStrategy strategy, @NotNull Material ...materials){
        for (Material material:materials){
            advancement.addCriterion(material.toString().toLowerCase(), AdvancementCriterion.INVENTORY_CHANGE.create(new ItemStack(material)));
        }
        advancement.requirementsStrategy(strategy);
    }

    private  static void addCriteria(@NotNull ConstructableCustomAdvancement advancement, @NotNull ConstructableCustomAdvancement.RequirementsStrategy strategy, @NotNull CustomItem...customItems){
        for (CustomItem customItem:customItems){
            advancement.addCriterion(customItem.getId(), AdvancementCriterion.INVENTORY_CHANGE.create(customItem.getItem()));
        }
        advancement.requirementsStrategy(strategy);
    }

    private static void register() {
        for (ConstructableCustomAdvancement advancement : advancements) {
            advancement.register();
        }
    }
}
