package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.data.HTWorldGenData
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.*

object RagiumPlacedProvider : RegistrySetBuilder.RegistryBootstrap<PlacedFeature> {
    override fun run(context: BootstrapContext<PlacedFeature>) {
        // Ore
        register(
            context,
            RagiumWorldGenData.ORE_RAGINITE,
            CountPlacement.of(8),
            HeightRangePlacement.uniform(
                VerticalAnchor.aboveBottom(32),
                VerticalAnchor.belowTop(64),
            ),
        )
        // Geode
        register(
            context,
            RagiumWorldGenData.GEODE_CRUDE_OIL,
            RarityFilter.onAverageOnceEvery(24),
            PlacementUtils.RANGE_10_10,
        )
    }

    private fun register(context: BootstrapContext<PlacedFeature>, data: HTWorldGenData, vararg modifiers: PlacementModifier) {
        data.placedHolder = context.register(
            data.placedKey,
            PlacedFeature(
                data.configuredHolder,
                listOf(
                    BiomeFilter.biome(),
                    InSquarePlacement.spread(),
                    *modifiers,
                ),
            ),
        )
    }
}
