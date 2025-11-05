package hiiragi283.ragium.common.variant

import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenKnifeItem
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object HTKitchenKnifeToolVariant : HTToolVariant {
    override fun registerItem(register: HTDeferredItemRegister, material: HTEquipmentMaterial): HTDeferredItem<*> =
        register.registerItemWith("${material.asMaterialName()}_kitchen_knife", material, ::KitchenKnifeItem)

    override val tagKeys: Iterable<TagKey<Item>> = listOf(TagMod.KITCHEN_KNIFE)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "%s Kitchen Knife"
        HTLanguageType.JA_JP -> "%sの包丁"
    }.replace("%s", value)

    override fun variantName(): String = "kitchen_knife"
}
