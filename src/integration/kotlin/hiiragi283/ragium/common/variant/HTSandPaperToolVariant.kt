package hiiragi283.ragium.common.variant

import com.simibubi.create.AllTags
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object HTSandPaperToolVariant : HTToolVariant {
    override val tagKeys: Iterable<TagKey<Item>> get() = listOf(AllTags.AllItemTags.SANDPAPER.tag)

    @Deprecated("Not implemented", level = DeprecationLevel.ERROR)
    override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial): HTSimpleDeferredItem =
        throw UnsupportedOperationException()

    override fun variantName(): String = "sand_paper"

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "$value Sand Paper"
        HTLanguageType.JA_JP -> "${value}の紙やすり"
    }
}
