package hiiragi283.ragium.api.util

import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

/**
 * 装備の種類を管理するクラス
 */
enum class HTArmorType(val armorTag: TagKey<Item>, val itemType: ArmorItem.Type) {
    HELMET(ItemTags.HEAD_ARMOR, ArmorItem.Type.HELMET),
    CHESTPLATE(ItemTags.CHEST_ARMOR, ArmorItem.Type.CHESTPLATE),
    LEGGINGS(ItemTags.LEG_ARMOR, ArmorItem.Type.LEGGINGS),
    BOOTS(ItemTags.FOOT_ARMOR, ArmorItem.Type.BOOTS),
    ;

    /**
     * 指定した値から[ArmorItem]を返します。
     * @param material 装備の素材
     * @param multiplier 装備の耐久地の倍率
     * @param settings アイテムの設定
     */
    fun createItem(material: RegistryEntry<ArmorMaterial>, multiplier: Int, settings: Settings): ArmorItem =
        ArmorItem(material, itemType, settings.maxDamage(itemType.getMaxDamage(multiplier)))

    /**
     * 定形クラフトでのパターンを返します。
     */
    fun getShapedPattern(): List<String> = when (this) {
        HELMET -> listOf("AAA", "A A")
        CHESTPLATE -> listOf("A A", "AAA", "AAA")
        LEGGINGS -> listOf("AAA", "A A", "A A")
        BOOTS -> listOf("A A", "A A")
    }
}
