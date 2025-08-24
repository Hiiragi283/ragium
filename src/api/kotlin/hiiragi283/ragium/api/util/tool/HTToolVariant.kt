package hiiragi283.ragium.api.util.tool

import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.material.HTMaterialType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

interface HTToolVariant : HTVariantKey.Tagged<Item> {
    fun registerItem(register: DeferredRegister.Items, material: HTMaterialType, tier: Tier): DeferredItem<*>

    fun getParentId(path: String): ResourceLocation
}
