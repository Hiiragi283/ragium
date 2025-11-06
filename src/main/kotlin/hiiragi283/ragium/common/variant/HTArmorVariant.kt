package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTVariantKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder

enum class HTArmorVariant(
    val armorType: ArmorItem.Type,
    private val enUsPattern: String,
    private val jaJpPattern: String,
    val tagKey: TagKey<Item>,
) : HTVariantKey {
    HELMET(ArmorItem.Type.HELMET, "%s Helmet", "%sのヘルメット", ItemTags.HEAD_ARMOR),
    CHESTPLATE(ArmorItem.Type.CHESTPLATE, "%s Chestplate", "%sのチェストプレート", ItemTags.CHEST_ARMOR),
    LEGGINGS(ArmorItem.Type.LEGGINGS, "%s Leggings", "%sのレギンス", ItemTags.LEG_ARMOR),
    BOOTS(ArmorItem.Type.BOOTS, "%s Boots", "%sのブーツ", ItemTags.FOOT_ARMOR),
    ;

    fun registerItem(
        register: HTDeferredItemRegister,
        material: HTEquipmentMaterial,
        name: String = "${material.asMaterialName()}_${variantName()}",
    ): HTDeferredItem<ArmorItem> = register.registerItem(
        name,
        {
            ArmorItem(
                DeferredHolder.create(Registries.ARMOR_MATERIAL, RagiumAPI.id(material.asMaterialName())),
                armorType,
                it.durability(armorType.getDurability(material.getArmorMultiplier())),
            )
        },
    )

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
