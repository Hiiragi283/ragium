package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.common.block.entity.HTWaterWellBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.world.level.block.state.BlockState

class HTWaterWellBlock(properties: Properties) : HTEntityBlock<HTWaterWellBlockEntity>(RagiumBlockEntityTypes.WATER_WELL, properties) {
    override fun initDefaultState(): BlockState = stateDefinition.any()
}
