package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.common.block.entity.HTCrusherBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlock(properties: Properties) :
    HTHorizontalEntityBlock<HTCrusherBlockEntity>(RagiumBlockEntityTypes.CRUSHER, properties) {
    override fun initDefaultState(): BlockState = stateDefinition
        .any()
        .setValue(HORIZONTAL, Direction.NORTH)
}
