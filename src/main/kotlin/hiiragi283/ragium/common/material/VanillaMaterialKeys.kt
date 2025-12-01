package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.variant.HTArmorVariant
import hiiragi283.ragium.common.variant.VanillaToolVariant
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

object VanillaMaterialKeys {
    // Gem
    @JvmStatic
    val LAPIS: HTMaterialKey = HTMaterialKey.of("lapis")

    @JvmStatic
    val QUARTZ: HTMaterialKey = HTMaterialKey.of("quartz")

    @JvmStatic
    val AMETHYST: HTMaterialKey = HTMaterialKey.of("amethyst")

    @JvmStatic
    val DIAMOND: HTMaterialKey = HTMaterialKey.of("diamond")

    @JvmStatic
    val EMERALD: HTMaterialKey = HTMaterialKey.of("emerald")

    @JvmStatic
    val ECHO: HTMaterialKey = HTMaterialKey.of("echo")

    // Metal
    @JvmStatic
    val COPPER: HTMaterialKey = HTMaterialKey.of("copper")

    @JvmStatic
    val IRON: HTMaterialKey = HTMaterialKey.of("iron")

    @JvmStatic
    val GOLD: HTMaterialKey = HTMaterialKey.of("gold")

    @JvmStatic
    val NETHERITE: HTMaterialKey = HTMaterialKey.of("netherite")

    // Other
    @JvmStatic
    val COAL: HTMaterialKey = HTMaterialKey.of("coal")

    @JvmStatic
    val CHARCOAL: HTMaterialKey = HTMaterialKey.of("charcoal")

    @JvmStatic
    val REDSTONE: HTMaterialKey = HTMaterialKey.of("redstone")

    @JvmStatic
    val GLOWSTONE: HTMaterialKey = HTMaterialKey.of("glowstone")

    @JvmStatic
    val OBSIDIAN: HTMaterialKey = HTMaterialKey.of("obsidian")

    @JvmStatic
    val WOOD: HTMaterialKey = HTMaterialKey.of("wood")

    //    Item    //

    @JvmStatic
    val ARMOR_TABLE: ImmutableTable<HTArmorVariant, HTMaterialKey, Item> = buildTable {
        // Iron
        this[HTArmorVariant.HELMET, IRON] = Items.IRON_HELMET
        this[HTArmorVariant.CHESTPLATE, IRON] = Items.IRON_CHESTPLATE
        this[HTArmorVariant.LEGGINGS, IRON] = Items.IRON_LEGGINGS
        this[HTArmorVariant.BOOTS, IRON] = Items.IRON_BOOTS
        // Gold
        this[HTArmorVariant.HELMET, GOLD] = Items.GOLDEN_HELMET
        this[HTArmorVariant.CHESTPLATE, GOLD] = Items.GOLDEN_CHESTPLATE
        this[HTArmorVariant.LEGGINGS, GOLD] = Items.GOLDEN_LEGGINGS
        this[HTArmorVariant.BOOTS, GOLD] = Items.GOLDEN_BOOTS
        // Diamond
        this[HTArmorVariant.HELMET, DIAMOND] = Items.DIAMOND_HELMET
        this[HTArmorVariant.CHESTPLATE, DIAMOND] = Items.DIAMOND_CHESTPLATE
        this[HTArmorVariant.LEGGINGS, DIAMOND] = Items.DIAMOND_LEGGINGS
        this[HTArmorVariant.BOOTS, DIAMOND] = Items.DIAMOND_BOOTS
        // Netherite
        this[HTArmorVariant.HELMET, NETHERITE] = Items.NETHERITE_HELMET
        this[HTArmorVariant.CHESTPLATE, NETHERITE] = Items.NETHERITE_CHESTPLATE
        this[HTArmorVariant.LEGGINGS, NETHERITE] = Items.NETHERITE_LEGGINGS
        this[HTArmorVariant.BOOTS, NETHERITE] = Items.NETHERITE_BOOTS
    }

    @JvmStatic
    val TOOL_TABLE: ImmutableTable<HTToolVariant, HTMaterialKey, Item> = buildTable {
        // Wooden
        this[VanillaToolVariant.SHOVEL, WOOD] = Items.WOODEN_SHOVEL
        this[VanillaToolVariant.PICKAXE, WOOD] = Items.WOODEN_PICKAXE
        this[VanillaToolVariant.AXE, WOOD] = Items.WOODEN_AXE
        this[VanillaToolVariant.HOE, WOOD] = Items.WOODEN_HOE
        this[VanillaToolVariant.SWORD, WOOD] = Items.WOODEN_SWORD
        // Stone
        // Iron
        this[VanillaToolVariant.SHOVEL, IRON] = Items.IRON_SHOVEL
        this[VanillaToolVariant.PICKAXE, IRON] = Items.IRON_PICKAXE
        this[VanillaToolVariant.AXE, IRON] = Items.IRON_AXE
        this[VanillaToolVariant.HOE, IRON] = Items.IRON_HOE
        this[VanillaToolVariant.SWORD, IRON] = Items.IRON_SWORD

        this[VanillaToolVariant.SHEARS, IRON] = Items.SHEARS
        // Golden
        this[VanillaToolVariant.SHOVEL, GOLD] = Items.GOLDEN_SHOVEL
        this[VanillaToolVariant.PICKAXE, GOLD] = Items.GOLDEN_PICKAXE
        this[VanillaToolVariant.AXE, GOLD] = Items.GOLDEN_AXE
        this[VanillaToolVariant.HOE, GOLD] = Items.GOLDEN_HOE
        this[VanillaToolVariant.SWORD, GOLD] = Items.GOLDEN_SWORD
        // Diamond
        this[VanillaToolVariant.SHOVEL, DIAMOND] = Items.DIAMOND_SHOVEL
        this[VanillaToolVariant.PICKAXE, DIAMOND] = Items.DIAMOND_PICKAXE
        this[VanillaToolVariant.AXE, DIAMOND] = Items.DIAMOND_AXE
        this[VanillaToolVariant.HOE, DIAMOND] = Items.DIAMOND_HOE
        this[VanillaToolVariant.SWORD, DIAMOND] = Items.DIAMOND_SWORD
        // Netherite
        this[VanillaToolVariant.SHOVEL, NETHERITE] = Items.NETHERITE_SHOVEL
        this[VanillaToolVariant.PICKAXE, NETHERITE] = Items.NETHERITE_PICKAXE
        this[VanillaToolVariant.AXE, NETHERITE] = Items.NETHERITE_AXE
        this[VanillaToolVariant.HOE, NETHERITE] = Items.NETHERITE_HOE
        this[VanillaToolVariant.SWORD, NETHERITE] = Items.NETHERITE_SWORD
    }
}
