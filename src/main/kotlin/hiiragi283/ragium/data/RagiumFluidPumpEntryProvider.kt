package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.machine.HTFluidPumpEntry
import hiiragi283.ragium.common.RagiumContents
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeKeys
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class RagiumFluidPumpEntryProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricCodecDataProvider<HTFluidPumpEntry>(
        output,
        registryLookup,
        HTFluidPumpEntry.REGISTRY_KEY,
        HTFluidPumpEntry.CODEC,
    ) {
    override fun getName(): String = "${RagiumAPI.MOD_NAME}/Fluid Pump Entry"

    override fun configure(provider: BiConsumer<Identifier, HTFluidPumpEntry>, lookup: RegistryWrapper.WrapperLookup) {
        register(provider, BiomeKeys.WARM_OCEAN, RagiumContents.Fluids.SALT_WATER)
        register(provider, BiomeKeys.LUKEWARM_OCEAN, RagiumContents.Fluids.SALT_WATER)
        register(provider, BiomeKeys.DEEP_LUKEWARM_OCEAN, RagiumContents.Fluids.SALT_WATER)
        register(provider, BiomeKeys.OCEAN, RagiumContents.Fluids.SALT_WATER)
        register(provider, BiomeKeys.DEEP_OCEAN, RagiumContents.Fluids.SALT_WATER)
        register(provider, BiomeKeys.COLD_OCEAN, RagiumContents.Fluids.SALT_WATER)
        register(provider, BiomeKeys.DEEP_COLD_OCEAN, RagiumContents.Fluids.SALT_WATER)
        register(provider, BiomeKeys.FROZEN_OCEAN, RagiumContents.Fluids.SALT_WATER)
        register(provider, BiomeKeys.DEEP_FROZEN_OCEAN, RagiumContents.Fluids.SALT_WATER)

        register(provider, BiomeKeys.NETHER_WASTES, RagiumContents.Fluids.LAVA)
        register(provider, BiomeKeys.WARPED_FOREST, RagiumContents.Fluids.LAVA)
        register(provider, BiomeKeys.CRIMSON_FOREST, RagiumContents.Fluids.LAVA)
        register(provider, BiomeKeys.SOUL_SAND_VALLEY, RagiumContents.Fluids.OIL)
        register(provider, BiomeKeys.BASALT_DELTAS, RagiumContents.Fluids.LAVA)
    }

    private fun register(provider: BiConsumer<Identifier, HTFluidPumpEntry>, biomeKey: RegistryKey<Biome>, fluid: RagiumContents.Fluids) {
        provider.accept(
            biomeKey.value,
            HTFluidPumpEntry(biomeKey, fluid.fluidName),
        )
    }
}
