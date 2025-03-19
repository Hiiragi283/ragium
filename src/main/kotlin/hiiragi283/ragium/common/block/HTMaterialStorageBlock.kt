package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

class HTMaterialStorageBlock(override val key: HTMaterialKey, properties: Properties) :
    Block(properties),
    HTMaterialItemLike {
    override val prefix: HTTagPrefix = HTTagPrefix.STORAGE_BLOCK
    override val id: ResourceLocation get() = asItemHolder().idOrThrow

    override fun getName(): MutableComponent = prefix.createText(key)
}
