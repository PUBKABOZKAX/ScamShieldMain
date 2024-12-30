package me.udnek.scamshieldmain;


import me.udnek.itemscoreu.customadvancement.AdvancementCriterion;
import me.udnek.itemscoreu.customadvancement.ConstructableCustomAdvancement;
import me.udnek.itemscoreu.customadvancement.CustomAdvancementDisplayBuilder;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.util.ItemUtils;
import me.udnek.toughasnailsu.util.Tags;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static me.udnek.itemscoreu.customadvancement.CustomAdvancementContainer.RequirementsStrategy.AND;
import static me.udnek.itemscoreu.customadvancement.CustomAdvancementContainer.RequirementsStrategy.OR;
import static me.udnek.rpgu.item.Items.*;
import static me.udnek.toughasnailsu.item.Items.*;
import static org.bukkit.Material.*;


public class AdvancementRegistering {
    private static final Set<ConstructableCustomAdvancement> advancements = new HashSet<>();

    public static void run() {

        ConstructableCustomAdvancement root = generate("root", FABRIC.getItem(), null);
        Objects.requireNonNull(root.getDisplay()).background("textures/block/cobblestone.png");
        root.addCriterion("tick", AdvancementCriterion.TICK);
        advancements.add(root);


        //// TANU /////////////////////////////////
        ConstructableCustomAdvancement tanu = generate("tanu", new ItemStack(SKELETON_SKULL), root);
        tanu.addCriterion("tick", AdvancementCriterion.TICK);

        ConstructableCustomAdvancement bottle = generate("bottle", DRINKING_GLASS_BOTTLE.getItem(), tanu);
        addCriteria(bottle, AND, DRINKING_GLASS_BOTTLE); advancements.add(bottle);

        ConstructableCustomAdvancement dirtyBottle = generate("dirty_bottle", DIRTY_WATER_BOTTLE.getItem(), bottle);
        addCriteria(dirtyBottle, AND, DIRTY_WATER_BOTTLE); advancements.add(dirtyBottle);

        ConstructableCustomAdvancement pureBottle = generate("pure_bottle", PURE_WATER_BOTTLE.getItem(), dirtyBottle);
        addCriteria(pureBottle, AND, PURE_WATER_BOTTLE); advancements.add(pureBottle);

        ConstructableCustomAdvancement boilingBottle = generate("boiling_bottle", BOILING_WATER_BOTTLE.getItem(), pureBottle);
        addCriteria(boilingBottle, AND, BOILING_WATER_BOTTLE); advancements.add(boilingBottle);

        ConstructableCustomAdvancement inWater = generate("in_water", new ItemStack(WATER_BUCKET), tanu);
        inWater.addCriterion("in_water", AdvancementCriterion.ENTER_BLOCK.create(WATER));

        ConstructableCustomAdvancement campfire = generate("campfire", new ItemStack(CAMPFIRE), tanu);
        addCriteria(campfire, AND, CAMPFIRE); advancements.add(campfire);

        ConstructableCustomAdvancement teaBottle = generate("tea", GREEN_SWEET_BERRY_TEA_BOTTLE.getItem(), boilingBottle);
        addCriteria(teaBottle, OR, Tags.TEAS.getValues().toArray(new CustomItem[0])); advancements.add(teaBottle);
        teaBottle.addFakeParent(campfire);

        ConstructableCustomAdvancement leatherArmorTANU = generate("tanu_leather_armor", new ItemStack(LEATHER_CHESTPLATE), campfire);
        addCriteria(leatherArmorTANU, OR, LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS);
        leatherArmorTANU.addFakeParent(teaBottle);

        ConstructableCustomAdvancement chainmailArmor = generate("chainmail_armor", new ItemStack(CHAINMAIL_CHESTPLATE), inWater);
        addCriteria(chainmailArmor, OR, CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS);
        chainmailArmor.addFakeParent(campfire);

        ConstructableCustomAdvancement juiceBottle = generate("juice", SWEET_BERRY_JUICE_BOTTLE.getItem(), pureBottle);
        addCriteria(juiceBottle, OR, Tags.JUICES.getValues().toArray(new CustomItem[0]));
        juiceBottle.addFakeParent(bottle);
        juiceBottle.addFakeParent(chainmailArmor);

        ConstructableCustomAdvancement amethystBottle = generate(AMETHYST_WATER_BOTTLE.getItem(), juiceBottle);
        amethystBottle.addFakeParent(chainmailArmor);

        /////////// RPGU //////////////////

        ConstructableCustomAdvancement woodenTools = generate("first_tools", new ItemStack(WOODEN_PICKAXE), root);
        addCriteria(woodenTools, OR, WOODEN_PICKAXE, WOODEN_AXE, WOODEN_SHOVEL, WOODEN_HOE, WOODEN_SWORD); advancements.add(woodenTools);

        ConstructableCustomAdvancement fabric = generate("fabric", FABRIC.getItem(), woodenTools);
        addCriteria(fabric, AND, FABRIC); advancements.add(fabric);

        ConstructableCustomAdvancement leatherArmor = generate("leather_armor", new ItemStack(LEATHER_CHESTPLATE), fabric);
        addCriteria(leatherArmor, OR, LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS);

        ConstructableCustomAdvancement ironArmor = generate("iron_armor", new ItemStack(IRON_CHESTPLATE), leatherArmor);
        addCriteria(ironArmor, OR, IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS); advancements.add(ironArmor);

        ConstructableCustomAdvancement flint = generate("flint", new ItemStack(FLINT), woodenTools);
        addCriteria(flint, AND, FLINT); advancements.add(flint);

        ConstructableCustomAdvancement flintTools = generate("flint_tools", FLINT_PICKAXE.getItem(), flint);
        addCriteria(flintTools, OR, FLINT_PICKAXE, FLINT_AXE, FLINT_SHOVEL, FLINT_HOE, FLINT_SWORD); advancements.add(flintTools);

        ConstructableCustomAdvancement ironTools = generate("iron_tools", new ItemStack(IRON_PICKAXE), flintTools);
        addCriteria(ironTools, OR, IRON_PICKAXE, IRON_AXE, IRON_SHOVEL, IRON_HOE, IRON_SWORD); advancements.add(ironTools);

        ConstructableCustomAdvancement blastFurnace = generate("blast_furnace", new ItemStack(BLAST_FURNACE), ironTools);
        addCriteria(blastFurnace, AND, BLAST_FURNACE); advancements.add(blastFurnace);
        blastFurnace.addFakeParent(ironArmor);

        ConstructableCustomAdvancement ferrudamEquipment = generate("ferrudam_equipment", FERRUDAM_PICKAXE.getItem(), blastFurnace);
        addCriteria(ferrudamEquipment, OR, FERRUDAM_PICKAXE, FERRUDAM_AXE, FERRUDAM_SHOVEL, FERRUDAM_HOE, FERRUDAM_SWORD, FERRUDAM_HELMET, FERRUDAM_CHESTPLATE, FERRUDAM_LEGGINGS, FERRUDAM_BOOTS);
        advancements.add(ferrudamEquipment);

        ConstructableCustomAdvancement netheriteEquipment = generate("netherite_equipment", new ItemStack(NETHERITE_PICKAXE), ferrudamEquipment);
        addCriteria(netheriteEquipment, OR, NETHERITE_PICKAXE, NETHERITE_AXE, NETHERITE_SHOVEL, NETHERITE_HOE, NETHERITE_SWORD, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS);
        advancements.add(netheriteEquipment);

        //// MAGICAL /////////////////////////////////

        ConstructableCustomAdvancement esotericSalve = generate("esoteric_salve", ESOTERIC_SALVE.getItem(), woodenTools);
        addCriteria(esotericSalve, AND, ESOTERIC_SALVE);advancements.add(esotericSalve);

        ConstructableCustomAdvancement shamanTambourine = generate(SHAMAN_TAMBOURINE.getItem(), esotericSalve);

        ConstructableCustomAdvancement naturesStaff = generate(NATURES_STAFF.getItem(), shamanTambourine);
        ConstructableCustomAdvancement airElementalTome = generate(AIR_ELEMENTAL_TOME.getItem(), naturesStaff);


        ConstructableCustomAdvancement wolfArmor = generate("wolf_armor", WOLF_HELMET.getItem(), esotericSalve);
        addCriteria(wolfArmor, OR, WOLF_HELMET, WOLF_CHESTPLATE, WOLF_LEGGINGS, WOLF_BOOTS);advancements.add(wolfArmor);

        ConstructableCustomAdvancement phantomWing = generate(PHANTOM_WING.getItem(), naturesStaff);
        phantomWing.addFakeParent(wolfArmor);

        ConstructableCustomAdvancement phantomChestplate = generate(PHANTOM_CHESTPLATE.getItem(), phantomWing);

        ConstructableCustomAdvancement grimArmor = generate("grim_armor", GRIM_CHESTPLATE.getItem(), phantomChestplate);
        addCriteria(grimArmor, OR, GRIM_HELMET, GRIM_CHESTPLATE);

        ConstructableCustomAdvancement evocationRobe = generate(EVOCATION_ROBE.getItem(), phantomChestplate);

        ConstructableCustomAdvancement sphereOfBalance = generate("sphere_of_balance", SPHERE_OF_BALANCE.getItem(), esotericSalve);
        addCriteria(sphereOfBalance, AND, SPHERE_OF_BALANCE);advancements.add(sphereOfBalance);

        ConstructableCustomAdvancement witherWreath = generate("wither_wreath", WITHER_WREATH.getItem(), sphereOfBalance);
        addCriteria(witherWreath, AND, WITHER_WREATH);advancements.add(witherWreath);

        ConstructableCustomAdvancement sphereOfDiscord = generate("sphere_of_discord", SPHERE_OF_DISCORD.getItem(), witherWreath);
        addCriteria(sphereOfDiscord, AND, SPHERE_OF_DISCORD);advancements.add(sphereOfDiscord);

        ConstructableCustomAdvancement amethystDirk = generate("amethyst_dirk", AMETHYST_DIRK.getItem(), esotericSalve);
        addCriteria(amethystDirk, AND, AMETHYST_DIRK);advancements.add(amethystDirk);

        ConstructableCustomAdvancement nautilusCore = generate("nautilus_core", NAUTILUS_CORE.getItem(), amethystDirk);
        addCriteria(nautilusCore, AND, AMETHYST_DIRK);advancements.add(nautilusCore);

        ConstructableCustomAdvancement amethystDoloire = generate(AMETHYST_DOLOIRE.getItem(), amethystDirk);

        ConstructableCustomAdvancement heavyAmethystDoloire = generate(HEAVY_AMETHYST_DOLOIRE.getItem(), amethystDoloire);

        //// ARTIFACTS /////////////////////////////////

        ConstructableCustomAdvancement flowerWreath = generate("flower_wreath", FLOWER_WREATH.getItem(), root);
        addCriteria(flowerWreath, AND, FLOWER_WREATH); advancements.add(flowerWreath);

        ConstructableCustomAdvancement totemSaving = generate("totem_of_saving", TOTEM_OF_SAVING.getItem(), flowerWreath);
        addCriteria(totemSaving, AND, TOTEM_OF_SAVING);advancements.add(totemSaving);

        ConstructableCustomAdvancement fishermanSnorkel = generate("fisherman_snorkel", FISHERMAN_SNORKEL.getItem(), flowerWreath);
        addCriteria(fishermanSnorkel, AND, FISHERMAN_SNORKEL); advancements.add(fishermanSnorkel);

        ConstructableCustomAdvancement rustyRing = generate("rusty_ring", RUSTY_IRON_RING.getItem(), totemSaving);
        addCriteria(rustyRing, AND, RUSTY_IRON_RING); advancements.add(rustyRing);
        rustyRing.addFakeParent(fishermanSnorkel);
        
        register();
    }

