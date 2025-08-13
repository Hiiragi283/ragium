package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.material.HTMaterialType
import net.minecraft.core.Holder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

enum class HTArmorVariant(
    val armorType: ArmorItem.Type,
    private val enUsPattern: String,
    private val jaJpPattern: String,
    override val tagKey: TagKey<Item>,
) : HTVariantKey.Tagged<Item> {
    HELMET(ArmorItem.Type.HELMET, "%s Helmet", "%sのヘルメット", ItemTags.HEAD_ARMOR),
    CHESTPLATE(ArmorItem.Type.CHESTPLATE, "%s Chestplate", "%sのチェストプレート", ItemTags.CHEST_ARMOR),
    LEGGINGS(ArmorItem.Type.LEGGINGS, "%s Leggings", "%sのレギンス", ItemTags.LEG_ARMOR),
    BOOTS(ArmorItem.Type.BOOTS, "%s Boots", "%sのブーツ", ItemTags.FOOT_ARMOR),
    ;

    fun registerItem(
        register: DeferredRegister.Items,
        material: HTMaterialType,
        armorMaterial1: Holder<ArmorMaterial>,
        multiplier: Int,
    ): DeferredItem<ArmorItem> = register.registerItem(
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
