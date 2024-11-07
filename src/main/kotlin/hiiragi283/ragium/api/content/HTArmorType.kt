package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.extension.itemSettings
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Item
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

enum class HTArmorType(val armorTag: TagKey<Item>, val itemType: ArmorItem.Type) : HTTranslationFormatter {
    HELMET(ItemTags.HEAD_ARMOR, ArmorItem.Type.HELMET) {
        override fun getPattern(type: HTLangType): String = when (type) {
            HTLangType.EN_US -> "%s Helmet"
            HTLangType.JA_JP -> "%sのヘルメット"
        }
    },
    CHESTPLATE(ItemTags.CHEST_ARMOR, ArmorItem.Type.CHESTPLATE) {
        override fun getPattern(type: HTLangType): String = when (type) {
            HTLangType.EN_US -> "%s Chestplate"
            HTLangType.JA_JP -> "%sのチェストプレート"
        }
    },
    LEGGINGS(ItemTags.LEG_ARMOR, ArmorItem.Type.LEGGINGS) {
        override fun getPattern(type: HTLangType): String = when (type) {
            HTLangType.EN_US -> "%s Leggings"
            HTLangType.JA_JP -> "%sのレギンス"
        }
    },
    BOOTS(ItemTags.FOOT_ARMOR, ArmorItem.Type.BOOTS) {
        override fun getPattern(type: HTLangType): String = when (type) {
            HTLangType.EN_US -> "%s Boots"
            HTLangType.JA_JP -> "%sのブーツ"
        }
    },
    ;

    fun createItem(material: RegistryEntry<ArmorMaterial>, multiplier: Int): ArmorItem =
        ArmorItem(material, itemType, itemSettings().maxDamage(itemType.getMaxDamage(multiplier)))
}
