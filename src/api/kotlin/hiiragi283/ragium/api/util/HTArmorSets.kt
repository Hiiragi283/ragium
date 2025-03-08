package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.material.HTMaterialKey
import net.minecraft.core.Holder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

class HTArmorSets(register: DeferredRegister.Items, material: Holder<ArmorMaterial>, val key: HTMaterialKey) {
    companion object {
        @JvmField
        val VALID_TYPES: List<ArmorItem.Type> = listOf(
            ArmorItem.Type.HELMET,
            ArmorItem.Type.CHESTPLATE,
            ArmorItem.Type.LEGGINGS,
            ArmorItem.Type.BOOTS,
        )
    }

    private val armorMap: Map<ArmorItem.Type, DeferredItem<ArmorItem>> =
        VALID_TYPES.associateWith { type: ArmorItem.Type ->
            register.registerItem(
                "${key.name}_${type.serializedName}",
                { properties: Item.Properties -> ArmorItem(material, type, properties) },
                itemProperty().durability(type.getDurability(20)),
            )
        }

    val armors: Collection<DeferredItem<ArmorItem>> get() = armorMap.values

    operator fun get(type: ArmorItem.Type): DeferredItem<ArmorItem> = armorMap[type] ?: error("Unknown armor type: $type")

    operator fun contains(item: ItemLike): Boolean = armors.any { holder: DeferredItem<ArmorItem> -> holder.isOf(item.asItem()) }

    //    Data Gen    //

    fun appendTags(action: (TagKey<Item>, Holder<Item>) -> Unit) {
        action(ItemTags.HEAD_ARMOR_ENCHANTABLE, get(ArmorItem.Type.HELMET))
        action(ItemTags.CHEST_ARMOR_ENCHANTABLE, get(ArmorItem.Type.CHESTPLATE))
        action(ItemTags.LEG_ARMOR_ENCHANTABLE, get(ArmorItem.Type.LEGGINGS))
        action(ItemTags.FOOT_ARMOR_ENCHANTABLE, get(ArmorItem.Type.BOOTS))
    }

    fun addTranslationsEn(name: String, action: (Supplier<out Item>, String) -> Unit) {
        action(get(ArmorItem.Type.HELMET), "$name Helmet")
        action(get(ArmorItem.Type.CHESTPLATE), "$name Chestplate")
        action(get(ArmorItem.Type.LEGGINGS), "$name Leggings")
        action(get(ArmorItem.Type.BOOTS), "$name Boots")
    }

    fun addTranslationsJp(name: String, action: (Supplier<out Item>, String) -> Unit) {
        action(get(ArmorItem.Type.HELMET), "${name}のヘルメット")
        action(get(ArmorItem.Type.CHESTPLATE), "${name}のチェストプレート")
        action(get(ArmorItem.Type.LEGGINGS), "${name}のレギンス")
        action(get(ArmorItem.Type.BOOTS), "${name}のブーツ")
    }
}
