package hiiragi283.ragium.data.tag

import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture
import net.minecraft.tags.TagKey

class RagiumBlockTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : HTTagsProvider.DataGen<Block>(fileHelper, output, Registries.BLOCK, lookupProvider, RagiumAPI.MOD_ID) {
    override fun createEmptyTags(registries: HolderLookup.Provider, consumer: (TagKey<Block>) -> Unit) {
        RagiumTags.Blocks.DEVICES.prepare(consumer)
        RagiumTags.Blocks.GENERATORS.prepare(consumer)
        RagiumTags.Blocks.MACHINES.prepare(consumer)
    }

    override fun appendTags(registries: HolderLookup.Provider) {
        // Mineable
        builder(BlockTags.MINEABLE_WITH_HOE)
            .add(RagiumBlocks.MEAT_BLOCK)
            .add(RagiumBlocks.COOKED_MEAT_BLOCK)

        builder(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(RagiumTags.Blocks.DEVICES.base)
            .addTag(RagiumTags.Blocks.GENERATORS.base)
            .addTag(RagiumTags.Blocks.MACHINES.base)
            .addTag(RagiumTags.Blocks.STORAGES)
        // Other
        RagiumTags.Blocks.DEVICES.apply(::builder)
        builder(RagiumTags.Blocks.DEVICES.ultimate)
            .add(RagiumBlocks.ENCHANTER)

        RagiumTags.Blocks.GENERATORS.apply(::builder)
        builder(RagiumTags.Blocks.GENERATORS.basic)
            .add(RagiumBlocks.BOILER)

        RagiumTags.Blocks.MACHINES.apply(::builder)
        builder(RagiumTags.Blocks.MACHINES.basic)
            .add(RagiumBlocks.ALLOY_SMELTER)
            .add(RagiumBlocks.ASSEMBLER)
            .add(RagiumBlocks.AUTO_CHISEL)
            .add(RagiumBlocks.COMPRESSOR)
            .add(RagiumBlocks.CRUSHER)
            .add(RagiumBlocks.CUTTING_MACHINE)
            .add(RagiumBlocks.ELECTRIC_FURNACE)
            .add(RagiumBlocks.PLANTER)
        builder(RagiumTags.Blocks.MACHINES.advanced)
            .add(RagiumBlocks.FREEZER)
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.PYROLYZER)
            .add(RagiumBlocks.REFINERY)
            .add(RagiumBlocks.WASHER)
        builder(RagiumTags.Blocks.MACHINES.elite)
            .add(RagiumBlocks.BREWERY)
            .add(RagiumBlocks.MIXER)
        builder(RagiumTags.Blocks.MACHINES.ultimate)
            .add(RagiumBlocks.FLUID_DUPLICATOR)
            .add(RagiumBlocks.MASS_FABRICATOR)

        builder(RagiumTags.Blocks.STORAGES)
            .addTag(RagiumTags.Blocks.STORAGES_CREATIVE)
            .add(RagiumBlocks.BATTERY)
            .add(RagiumBlocks.CRATE)
            .add(RagiumBlocks.TANK)
            .add(RagiumBlocks.UNIVERSAL_CHEST)
            .add(RagiumBlocks.VOID_TANK)
        builder(RagiumTags.Blocks.STORAGES_CREATIVE)
            .add(RagiumBlocks.CREATIVE_BATTERY)
            .add(RagiumBlocks.CREATIVE_CRATE)
            .add(RagiumBlocks.CREATIVE_TANK)
    }
}
