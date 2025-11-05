package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import vectorwing.farmersdelight.common.item.KnifeItem
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.CommonTags
import vectorwing.farmersdelight.common.tag.ModTags

object HTKnifeToolVariant : HTToolVariant {
    override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial): HTDeferredItem<*> =
        register.register("${material.asMaterialName()}_knife") { _ ->
            KnifeItem(material, ModItems.knifeItem(material))
        }

    override val tagKeys: Iterable<TagKey<Item>> = listOf(CommonTags.TOOLS_KNIFE, ModTags.KNIVES)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "%s Knife"
        HTLanguageType.JA_JP -> "%sのナイフ"
    }.replace("%s", value)

    override fun variantName(): String = "knife"
}
