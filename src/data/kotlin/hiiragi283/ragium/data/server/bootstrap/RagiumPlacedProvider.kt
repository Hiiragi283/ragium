package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.util.HTWorldGenData
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier

object RagiumPlacedProvider : RegistrySetBuilder.RegistryBootstrap<PlacedFeature> {
    override fun run(context: BootstrapContext<PlacedFeature>) {
        // Ore
        register(
            context,
            RagiumWorldGenData.ORE_RAGINITE,
            CountPlacement.of(8),
            HeightRangePlacement.uniform(
                VerticalAnchor.bottom(),
                VerticalAnchor.absolute(15),
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
