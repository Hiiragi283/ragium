package hiiragi283.ragium.api.registry

import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Ragiumで使用する[DeferredRegister.Blocks]
 */
class HTBlockRegister(namespace: String) : DeferredRegister.Blocks(namespace) {
    override fun getEntries(): List<DeferredBlock<*>> =
        super.getEntries().map { holder: DeferredHolder<Block, *> -> DeferredBlock.createBlock<Block>(holder.id) }
}
