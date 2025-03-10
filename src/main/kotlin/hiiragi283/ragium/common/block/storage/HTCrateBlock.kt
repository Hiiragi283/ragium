package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.util.HTCrateVariant
import hiiragi283.ragium.common.tile.storage.HTCrateBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTCrateBlock(val variant: HTCrateVariant, properties: Properties) : HTEntityBlock.Horizontal(properties) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): HTCrateBlockEntity = HTCrateBlockEntity(pos, state, variant)
}
