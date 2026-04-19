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

class RagiumBlockTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : HTTagsProvider.DataGen<Block>(fileHelper, output, Registries.BLOCK, lookupProvider, RagiumAPI.MOD_ID) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Block>) {
        // Mineable
        factory
            .apply(BlockTags.MINEABLE_WITH_HOE)
            .add(RagiumBlocks.MEAT_BLOCK)
            .add(RagiumBlocks.COOKED_MEAT_BLOCK)

        factory
            .apply(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(RagiumTags.Blocks.DEVICES.base)
            .addTag(RagiumTags.Blocks.GENERATORS.base)
            .addTag(RagiumTags.Blocks.MACHINES.base)
            .addTag(RagiumTags.Blocks.STORAGES)
        // Other
        RagiumTags.Blocks.DEVICES.apply(factory)

        RagiumTags.Blocks.GENERATORS.apply(factory)
        factory
            .apply(RagiumTags.Blocks.GENERATORS.basic)
            .add(RagiumBlocks.BOILER)

        RagiumTags.Blocks.MACHINES.apply(factory)
        factory
            .apply(RagiumTags.Blocks.MACHINES.basic)
            .add(RagiumBlocks.ALLOY_SMELTER)
            .add(RagiumBlocks.ASSEMBLER)
            .add(RagiumBlocks.AUTO_CHISEL)
            .add(RagiumBlocks.CRUSHER)
            .add(RagiumBlocks.CUTTING_MACHINE)
            .add(RagiumBlocks.ELECTRIC_FURNACE)
            .add(RagiumBlocks.PLANTER)
        factory
            .apply(RagiumTags.Blocks.MACHINES.advanced)
            .add(RagiumBlocks.FREEZER)
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.PYROLYZER)
            .add(RagiumBlocks.REFINERY)
            .add(RagiumBlocks.WASHER)
        factory
            .apply(RagiumTags.Blocks.MACHINES.elite)
            .add(RagiumBlocks.BREWERY)
            .add(RagiumBlocks.CHEMICAL_WASHER)
            .add(RagiumBlocks.FLUID_MIXER)
            .add(RagiumBlocks.MIXER)
        factory
            .apply(RagiumTags.Blocks.MACHINES.ultimate)
            .add(RagiumBlocks.FLUID_DUPLICATOR)

        factory
            .apply(RagiumTags.Blocks.STORAGES)
            .addTag(RagiumTags.Blocks.STORAGES_CREATIVE)
            .add(RagiumBlocks.BATTERY)
            .add(RagiumBlocks.CRATE)
            .add(RagiumBlocks.TANK)
            .add(RagiumBlocks.UNIVERSAL_CHEST)
            .add(RagiumBlocks.VOID_TANK)
        factory
            .apply(RagiumTags.Blocks.STORAGES_CREATIVE)
            .add(RagiumBlocks.CREATIVE_BATTERY)
            .add(RagiumBlocks.CREATIVE_CRATE)
            .add(RagiumBlocks.CREATIVE_TANK)
    }
}
