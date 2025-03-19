package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.common.block.entity.HTEnergyNetworkInterfaceBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.world.level.block.state.BlockState

class HTEnergyNetworkInterfaceBlock(properties: Properties) :
    HTEntityBlock<HTEnergyNetworkInterfaceBlockEntity>(RagiumBlockEntityTypes.ENI, properties) {
    override fun initDefaultState(): BlockState = stateDefinition.any()
}
