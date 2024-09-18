package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.item.HTFluidCubeItem
import net.minecraft.registry.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeKeys

object HTFluidPumpRegistry {
    val registry: Map<RegistryKey<Biome>, HTFluidCubeItem>
        get() = registry1
    private val registry1: MutableMap<RegistryKey<Biome>, HTFluidCubeItem> = mutableMapOf()

    @JvmStatic
    fun get(biomeKey: RegistryKey<Biome>): HTFluidCubeItem? = registry[biomeKey]

    @JvmStatic
    fun forEach(action: (RegistryKey<Biome>, HTFluidCubeItem) -> Unit) {
        registry.forEach(action)
    }

    @JvmStatic
    fun register(biomeKey: RegistryKey<Biome>, fluidName: String) {
        // check(biomeKey !in registry) { "Biome; $biomeKey is already registered!" }
        register(biomeKey, HTFluidCubeItem.getOrThrow(fluidName))
    }

    @JvmStatic
    fun register(biomeKey: RegistryKey<Biome>, item: HTFluidCubeItem) {
        registry1[biomeKey] = item
    }

    init {
        register(BiomeKeys.WARM_OCEAN, RagiumItems.SALT_WATER_FLUID_CUBE)
        register(BiomeKeys.LUKEWARM_OCEAN, RagiumItems.SALT_WATER_FLUID_CUBE)
        register(BiomeKeys.DEEP_LUKEWARM_OCEAN, RagiumItems.SALT_WATER_FLUID_CUBE)
        register(BiomeKeys.OCEAN, RagiumItems.SALT_WATER_FLUID_CUBE)
        register(BiomeKeys.DEEP_OCEAN, RagiumItems.SALT_WATER_FLUID_CUBE)
        register(BiomeKeys.COLD_OCEAN, RagiumItems.SALT_WATER_FLUID_CUBE)
        register(BiomeKeys.DEEP_OCEAN, RagiumItems.SALT_WATER_FLUID_CUBE)
        register(BiomeKeys.FROZEN_OCEAN, RagiumItems.SALT_WATER_FLUID_CUBE)
        register(BiomeKeys.DEEP_FROZEN_OCEAN, RagiumItems.SALT_WATER_FLUID_CUBE)

        register(BiomeKeys.NETHER_WASTES, RagiumItems.LAVA_FLUID_CUBE)
        register(BiomeKeys.WARPED_FOREST, RagiumItems.LAVA_FLUID_CUBE)
        register(BiomeKeys.CRIMSON_FOREST, RagiumItems.LAVA_FLUID_CUBE)
        register(BiomeKeys.SOUL_SAND_VALLEY, RagiumItems.OIL_FLUID_CUBE)
        register(BiomeKeys.BASALT_DELTAS, RagiumItems.LAVA_FLUID_CUBE)
    }
}
