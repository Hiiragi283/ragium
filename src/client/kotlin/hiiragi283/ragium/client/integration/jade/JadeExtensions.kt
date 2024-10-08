package hiiragi283.ragium.client.integration.jade

import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import net.minecraft.block.Block
import snownee.jade.api.*

//    IWailaPlugin    //

fun IWailaCommonRegistration.registerBlock(provider: IServerDataProvider<BlockAccessor>, block: Block) {
    registerBlockDataProvider(provider, block::class.java)
}

fun IWailaClientRegistration.registerBlock(provider: IBlockComponentProvider, block: Block) {
    registerBlockComponent(provider, block::class.java)
}

//    BlockAccessor    //

val BlockAccessor.machineEntity: HTMachineEntity?
    get() = (blockEntity as? HTMetaMachineBlockEntity)?.machineEntity
