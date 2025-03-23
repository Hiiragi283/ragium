package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.common.block.entity.HTMilkDrainBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.world.level.block.state.BlockState

class HTMilkDrainBlock(properties: Properties) : HTEntityBlock<HTMilkDrainBlockEntity>(RagiumBlockEntityTypes.MILK_DRAIN, properties) {
    override fun initDefaultState(): BlockState = stateDefinition.any()
}
