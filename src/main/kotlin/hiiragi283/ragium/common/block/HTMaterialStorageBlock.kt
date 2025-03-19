package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.level.block.Block

class HTMaterialStorageBlock(val key: HTMaterialKey, properties: Properties) : Block(properties) {
    override fun getName(): MutableComponent = HTTagPrefix.STORAGE_BLOCK.createText(key)
}
