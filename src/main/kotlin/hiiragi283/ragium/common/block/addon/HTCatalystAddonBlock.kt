package hiiragi283.ragium.common.block.addon

import hiiragi283.ragium.api.block.HTEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HTCatalystAddonBlock(properties: Properties) : HTEntityBlock(properties) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTCatalystAddonBlockEntity(pos, state)
}
