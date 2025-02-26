package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.extension.blockLookup
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
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
    lateinit var builder: HTTagBuilder<Block>

    private fun HTTagBuilder<Block>.add(tagKey: TagKey<Block>, block: Block) {
        add(tagKey, BuiltInRegistries.BLOCK.wrapAsHolder(block))
    }

    @Suppress("DEPRECATION")
    override fun addTags(provider: HolderLookup.Provider) {
        builder = HTTagBuilder(provider.blockLookup())

        // Mineable
        HTMachineType.getBlocks().forEach { builder.add(BlockTags.MINEABLE_WITH_PICKAXE, it) }

        buildList {
            addAll(RagiumBlocks.ORES.values)
            addAll(RagiumBlocks.STORAGE_BLOCKS.values)

            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            add(RagiumBlocks.SHAFT)
            addAll(RagiumBlocks.GLASSES)

            add(RagiumBlocks.MANUAL_GRINDER)
            add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)
            add(RagiumBlocks.DISENCHANTING_TABLE)

            add(RagiumBlocks.COPPER_DRUM)
            addAll(RagiumBlocks.ADDONS)
            addAll(RagiumBlocks.BURNERS)
        }.forEach { builder.add(BlockTags.MINEABLE_WITH_PICKAXE, it) }

        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.SLAG_BLOCK)

        builder.add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SPONGE_CAKE)
        builder.add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SWEET_BERRIES_CAKE)
        // Vanilla
        RagiumBlocks.RAGI_BRICK_FAMILY.appendTags(BlockTags.MINEABLE_WITH_PICKAXE, builder::add)
        RagiumBlocks.PLASTIC_FAMILY.appendTags(BlockTags.MINEABLE_WITH_PICKAXE, builder::add)
        RagiumBlocks.BLUE_NETHER_BRICK_FAMILY.appendTags(BlockTags.MINEABLE_WITH_PICKAXE, builder::add)
        // Common
        RagiumBlocks.ORES.forEach { (variant: HTOreVariant, key: HTMaterialKey, ore: DeferredBlock<out Block>) ->
            when (variant) {
                HTOreVariant.OVERWORLD -> Tags.Blocks.ORES_IN_GROUND_STONE
                HTOreVariant.DEEPSLATE -> Tags.Blocks.ORES_IN_GROUND_DEEPSLATE
                HTOreVariant.NETHER -> Tags.Blocks.ORES_IN_GROUND_NETHERRACK
                HTOreVariant.END -> blockTagKey(commonId("ores_in_ground/end_stone"))
            }?.let { builder.add(it, ore) }

            val oreTagKey: TagKey<Block> = HTTagPrefix.ORE.createBlockTag(key) ?: return@forEach
            builder.addTag(Tags.Blocks.ORES, oreTagKey)
            builder.add(oreTagKey, ore)
        }

        RagiumBlocks.STORAGE_BLOCKS.forEach { (key: HTMaterialKey, storage: DeferredBlock<Block>) ->
            val storageTag: TagKey<Block> = HTTagPrefix.STORAGE_BLOCK.createBlockTag(key) ?: return@forEach
            builder.addTag(Tags.Blocks.STORAGE_BLOCKS, storageTag)
            builder.add(storageTag, storage)

            builder.addTag(BlockTags.BEACON_BASE_BLOCKS, storageTag)
        }
        // Ragium
        builder.add(RagiumBlockTags.COOLING_SOURCES, Blocks.WATER)
        builder.addTag(RagiumBlockTags.COOLING_SOURCES, BlockTags.ICE)
        builder.addTag(RagiumBlockTags.COOLING_SOURCES, BlockTags.SNOW)

        builder.add(RagiumBlockTags.HEATING_SOURCES, Blocks.CAMPFIRE)
        builder.add(RagiumBlockTags.HEATING_SOURCES, Blocks.FIRE)
        builder.add(RagiumBlockTags.HEATING_SOURCES, Blocks.LAVA)
        builder.add(RagiumBlockTags.HEATING_SOURCES, Blocks.MAGMA_BLOCK)

        builder.addTag(RagiumBlockTags.MINEABLE_WITH_DRILL, BlockTags.MINEABLE_WITH_PICKAXE)
        builder.addTag(RagiumBlockTags.MINEABLE_WITH_DRILL, BlockTags.MINEABLE_WITH_SHOVEL)

        // Farmer's Delight
        builder.add(ModTags.HEAT_SOURCES, RagiumBlocks.MAGMA_BURNER)
        builder.add(ModTags.HEAT_SOURCES, RagiumBlocks.SOUL_BURNER)
        builder.add(ModTags.HEAT_SOURCES, RagiumBlocks.FIERY_BURNER)

        builder.build { tagKey: TagKey<Block>, entry: TagEntry ->
            tag(tagKey).add(entry)
        }
    }
}
