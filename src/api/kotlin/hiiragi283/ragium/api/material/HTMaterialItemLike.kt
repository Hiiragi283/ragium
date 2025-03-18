package hiiragi283.ragium.api.material

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

interface HTMaterialItemLike : ItemLike {
    val prefix: HTTagPrefix
    val material: HTMaterialKey
    val id: ResourceLocation
}
