package hiiragi283.ragium.common.block

import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.world.level.ItemLike

class HTExpBerriesBushBlock(properties: Properties) : HTCropBlock(properties) {
    override fun getBaseSeedId(): ItemLike = RagiumBlocks.EXP_BERRIES
}
