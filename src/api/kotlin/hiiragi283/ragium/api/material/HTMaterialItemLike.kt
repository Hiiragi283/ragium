package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

/**
 * [HTTagPrefix]と[HTMaterial]を保持する[ItemLike]
 */
interface HTMaterialItemLike :
    ItemLike,
    HTMaterial {
    val prefix: HTTagPrefix
    val id: ResourceLocation
}
