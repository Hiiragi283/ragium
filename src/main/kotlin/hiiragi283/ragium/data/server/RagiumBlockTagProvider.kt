package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.extension.addBlock
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
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
        RagiumAPI
            .getInstance()
            .getMachineRegistry()
            .blocks
            .forEach(pickaxe::add)

        buildList {
            addAll(RagiumBlocks.ORES.values)
            addAll(RagiumBlocks.STORAGE_BLOCKS.values)

            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            add(RagiumBlocks.SHAFT)
            addAll(RagiumBlocks.GLASSES)

            add(RagiumBlocks.PLASTIC_BLOCK)

            add(RagiumBlocks.MANUAL_GRINDER)
            add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)

            add(RagiumBlocks.COPPER_DRUM)
            addAll(RagiumBlocks.ADDONS)
            addAll(RagiumBlocks.BURNERS_NEW)
        }.forEach(pickaxe::add)

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(RagiumBlocks.SOUL_MAGMA_BLOCK)

        tag(BlockTags.MINEABLE_WITH_HOE)
            .add(RagiumBlocks.SPONGE_CAKE)
            .add(RagiumBlocks.SWEET_BERRIES_CAKE)

        // Vanilla

        // Ragium
        tag(RagiumBlockTags.COOLING_SOURCES)
            .addBlock(Blocks.WATER)
            .addTag(BlockTags.ICE)
            .addTag(BlockTags.SNOW)

        tag(RagiumBlockTags.HEATING_SOURCES)
            .addBlock(Blocks.CAMPFIRE)
            .addBlock(Blocks.FIRE)
            .addBlock(Blocks.LAVA)
            .addBlock(Blocks.MAGMA_BLOCK)

        // Farmer's Delight
        tag(ModTags.HEAT_SOURCES)
            .add(RagiumBlocks.MAGMA_BURNER)
            .add(RagiumBlocks.SOUL_BURNER)
            .add(RagiumBlocks.FIERY_BURNER)

        RagiumBlocks.ORES.forEach { (variant: HTOreVariant, _, ore: DeferredBlock<out Block>) ->
            tag(Tags.Blocks.ORES).add(ore)
            when (variant) {
                HTOreVariant.OVERWORLD -> Tags.Blocks.ORES_IN_GROUND_STONE
                HTOreVariant.DEEPSLATE -> Tags.Blocks.ORES_IN_GROUND_DEEPSLATE
                HTOreVariant.NETHER -> Tags.Blocks.ORES_IN_GROUND_NETHERRACK
                HTOreVariant.END -> null
            }?.let(::tag)?.add(ore)
        }
    }
}
