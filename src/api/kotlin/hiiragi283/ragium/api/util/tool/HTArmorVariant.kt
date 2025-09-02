package hiiragi283.ragium.api.util.tool

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import net.minecraft.core.Holder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

enum class HTArmorVariant(
    private val armorType: ArmorItem.Type,
    private val enUsPattern: String,
    private val jaJpPattern: String,
    override val tagKey: TagKey<Item>,
) : HTVariantKey.Tagged<Item> {
    HELMET(ArmorItem.Type.HELMET, "%s Helmet", "%sのヘルメット", ItemTags.HEAD_ARMOR),
    CHESTPLATE(ArmorItem.Type.CHESTPLATE, "%s Chestplate", "%sのチェストプレート", ItemTags.CHEST_ARMOR),
    LEGGINGS(ArmorItem.Type.LEGGINGS, "%s Leggings", "%sのレギンス", ItemTags.LEG_ARMOR),
    BOOTS(ArmorItem.Type.BOOTS, "%s Boots", "%sのブーツ", ItemTags.FOOT_ARMOR),
    ;

    companion object {
        @JvmField
        val ARMOR_TABLE: HTTable<HTArmorVariant, HTVanillaMaterialType, Item> = buildTable {
            // Iron
            put(HELMET, HTVanillaMaterialType.IRON, Items.IRON_HELMET)
            put(CHESTPLATE, HTVanillaMaterialType.IRON, Items.IRON_CHESTPLATE)
            put(LEGGINGS, HTVanillaMaterialType.IRON, Items.IRON_LEGGINGS)
            put(BOOTS, HTVanillaMaterialType.IRON, Items.IRON_BOOTS)
            // Gold
            put(HELMET, HTVanillaMaterialType.GOLD, Items.GOLDEN_HELMET)
            put(CHESTPLATE, HTVanillaMaterialType.GOLD, Items.GOLDEN_CHESTPLATE)
            put(LEGGINGS, HTVanillaMaterialType.GOLD, Items.GOLDEN_LEGGINGS)
            put(BOOTS, HTVanillaMaterialType.GOLD, Items.GOLDEN_BOOTS)
            // Diamond
            put(HELMET, HTVanillaMaterialType.DIAMOND, Items.DIAMOND_HELMET)
            put(CHESTPLATE, HTVanillaMaterialType.DIAMOND, Items.DIAMOND_CHESTPLATE)
            put(LEGGINGS, HTVanillaMaterialType.DIAMOND, Items.DIAMOND_LEGGINGS)
            put(BOOTS, HTVanillaMaterialType.DIAMOND, Items.DIAMOND_BOOTS)
            // Netherite
            put(HELMET, HTVanillaMaterialType.NETHERITE, Items.NETHERITE_HELMET)
            put(CHESTPLATE, HTVanillaMaterialType.NETHERITE, Items.NETHERITE_CHESTPLATE)
            put(LEGGINGS, HTVanillaMaterialType.NETHERITE, Items.NETHERITE_LEGGINGS)
            put(BOOTS, HTVanillaMaterialType.NETHERITE, Items.NETHERITE_BOOTS)
        }
    }

    fun registerItem(
        register: HTDeferredItemRegister,
        material: HTMaterialType,
        armorMaterial1: Holder<ArmorMaterial>,
        multiplier: Int,
    ): HTDeferredItem<ArmorItem> = register.registerItem(
        "${material.serializedName}_$serializedName",
        { prop: Item.Properties -> ArmorItem(armorMaterial1, armorType, prop) },
        Item.Properties().durability(armorType.getDurability(multiplier)),
    )

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
