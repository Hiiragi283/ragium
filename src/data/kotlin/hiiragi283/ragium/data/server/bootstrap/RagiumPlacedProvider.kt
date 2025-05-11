package hiiragi283.ragium.data.server.bootstrap

import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.level.levelgen.placement.PlacedFeature

object RagiumPlacedProvider : RegistrySetBuilder.RegistryBootstrap<PlacedFeature> {
    override fun run(context: BootstrapContext<PlacedFeature>) {
        // Ore
        /*register(
            context,
            RagiumWorldGenData.ORE_RAGINITE,
            CountPlacement.of(8),
            HeightRangePlacement.uniform(
                VerticalAnchor.aboveBottom(32),
                VerticalAnchor.belowTop(64),
            ),
        )*/
    }

    /*private fun register(context: BootstrapContext<PlacedFeature>, data: HTWorldGenData, vararg modifiers: PlacementModifier) {
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
    }*/
}