    private static @NotNull ConstructableCustomAdvancement generate(@NotNull ItemStack itemStack, ConstructableCustomAdvancement parent){
        ConstructableCustomAdvancement generate = generate(ItemUtils.getId(itemStack).replace(":", "_"), itemStack, parent);
        if (CustomItem.isCustom(itemStack)){
            addCriteria(generate, AND, CustomItem.get(itemStack));
        } else {
            addCriteria(generate, AND, itemStack.getType());
        }

        return generate;
    }

    private static @NotNull ConstructableCustomAdvancement generate(@NotNull String key, @NotNull ItemStack itemStack, @Nullable ConstructableCustomAdvancement parent){
        ConstructableCustomAdvancement advancement = new ConstructableCustomAdvancement(new NamespacedKey(ScamShieldMain.getInstance(), key));
        CustomAdvancementDisplayBuilder displayAdvancement = new CustomAdvancementDisplayBuilder(itemStack);
        displayAdvancement.title(Component.translatable("advancement.scamshieldmain."+ key +".title"));
        displayAdvancement.description(Component.translatable("advancement.scamshieldmain." + key + ".description"));
        displayAdvancement.announceToChat(false);
        advancement.display(displayAdvancement);
        advancement.setParent(parent);

        advancements.add(advancement);

        return advancement;
    }

    private static void addCriteria(@NotNull ConstructableCustomAdvancement advancement, @NotNull ConstructableCustomAdvancement.RequirementsStrategy strategy, @NotNull Material ...materials){
        for (Material material:materials){
            advancement.addCriterion(material.toString().toLowerCase(), AdvancementCriterion.INVENTORY_CHANGE.create(material));
        }
        advancement.requirementsStrategy(strategy);
    }

    private static void addCriteria(@NotNull ConstructableCustomAdvancement advancement, @NotNull ConstructableCustomAdvancement.RequirementsStrategy strategy, @NotNull CustomItem...customItems){
        for (CustomItem customItem:customItems){
            advancement.addCriterion(customItem.getId(), AdvancementCriterion.INVENTORY_CHANGE.create(customItem.getItem()));
        }
        advancement.requirementsStrategy(strategy);
    }

    private static void register() {
        for (ConstructableCustomAdvancement advancement : advancements) {
/*            LogUtils.pluginLog('"'+((TranslatableComponent) advancement.getDisplay().title()).key()+"\": \"\",");
            LogUtils.pluginLog('"'+((TranslatableComponent) advancement.getDisplay().description()).key()+"\": \"\",");
            LogUtils.log("");*/
            advancement.register();
        }
    }
}
