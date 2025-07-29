package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.energy.HTEnergyFilter
import hiiragi283.ragium.api.storage.energy.HTFilteredEnergyStorage
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTDynamoBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state) {
    override fun wrapNetworkToExternal(network: IEnergyStorage): IEnergyStorage =
        HTFilteredEnergyStorage(network, HTEnergyFilter.EXTRACT_ONLY)
}
