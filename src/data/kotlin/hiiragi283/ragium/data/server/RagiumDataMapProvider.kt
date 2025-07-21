package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.extension.blockLookup
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
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
        // mekanism()
    }

    private fun fuels() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)

        builder.add(RagiumItems.RAGI_COKE, FurnaceFuel(200 * 8 * 4), false)
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
