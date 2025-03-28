package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.data.HTWorldGenData
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.*

object RagiumPlacedProvider : RegistrySetBuilder.RegistryBootstrap<PlacedFeature> {
    override fun run(context: BootstrapContext<PlacedFeature>) {
        registerOres(context)
    }

    private fun register(context: BootstrapContext<PlacedFeature>, data: HTWorldGenData, placed: PlacedFeature) {
        data.placedHolder = context.register(data.placedKey, placed)
    }

    //    Ore    //

    private fun registerOres(context: BootstrapContext<PlacedFeature>) {
        fun register(
            data: HTWorldGenData,
            count: Int,
            min: Int,
            max: Int,
        ) {
            register(
                context,
                data,
                PlacedFeature(
                    data.configuredHolder,
                    listOf(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                            VerticalAnchor.absolute(min),
                            VerticalAnchor.absolute(max),
                        ),
                        BiomeFilter.biome(),
                    ),
                ),
            )
        }

        register(RagiumWorldGenData.RAGINITE_ORE, 8, -32, 192)
    }
}
