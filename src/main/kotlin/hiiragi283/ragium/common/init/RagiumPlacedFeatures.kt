package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.*

object RagiumPlacedFeatures {
    @JvmField
    val OVERWORLD_RAGINITE: ResourceKey<PlacedFeature> = createKey("overworld_raginite")

    @JvmField
    val NETHER_RAGINITE: ResourceKey<PlacedFeature> = createKey("nether_raginite")

    @JvmField
    val END_RAGINITE: ResourceKey<PlacedFeature> = createKey("end_raginite")

    @JvmStatic
    private fun createKey(path: String): ResourceKey<PlacedFeature> = ResourceKey.create(Registries.PLACED_FEATURE, RagiumAPI.id(path))

    @JvmStatic
    fun boostrap(context: BootstrapContext<PlacedFeature>) {
        val getter: HolderGetter<ConfiguredFeature<*, *>> = context.lookup(Registries.CONFIGURED_FEATURE)

        fun register(
            placedKey: ResourceKey<PlacedFeature>,
            configuredKey: ResourceKey<ConfiguredFeature<*, *>>,
            amount: Int,
            bottom: VerticalAnchor,
            top: VerticalAnchor,
        ) {
            context.register(
                placedKey,
                PlacedFeature(
                    getter.getOrThrow(configuredKey),
                    listOf(
                        CountPlacement.of(amount),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(bottom, top),
                        BiomeFilter.biome(),
                    ),
                ),
            )
        }

        // Overworld
        register(
            OVERWORLD_RAGINITE,
            RagiumConfiguredFeatures.OVERWORLD_RAGINITE,
            32,
            VerticalAnchor.aboveBottom(5),
            VerticalAnchor.belowTop(5),
        )
        // Nether
        register(
            NETHER_RAGINITE,
            RagiumConfiguredFeatures.NETHER_RAGINITE,
            32,
            VerticalAnchor.aboveBottom(5),
            VerticalAnchor.belowTop(5),
        )
        // End
        register(
            END_RAGINITE,
            RagiumConfiguredFeatures.END_RAGINITE,
            32,
            VerticalAnchor.aboveBottom(5),
            VerticalAnchor.belowTop(5),
        )
    }
}
