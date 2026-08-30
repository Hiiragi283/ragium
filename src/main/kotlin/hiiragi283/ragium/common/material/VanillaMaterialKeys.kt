package hiiragi283.ragium.common.material

import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.ragium.api.RagiumAPI

data object VanillaMaterialKeys {
    @JvmStatic
    private fun create(path: String): HTMaterialKey = HTMaterialKey(RagiumAPI.id(path))

    //    Fuels    //

    @JvmStatic
    val COAL: HTMaterialKey = create("coal")

    @JvmStatic
    val CHARCOAL: HTMaterialKey = create("charcoal")

    //    Minerals    //

    @JvmStatic
    val REDSTONE: HTMaterialKey = create("redstone")

    @JvmStatic
    val GLOWSTONE: HTMaterialKey = create("glowstone")

    //    Gems    //

    @JvmStatic
    val LAPIS: HTMaterialKey = create("lapis")

    @JvmStatic
    val QUARTZ: HTMaterialKey = create("quartz")

    @JvmStatic
    val AMETHYST: HTMaterialKey = create("amethyst")

    @JvmStatic
    val DIAMOND: HTMaterialKey = create("diamond")

    @JvmStatic
    val EMERALD: HTMaterialKey = create("emerald")

    @JvmStatic
    val ECHO: HTMaterialKey = create("echo")

    @JvmStatic
    val PRISMARINE: HTMaterialKey = create("prismarine")

    @JvmStatic
    val ENDER: HTMaterialKey = create("ender")

    //    Metals    //

    @JvmStatic
    val COPPER: HTMaterialKey = create("copper")

    @JvmStatic
    val IRON: HTMaterialKey = create("iron")

    @JvmStatic
    val GOLD: HTMaterialKey = create("gold")

    //    Alloys    //

    @JvmStatic
    val NETHERITE: HTMaterialKey = create("netherite")

    //    Others    //

    @JvmStatic
    val WOOD: HTMaterialKey = create("wood")

    @JvmStatic
    val PAPER: HTMaterialKey = create("paper")

    @JvmStatic
    val GLASS: HTMaterialKey = create("glass")

    @JvmStatic
    val STONE: HTMaterialKey = create("stone")

    @JvmStatic
    val OBSIDIAN: HTMaterialKey = create("obsidian")

    @JvmStatic
    val BLAZE: HTMaterialKey = create("blaze")

    @JvmStatic
    val BREEZE: HTMaterialKey = create("breeze")

    @JvmStatic
    val BRICK: HTMaterialKey = create("brick")

    @JvmStatic
    val NETHER_BRICK: HTMaterialKey = create("nether_brick")

    //    Item    //

    /*val ARMOR_TABLE: ImmutableTable<HTArmorVariant, HTMaterialKey, Item> = buildTable {
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
    }*/
}
