package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumWorldGens
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import java.util.concurrent.CompletableFuture

class RagiumWorldGenProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) :
    DatapackBuiltinEntriesProvider(
        output,
        provider,
        RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, RagiumWorldGens::boostrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, RagiumWorldGens::boostrapPlacedFeatures),
        setOf(RagiumAPI.MOD_ID)
    ) {
}
