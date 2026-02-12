package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block

class RagiumBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider.DataGen<Block>(RagiumAPI.MOD_ID, Registries.BLOCK, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Block>) {
        val hoe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_HOE)
        sequence {
            yield(RagiumBlocks.MEAT_BLOCK)
            yield(RagiumBlocks.COOKED_MEAT_BLOCK)
        }.forEach(hoe::add)

        val pickaxe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            yield(RagiumBlocks.ALLOY_SMELTER)
            yield(RagiumBlocks.CRUSHER)
            yield(RagiumBlocks.CUTTING_MACHINE)
            yield(RagiumBlocks.ELECTRIC_FURNACE)
            yield(RagiumBlocks.FORMING_PRESS)

            yield(RagiumBlocks.MELTER)
            yield(RagiumBlocks.PYROLYZER)

            yield(RagiumBlocks.SOLIDIFIER)

            yield(RagiumBlocks.MIXER)
            yield(RagiumBlocks.WASHER)

            yield(RagiumBlocks.PLANTER)

            yield(RagiumBlocks.ENCHANTER)

            yield(RagiumBlocks.BATTERY)
            yield(RagiumBlocks.CRATE)
            yield(RagiumBlocks.TANK)
            yield(RagiumBlocks.RESONANT_INTERFACE)
            yield(RagiumBlocks.UNIVERSAL_CHEST)

            yield(RagiumBlocks.IMITATION_SPAWNER)

            yield(RagiumBlocks.CREATIVE_BATTERY)
            yield(RagiumBlocks.CREATIVE_CRATE)
            yield(RagiumBlocks.CREATIVE_TANK)
        }.forEach(pickaxe::add)
    }
}
