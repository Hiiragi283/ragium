package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.data.HTWorldGenData
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter

object RagiumPlacedProvider : RegistrySetBuilder.RegistryBootstrap<PlacedFeature> {
    override fun run(context: BootstrapContext<PlacedFeature>) {
        // Ore
        register(
            context,
            RagiumWorldGenData.ORE_RAGINITE,
            CountPlacement.of(4),
            HeightRangePlacement.uniform(
                VerticalAnchor.bottom(),
                VerticalAnchor.absolute(15),
            ),
        )
        register(
            context,
            RagiumWorldGenData.ORE_RAGINITE_LOWER,
            CountPlacement.of(8),
            HeightRangePlacement.triangle(
                VerticalAnchor.aboveBottom(-32),
                VerticalAnchor.aboveBottom(32),
            ),
        )

        register(
            context,
            RagiumWorldGenData.ORE_RESONANT_DEBRIS,
            HeightRangePlacement.uniform(
                VerticalAnchor.bottom(),
                VerticalAnchor.absolute(15),
            ),
        )
        // Geode
        register(
            context,
            RagiumWorldGenData.QUARTZ_GEODE,
            RarityFilter.onAverageOnceEvery(24),
            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.belowTop(6)),
        )
    }

    private fun register(context: BootstrapContext<PlacedFeature>, data: HTWorldGenData, vararg modifiers: PlacementModifier) {
        context.register(
            data.placedKey,
            PlacedFeature(
                context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(data.configuredKey),
                listOf(
                    BiomeFilter.biome(),
                    InSquarePlacement.spread(),
                    *modifiers,
                ),
            ),
        )
    }
}
