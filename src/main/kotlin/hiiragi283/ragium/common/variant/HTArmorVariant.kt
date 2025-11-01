package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.material.VanillaMaterialKeys
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
    val tagKey: TagKey<Item>,
) : HTVariantKey {
    HELMET(ArmorItem.Type.HELMET, "%s Helmet", "%sのヘルメット", ItemTags.HEAD_ARMOR),
    CHESTPLATE(ArmorItem.Type.CHESTPLATE, "%s Chestplate", "%sのチェストプレート", ItemTags.CHEST_ARMOR),
    LEGGINGS(ArmorItem.Type.LEGGINGS, "%s Leggings", "%sのレギンス", ItemTags.LEG_ARMOR),
    BOOTS(ArmorItem.Type.BOOTS, "%s Boots", "%sのブーツ", ItemTags.FOOT_ARMOR),
    ;

    companion object {
        val ARMOR_TABLE: ImmutableTable<HTArmorVariant, HTMaterialKey, Item> = buildTable {
            // Iron
            this[HELMET, VanillaMaterialKeys.IRON] = Items.IRON_HELMET
            this[CHESTPLATE, VanillaMaterialKeys.IRON] = Items.IRON_CHESTPLATE
            this[LEGGINGS, VanillaMaterialKeys.IRON] = Items.IRON_LEGGINGS
            this[BOOTS, VanillaMaterialKeys.IRON] = Items.IRON_BOOTS
            // Gold
            this[HELMET, VanillaMaterialKeys.GOLD] = Items.GOLDEN_HELMET
            this[CHESTPLATE, VanillaMaterialKeys.GOLD] = Items.GOLDEN_CHESTPLATE
            this[LEGGINGS, VanillaMaterialKeys.GOLD] = Items.GOLDEN_LEGGINGS
            this[BOOTS, VanillaMaterialKeys.GOLD] = Items.GOLDEN_BOOTS
            // Diamond
            this[HELMET, VanillaMaterialKeys.DIAMOND] = Items.DIAMOND_HELMET
            this[CHESTPLATE, VanillaMaterialKeys.DIAMOND] = Items.DIAMOND_CHESTPLATE
            this[LEGGINGS, VanillaMaterialKeys.DIAMOND] = Items.DIAMOND_LEGGINGS
            this[BOOTS, VanillaMaterialKeys.DIAMOND] = Items.DIAMOND_BOOTS
            // Netherite
            this[HELMET, VanillaMaterialKeys.NETHERITE] = Items.NETHERITE_HELMET
            this[CHESTPLATE, VanillaMaterialKeys.NETHERITE] = Items.NETHERITE_CHESTPLATE
            this[LEGGINGS, VanillaMaterialKeys.NETHERITE] = Items.NETHERITE_LEGGINGS
            this[BOOTS, VanillaMaterialKeys.NETHERITE] = Items.NETHERITE_BOOTS
        }
    }

    fun registerItem(
        register: HTDeferredItemRegister,
        material: HTMaterialLike,
        material1: Holder<ArmorMaterial>,
        multiplier: Int,
    ): HTDeferredItem<ArmorItem> = register.registerItem(
        "${material.asMaterialName()}_${variantName()}",
        { ArmorItem(material1, armorType, it.durability(armorType.getDurability(multiplier))) },
    )

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
