package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTManualStepBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty

class HTManualMixerBlock(properties: Properties) : HTManualStepBlock(properties) {
    override fun getStepProperty(): IntegerProperty = BlockStateProperties.AGE_7

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        TODO("Not yet implemented")
    }
}
