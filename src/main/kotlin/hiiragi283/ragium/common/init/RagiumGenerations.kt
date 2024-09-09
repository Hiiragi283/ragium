package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.PlacedFeature

object RagiumGenerations {

    @JvmField
    val ORE_RAGINITE: RegistryKey<PlacedFeature> =
        RegistryKey.of(RegistryKeys.PLACED_FEATURE, Ragium.id("ore_raginite"))

    init {
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            ORE_RAGINITE
        )
    }

}