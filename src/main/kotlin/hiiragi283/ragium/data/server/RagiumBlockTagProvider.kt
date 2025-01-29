package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.extension.addAll
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import vectorwing.farmersdelight.common.tag.ModTags
import java.util.concurrent.CompletableFuture

class RagiumBlockTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : TagsProvider<Block>(output, Registries.BLOCK, provider, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        // Mineable
        val pickaxe: TagAppender<Block> = tag(BlockTags.MINEABLE_WITH_PICKAXE)
        buildList {
            addAll(RagiumBlocks.Ores.entries)
            addAll(RagiumBlocks.StorageBlocks.entries)

            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Hulls.entries)
            addAll(RagiumBlocks.Coils.entries)
            addAll(RagiumBlocks.Burners.entries)

            addAll(RagiumBlocks.Drums.entries)

            addAll(RagiumAPI.machineRegistry.blockMap.values)
        }.forEach(pickaxe::add)

        buildList {
            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            add(RagiumBlocks.SHAFT)
            add(RagiumBlocks.CHEMICAL_GLASS)

            add(RagiumBlocks.PLASTIC_BLOCK)

            add(RagiumBlocks.MANUAL_GRINDER)

            addAll(RagiumBlocks.ADDONS)
        }.forEach(pickaxe::add)

        tag(BlockTags.MINEABLE_WITH_HOE)
            .add(RagiumBlocks.SPONGE_CAKE)
            .add(RagiumBlocks.SWEET_BERRIES_CAKE)

        // Farmer's Delight
        tag(ModTags.HEAT_SOURCES).addAll(RagiumBlocks.Burners.entries)

        RagiumBlocks.Ores.entries.forEach { ore: RagiumBlocks.Ores ->
            tag(Tags.Blocks.ORES).add(ore)
            when (ore.oreVariant) {
                HTOreVariant.OVERWORLD -> Tags.Blocks.ORES_IN_GROUND_STONE
                HTOreVariant.DEEP -> Tags.Blocks.ORES_IN_GROUND_DEEPSLATE
                HTOreVariant.NETHER -> Tags.Blocks.ORES_IN_GROUND_NETHERRACK
                HTOreVariant.END -> null
            }?.let(::tag)?.add(ore)
        }
    }
}
