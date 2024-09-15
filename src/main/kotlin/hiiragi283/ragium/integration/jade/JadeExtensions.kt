package hiiragi283.ragium.integration.jade

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

fun BlockAccessor.ifPresent(key: String, action: BlockAccessor.() -> Unit) {
    if (serverData.contains(key)) action(this)
}
