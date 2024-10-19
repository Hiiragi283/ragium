package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.PlacedFeature

object RagiumFeatures {
    @JvmField
    val ORE_BAUXITE: Data = create("ore_bauxite")

    @JvmField
    val ORE_RAGINITE: Data = create("ore_raginite")

    @JvmField
    val ORE_NETHER_RAGINITE: Data = create("ore_nether_raginite")

    @JvmField
    val ORE_END_RAGI_CRYSTAL: Data = create("ore_end_ragi_crystal")

    @JvmField
    val PATCH_END_OBLIVION_CLUSTER: Data = create("patch_end_oblivion_cluster")

    @JvmStatic
    private fun create(name: String): Data = Data(
        RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, RagiumAPI.id(name)),
        RegistryKey.of(RegistryKeys.PLACED_FEATURE, RagiumAPI.id(name)),
    )

    @JvmStatic
    fun init() {
        /*BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            DISK_SALT.featureKey,
        )*/
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_BADLANDS),
            GenerationStep.Feature.UNDERGROUND_ORES,
            ORE_BAUXITE.featureKey,
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
        BiomeModifications.addFeature(
            BiomeSelectors.foundInTheEnd(),
            GenerationStep.Feature.VEGETAL_DECORATION,
            PATCH_END_OBLIVION_CLUSTER.featureKey,
        )
    }

    //    Data    //

    data class Data(val configuredKey: RegistryKey<ConfiguredFeature<*, *>>, val featureKey: RegistryKey<PlacedFeature>) {
        fun getConfiguredEntry(lookup: RegistryEntryLookup<ConfiguredFeature<*, *>>): RegistryEntry.Reference<ConfiguredFeature<*, *>> =
            lookup.getOrThrow(configuredKey)
    }
}
