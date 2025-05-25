package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.HTTreeTap
import hiiragi283.ragium.api.extension.blockLookup
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
class RagiumDataMapProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(output, provider) {
    private lateinit var blockLookup: HolderGetter<Block>

    override fun gather(provider: HolderLookup.Provider) {
        blockLookup = provider.blockLookup()

        fuels()
        treeTaps()
        // mekanism()
    }

    private fun fuels() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)

        builder.add(RagiumItems.RAGI_COKE, FurnaceFuel(200 * 8 * 4), false)
    }

    private fun treeTaps() {
        val builder: Builder<HTTreeTap, Fluid> = builder(RagiumDataMaps.TREE_TAP)

        builder.add(
            RagiumFluidContents.MUSHROOM_STEW.stillHolder,
            HTTreeTap(Blocks.MUSHROOM_STEM),
            false,
        )
        builder.add(
            RagiumFluidContents.HONEY.stillHolder,
            HTTreeTap(Blocks.BEE_NEST),
            false,
        )
        builder.add(
            RagiumFluidContents.CRIMSON_SAP.stillHolder,
            HTTreeTap(BlockTags.CRIMSON_STEMS),
            false,
        )
        builder.add(
            RagiumFluidContents.WARPED_SAP.stillHolder,
            HTTreeTap(BlockTags.WARPED_STEMS),
            false,
        )
    }

    /*private fun mekanism() {
        val builder: Builder<ChemicalSolidTag, Chemical> = builder(MekanismDataMapTypes.INSTANCE.chemicalSolidTag())

        builder.add(
            RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY.cleanSlurry,
            ChemicalSolidTag(RagiumItemTags.ORES_RAGINITE),
            false,
        )
    }*/
}
