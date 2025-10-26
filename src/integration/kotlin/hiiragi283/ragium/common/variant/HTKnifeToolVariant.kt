package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslationProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTToolVariant
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier
import vectorwing.farmersdelight.common.item.KnifeItem
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.CommonTags
import vectorwing.farmersdelight.common.tag.ModTags

object HTKnifeToolVariant : HTToolVariant, HTTranslationProvider {
    override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
        register.register("${material.materialName()}_knife") { _: ResourceLocation ->
            KnifeItem(
                tier,
                ModItems.knifeItem(tier),
            )
        }

    override val tagKeys: Iterable<TagKey<Item>> = listOf(CommonTags.TOOLS_KNIFE, ModTags.KNIVES)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "%s Knife"
        HTLanguageType.JA_JP -> "%sのナイフ"
    }.replace("%s", value)

    override fun variantName(): String = "knife"
}
