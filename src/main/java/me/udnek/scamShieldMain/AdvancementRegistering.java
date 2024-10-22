package me.udnek.scamShieldMain;


import me.udnek.itemscoreu.customadvancement.AdvancementCriterion;
import me.udnek.itemscoreu.customadvancement.ConstructableCustomAdvancement;
import me.udnek.itemscoreu.customadvancement.CustomAdvancementDisplayBuilder;
import me.udnek.itemscoreu.customadvancement.CustomAdvancementUtils;
import me.udnek.rpgu.RpgU;
import me.udnek.rpgu.item.Items;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class AdvancementRegistering {
    private static final List<ConstructableCustomAdvancement> advancements = new ArrayList<>();

    public static void run() {
        ConstructableCustomAdvancement root = CustomAdvancementUtils.itemAdvancement(new NamespacedKey(RpgU.getInstance(), "root"), Items.FABRIC.getItem());
        root.removeCriterion("0");
        root.addCriterion("tick", AdvancementCriterion.TICK);
        CustomAdvancementDisplayBuilder displayRoot = new CustomAdvancementDisplayBuilder(Items.FABRIC.getItem());
        displayRoot.title(Component.translatable("advancement.rpgu.root.title"));//Приветствуем на сервере ScumShield.
        displayRoot.description(Component.translatable("advancement.rpgu.root.description"));//Мы подготовили для вас цепочку квестов чтобы вы разобрались со всеми изменениями, идите по квестам  и вы увидите все нововидения.
        root.display(displayRoot);
        root.getDisplay().background("textures/block/cobblestone.png");
        advancements.add(root);


        ConstructableCustomAdvancement woodenTools = CustomAdvancementUtils.itemAdvancement(new NamespacedKey(RpgU.getInstance(), "first_tools"), new ItemStack(Material.WOODEN_PICKAXE));
        woodenTools.removeCriterion("0");
        CustomAdvancementDisplayBuilder displayWoodenTools = new CustomAdvancementDisplayBuilder(new ItemStack(Material.WOODEN_PICKAXE));
        displayWoodenTools.title(Component.translatable("advancement.rpgu.first_tools.title"));//Первые инструметы
        displayWoodenTools.description(Component.translatable("advancement.rpgu.first_tools.description"));//Отправляйтесь в свое путешествие, приобретя первые простые инструменты.
        displayWoodenTools.announceToChat(false);
        woodenTools.display(displayWoodenTools);
        woodenTools.requirementsStrategy(ConstructableCustomAdvancement.RequirementsStrategy.OR);
        woodenTools.addCriterion("wooden_pickaxe", AdvancementCriterion.INVENTORY_CHANGE.create(new ItemStack(Material.WOODEN_PICKAXE)));
        woodenTools.addCriterion("wooden_axe", AdvancementCriterion.INVENTORY_CHANGE.create(new ItemStack(Material.WOODEN_AXE)));
        woodenTools.addCriterion("wooden_shovel", AdvancementCriterion.INVENTORY_CHANGE.create(new ItemStack(Material.WOODEN_SHOVEL)));
        woodenTools.addCriterion("wooden_hoe", AdvancementCriterion.INVENTORY_CHANGE.create(new ItemStack(Material.WOODEN_HOE)));
        woodenTools.addCriterion("wooden_sword", AdvancementCriterion.INVENTORY_CHANGE.create(new ItemStack(Material.WOODEN_SWORD)));
        woodenTools.parent(root);
        advancements.add(woodenTools);

        ConstructableCustomAdvancement sand = CustomAdvancementUtils.itemAdvancement(new NamespacedKey(RpgU.getInstance(), "to_tough_as_nails_u"), new ItemStack(Material.SAND));
        sand.removeCriterion("0");
        CustomAdvancementDisplayBuilder displaySand = new CustomAdvancementDisplayBuilder(new ItemStack(Material.SAND));
        displaySand.title(Component.translatable("Attempts to overcome thirst."));//Попытки побороть жажду.
        displaySand.description(Component.translatable("Embark on your journey by acquiring the first flimsy tools."));//Отправляйтесь в свое путешествие, приобретя первые простые инструменты.
        sand.display(displaySand);
        sand.addCriterion("sand", AdvancementCriterion.INVENTORY_CHANGE.create(new ItemStack(Material.SAND)));
        sand.parent(woodenTools);
        advancements.add(sand);

        register();
    }
    private static void register() {
        for (ConstructableCustomAdvancement advancement : advancements) {
            advancement.register();
        }
    }
}
