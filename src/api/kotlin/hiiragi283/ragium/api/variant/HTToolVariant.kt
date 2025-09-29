package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier

interface HTToolVariant : HTVariantKey.Tagged<Item> {
    fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*>

    fun getParentId(path: String): ResourceLocation
}
