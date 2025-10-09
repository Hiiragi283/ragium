package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.material.HTVanillaMaterialType
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
        val ARMOR_TABLE: ImmutableTable<HTArmorVariant, HTVanillaMaterialType, Item> = buildTable {
            // Iron
            this[HELMET, HTVanillaMaterialType.IRON] = Items.IRON_HELMET
            this[CHESTPLATE, HTVanillaMaterialType.IRON] = Items.IRON_CHESTPLATE
            this[LEGGINGS, HTVanillaMaterialType.IRON] = Items.IRON_LEGGINGS
            this[BOOTS, HTVanillaMaterialType.IRON] = Items.IRON_BOOTS
            // Gold
            this[HELMET, HTVanillaMaterialType.GOLD] = Items.GOLDEN_HELMET
            this[CHESTPLATE, HTVanillaMaterialType.GOLD] = Items.GOLDEN_CHESTPLATE
            this[LEGGINGS, HTVanillaMaterialType.GOLD] = Items.GOLDEN_LEGGINGS
            this[BOOTS, HTVanillaMaterialType.GOLD] = Items.GOLDEN_BOOTS
            // Diamond
            this[HELMET, HTVanillaMaterialType.DIAMOND] = Items.DIAMOND_HELMET
            this[CHESTPLATE, HTVanillaMaterialType.DIAMOND] = Items.DIAMOND_CHESTPLATE
            this[LEGGINGS, HTVanillaMaterialType.DIAMOND] = Items.DIAMOND_LEGGINGS
            this[BOOTS, HTVanillaMaterialType.DIAMOND] = Items.DIAMOND_BOOTS
            // Netherite
            this[HELMET, HTVanillaMaterialType.NETHERITE] = Items.NETHERITE_HELMET
            this[CHESTPLATE, HTVanillaMaterialType.NETHERITE] = Items.NETHERITE_CHESTPLATE
            this[LEGGINGS, HTVanillaMaterialType.NETHERITE] = Items.NETHERITE_LEGGINGS
            this[BOOTS, HTVanillaMaterialType.NETHERITE] = Items.NETHERITE_BOOTS
        }
    }

    fun registerItem(
        register: HTDeferredItemRegister,
        material: HTMaterialType,
        armorMaterial1: Holder<ArmorMaterial>,
        multiplier: Int,
    ): HTDeferredItem<ArmorItem> = register.registerItem(
        "${material.materialName()}_${variantName()}",
        { prop: Item.Properties -> ArmorItem(armorMaterial1, armorType, prop) },
        Item.Properties().durability(armorType.getDurability(multiplier)),
    )

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
