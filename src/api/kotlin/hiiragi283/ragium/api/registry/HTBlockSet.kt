package hiiragi283.ragium.api.registry

import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.registries.DeferredBlock

interface HTBlockSet : HTItemSet {
    val blockHolders: List<DeferredBlock<*>>

    fun getBlocks(): List<Block> = blockHolders.map(DeferredBlock<*>::get)

    fun addBlockStates(provider: BlockStateProvider)
}
