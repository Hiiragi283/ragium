package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.extension.itemSettings
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Item
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

enum class HTArmorType(val armorTag: TagKey<Item>, val itemType: ArmorItem.Type) : HTTranslationFormatter {
    HELMET(ItemTags.HEAD_ARMOR, ArmorItem.Type.HELMET) {
        override val enPattern: String = "%s Helmet"
        override val jaPattern: String = "%sのヘルメット"
    },
    CHESTPLATE(ItemTags.CHEST_ARMOR, ArmorItem.Type.CHESTPLATE) {
        override val enPattern: String = "%s Chestplate"
        override val jaPattern: String = "%sのチェストプレート"
    },
    LEGGINGS(ItemTags.LEG_ARMOR, ArmorItem.Type.LEGGINGS) {
        override val enPattern: String = "%s Leggings"
        override val jaPattern: String = "%sのレギンス"
    },
    BOOTS(ItemTags.FOOT_ARMOR, ArmorItem.Type.BOOTS) {
        override val enPattern: String = "%s Boots"
        override val jaPattern: String = "%sのブーツ"
    },
    ;

    fun createItem(material: RegistryEntry<ArmorMaterial>, multiplier: Int): ArmorItem =
        ArmorItem(material, itemType, itemSettings().maxDamage(itemType.getMaxDamage(multiplier)))
}
