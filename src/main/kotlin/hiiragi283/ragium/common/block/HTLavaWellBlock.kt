package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.common.block.entity.HTLavaWellBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.world.level.block.state.BlockState

class HTLavaWellBlock(properties: Properties) : HTEntityBlock<HTLavaWellBlockEntity>(RagiumBlockEntityTypes.LAVA_WELL, properties) {
    override fun initDefaultState(): BlockState = stateDefinition.any()
}
