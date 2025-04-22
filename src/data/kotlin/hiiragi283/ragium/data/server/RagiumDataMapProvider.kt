package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.extension.blockLookup
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import mekanism.api.chemical.Chemical
import mekanism.api.datamaps.chemical.ChemicalSolidTag
import mekanism.common.registries.MekanismDataMapTypes
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
class RagiumDataMapProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(output, provider) {
    private lateinit var blockLookup: HolderGetter<Block>

    override fun gather(provider: HolderLookup.Provider) {
        blockLookup = provider.blockLookup()

        mekanism()
    }

    private fun mekanism() {
        val builder: Builder<ChemicalSolidTag, Chemical> = builder(MekanismDataMapTypes.INSTANCE.chemicalSolidTag())

        builder.add(
            RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY.cleanSlurry,
            ChemicalSolidTag(HTTagPrefixes.ORE.createItemTag(RagiumMaterials.RAGINITE)),
            false,
        )
    }
}
