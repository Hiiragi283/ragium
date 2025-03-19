package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.common.block.entity.HTExtractorBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

class HTExtractorBlock(properties: Properties) :
    HTHorizontalEntityBlock<HTExtractorBlockEntity>(RagiumBlockEntityTypes.EXTRACTOR, properties) {
    override fun initDefaultState(): BlockState = stateDefinition
        .any()
        .setValue(HORIZONTAL, Direction.NORTH)
}
