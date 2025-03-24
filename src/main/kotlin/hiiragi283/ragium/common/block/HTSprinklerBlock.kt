package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.common.block.entity.HTSprinklerBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.world.level.block.state.BlockState

class HTSprinklerBlock(properties: Properties) : HTEntityBlock<HTSprinklerBlockEntity>(RagiumBlockEntityTypes.SPRINKLER, properties) {
    override fun initDefaultState(): BlockState = stateDefinition.any()
}
