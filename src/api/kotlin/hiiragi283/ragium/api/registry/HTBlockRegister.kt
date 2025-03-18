package hiiragi283.ragium.api.registry

import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

class HTBlockRegister(namespace: String) : DeferredRegister.Blocks(namespace) {
    override fun getEntries(): List<DeferredBlock<Block>> =
        super.getEntries().map { holder: DeferredHolder<Block, out Block> -> DeferredBlock.createBlock<Block>(holder.id) }
}
