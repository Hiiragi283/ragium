package hiiragi283.ragium.data.tag

import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.BlockItemTagKey
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumBlocks
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper

class RagiumBlockTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : HTTagsProvider.DataGen<Block>(fileHelper, output, Registries.BLOCK, lookupProvider, RagiumAPI.MOD_ID) {
    override fun createEmptyTags(registries: HolderLookup.Provider, consumer: (TagKey<Block>) -> Unit) {
        RagiumTags.BlockItems.allTags.forEach { consumer(it.block) }
    }

    override fun appendTags(registries: HolderLookup.Provider) {
        // Mineable
        builder(BlockTags.MINEABLE_WITH_HOE)
            .add(RagiumBlocks.MEAT_BLOCK)
            .add(RagiumBlocks.COOKED_MEAT_BLOCK)

        builder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(RagiumBlocks.HEATING_COIL)
            .add(RagiumBlocks.BOILER)
            .addTag(RagiumTags.BlockItems.MACHINES)
            .addTag(RagiumTags.BlockItems.STORAGES)
        // Other
        builder(RagiumTags.BlockItems.MACHINES)
            .addTag(RagiumTags.BlockItems.MACHINES_MECHANICAL)
            .addTag(RagiumTags.BlockItems.MACHINES_HEAT)
            .addTag(RagiumTags.BlockItems.MACHINES_CHEMICAL)
            .addTag(RagiumTags.BlockItems.MACHINES_BIO)
            .addTag(RagiumTags.BlockItems.MACHINES_COLD)
            .addTag(RagiumTags.BlockItems.MACHINES_ELECTRONICS)
            .addTag(RagiumTags.BlockItems.MACHINES_ARCANE)

        builder(RagiumTags.BlockItems.MACHINES_MECHANICAL)
            .add(RagiumBlocks.ALLOY_SMELTER)
            .add(RagiumBlocks.ASSEMBLER)
            .add(RagiumBlocks.AUTO_CHISEL)
            .add(RagiumBlocks.COMPRESSOR)
            .add(RagiumBlocks.CRUSHER)
            .add(RagiumBlocks.CUTTING_MACHINE)
            .add(RagiumBlocks.ELECTRIC_FURNACE)
        builder(RagiumTags.BlockItems.MACHINES_HEAT)
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.PYROLYZER)
            .add(RagiumBlocks.REFINERY)
        builder(RagiumTags.BlockItems.MACHINES_CHEMICAL)
            .add(RagiumBlocks.BREWERY)
            .add(RagiumBlocks.MIXER)
            .add(RagiumBlocks.WASHER)
        builder(RagiumTags.BlockItems.MACHINES_BIO)
            .add(RagiumBlocks.PLANTER)
        builder(RagiumTags.BlockItems.MACHINES_COLD)
            .add(RagiumBlocks.FREEZER)
        builder(RagiumTags.BlockItems.MACHINES_ELECTRONICS)
            .add(RagiumBlocks.PRINTER)
        builder(RagiumTags.BlockItems.MACHINES_ARCANE)
            .add(RagiumBlocks.FLUID_DUPLICATOR)
            .add(RagiumBlocks.MASS_FABRICATOR)
            .add(RagiumBlocks.ENCHANTER)

        builder(RagiumTags.BlockItems.STORAGES)
            .addTag(RagiumTags.BlockItems.STORAGES_CREATIVE)
            .add(RagiumBlocks.BATTERY)
            .add(RagiumBlocks.CRATE)
            .add(RagiumBlocks.TANK)
            .add(RagiumBlocks.UNIVERSAL_CHEST)
            .add(RagiumBlocks.VOID_TANK)
        builder(RagiumTags.BlockItems.STORAGES_CREATIVE)
            .add(RagiumBlocks.CREATIVE_BATTERY)
            .add(RagiumBlocks.CREATIVE_CRATE)
            .add(RagiumBlocks.CREATIVE_TANK)
    }

    fun builder(tagKey: BlockItemTagKey): HTTagBuilder<Block> = builder(tagKey.block)

    fun HTTagBuilder<Block>.addTag(tagKey: BlockItemTagKey, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<Block> = this.addTag(tagKey.block, type)
}
