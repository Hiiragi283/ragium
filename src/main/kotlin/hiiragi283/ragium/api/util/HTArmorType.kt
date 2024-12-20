package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.extension.itemSettings
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

enum class HTArmorType(val armorTag: TagKey<Item>, val itemType: ArmorItem.Type) {
    HELMET(ItemTags.HEAD_ARMOR, ArmorItem.Type.HELMET),
    CHESTPLATE(ItemTags.CHEST_ARMOR, ArmorItem.Type.CHESTPLATE),
    LEGGINGS(ItemTags.LEG_ARMOR, ArmorItem.Type.LEGGINGS),
    BOOTS(ItemTags.FOOT_ARMOR, ArmorItem.Type.BOOTS),
    ;

    fun createItem(material: RegistryEntry<ArmorMaterial>, multiplier: Int, settings: Settings = itemSettings()): ArmorItem =
        ArmorItem(material, itemType, settings.maxDamage(itemType.getMaxDamage(multiplier)))
}
