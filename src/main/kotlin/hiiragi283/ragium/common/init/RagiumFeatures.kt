package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.PlacedFeature

object RagiumFeatures {
    @JvmField
    val ORE_ASPHALT: Data = create("ore_asphalt")

    @JvmField
    val ORE_GYPSUM: Data = create("ore_gypsum")

    @JvmField
    val ORE_SLATE: Data = create("ore_slate")

    @JvmField
    val ORE_RAGINITE: Data = create("ore_raginite")

    @JvmField
    val ORE_NETHER_RAGINITE: Data = create("ore_nether_raginite")

    @JvmField
    val ORE_END_RAGI_CRYSTAL: Data = create("ore_end_ragi_crystal")

    @JvmStatic
    private fun create(name: String): Data = Data(
        RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, RagiumAPI.id(name)),
        RegistryKey.of(RegistryKeys.PLACED_FEATURE, RagiumAPI.id(name)),
    )

    @JvmStatic
    fun init() {
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            ORE_ASPHALT.featureKey,
        )
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            ORE_GYPSUM.featureKey,
        )
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            ORE_SLATE.featureKey,
        )
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            ORE_RAGINITE.featureKey,
        )
        BiomeModifications.addFeature(
            BiomeSelectors.foundInTheNether(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            ORE_NETHER_RAGINITE.featureKey,
        )
        BiomeModifications.addFeature(
            BiomeSelectors.foundInTheEnd(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            ORE_END_RAGI_CRYSTAL.featureKey,
        )
        /*BiomeModifications.addFeature(
            BiomeSelectors.foundInTheEnd(),
            GenerationStep.Feature.VEGETAL_DECORATION,
            PATCH_END_OBLIVION_CLUSTER.featureKey,
        )*/
    }

    //    Data    //

    data class Data(val configuredKey: RegistryKey<ConfiguredFeature<*, *>>, val featureKey: RegistryKey<PlacedFeature>) {
        fun getConfiguredEntry(lookup: RegistryEntryLookup<ConfiguredFeature<*, *>>): RegistryEntry.Reference<ConfiguredFeature<*, *>> =
            lookup.getOrThrow(configuredKey)
    }
}
