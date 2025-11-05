package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.item.tool.HTHammerItem
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Item

object HTHammerToolVariant : HTToolVariant {
    override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial): HTDeferredItem<*> =
        register.registerItemWith("${material.asMaterialName()}_hammer", material, ::HTHammerItem) {
            it.attributes(DiggerItem.createAttributes(material, material.getPickaxeDamage(), material.getPickaxeAttackSpeed()))
        }

    override val tagKeys: Iterable<TagKey<Item>> = listOf(RagiumModTags.Items.TOOLS_HAMMER)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "$value Hammer"
        HTLanguageType.JA_JP -> "${value}のハンマー"
    }

    override fun variantName(): String = "hammer"
}
