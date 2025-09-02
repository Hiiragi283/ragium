package hiiragi283.ragium.api.util.tool

import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.util.material.HTMaterialType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier

interface HTToolVariant : HTVariantKey.Tagged<Item> {
    fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*>

    fun getParentId(path: String): ResourceLocation
}
