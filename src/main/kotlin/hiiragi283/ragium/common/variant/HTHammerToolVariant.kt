package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslationProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.item.tool.HTHammerItem
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier

object HTHammerToolVariant : HTToolVariant, HTTranslationProvider {
    override fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*> =
        register.registerItemWith("${material.materialName()}_hammer", tier, ::HTHammerItem) {
            it.attributes(DiggerItem.createAttributes(tier, 1f, -2.8f))
        }

    override val tagKeys: Iterable<TagKey<Item>> = listOf(RagiumModTags.Items.TOOLS_HAMMER)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "%s Hammer"
        HTLanguageType.JA_JP -> "%sのハンマー"
    }.replace("%s", value)

    override fun variantName(): String = "hammer"
}
