package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.recipe.HTFluidPumpEntry
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
    override fun getName(): String = "${Ragium.MOD_NAME}/Fluid Pump Entry"

    override fun configure(provider: BiConsumer<Identifier, HTFluidPumpEntry>, lookup: RegistryWrapper.WrapperLookup) {
        register(provider, BiomeKeys.WARM_OCEAN, RagiumItems.Fluids.SALT_WATER)
        register(provider, BiomeKeys.LUKEWARM_OCEAN, RagiumItems.Fluids.SALT_WATER)
        register(provider, BiomeKeys.DEEP_LUKEWARM_OCEAN, RagiumItems.Fluids.SALT_WATER)
        register(provider, BiomeKeys.OCEAN, RagiumItems.Fluids.SALT_WATER)
        register(provider, BiomeKeys.DEEP_OCEAN, RagiumItems.Fluids.SALT_WATER)
        register(provider, BiomeKeys.COLD_OCEAN, RagiumItems.Fluids.SALT_WATER)
        register(provider, BiomeKeys.DEEP_COLD_OCEAN, RagiumItems.Fluids.SALT_WATER)
        register(provider, BiomeKeys.FROZEN_OCEAN, RagiumItems.Fluids.SALT_WATER)
        register(provider, BiomeKeys.DEEP_FROZEN_OCEAN, RagiumItems.Fluids.SALT_WATER)

        register(provider, BiomeKeys.NETHER_WASTES, RagiumItems.Fluids.LAVA)
        register(provider, BiomeKeys.WARPED_FOREST, RagiumItems.Fluids.LAVA)
        register(provider, BiomeKeys.CRIMSON_FOREST, RagiumItems.Fluids.LAVA)
        register(provider, BiomeKeys.SOUL_SAND_VALLEY, RagiumItems.Fluids.OIL)
        register(provider, BiomeKeys.BASALT_DELTAS, RagiumItems.Fluids.LAVA)
    }

    private fun register(provider: BiConsumer<Identifier, HTFluidPumpEntry>, biomeKey: RegistryKey<Biome>, fluid: RagiumItems.Fluids) {
        provider.accept(
            biomeKey.value,
            HTFluidPumpEntry(biomeKey, fluid.fluidName),
        )
    }
}
