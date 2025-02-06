package hiiragi283.ragium.data.server.worldgen

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight
import net.minecraft.world.level.levelgen.placement.*

object RagiumPlacedFeatures {
    @JvmField
    val OVERWORLD_RAGINITE: ResourceKey<PlacedFeature> = createKey("overworld_raginite")

    @JvmField
    val NETHER_RAGINITE: ResourceKey<PlacedFeature> = createKey("nether_raginite")

    @JvmField
    val END_RAGINITE: ResourceKey<PlacedFeature> = createKey("end_raginite")

    @JvmField
    val LAKE_CRUDE_OIL_SURFACE: ResourceKey<PlacedFeature> = createKey("lake_crude_oil_surface")

    @JvmField
    val LAKE_CRUDE_OIL_UNDERGROUND: ResourceKey<PlacedFeature> = createKey("lake_crude_oil_underground")

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
            PlacementUtils.register(
                context,
                placedKey,
                getter.getOrThrow(configuredKey),
                CountPlacement.of(amount),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(bottom, top),
                BiomeFilter.biome(),
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
        // Crude Oil
        PlacementUtils.register(
            context,
            LAKE_CRUDE_OIL_SURFACE,
            getter.getOrThrow(RagiumConfiguredFeatures.LAKE_CRUDE_OIL),
            RarityFilter.onAverageOnceEvery(9),
            InSquarePlacement.spread(),
            HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.top())),
            EnvironmentScanPlacement.scanningFor(
                Direction.DOWN,
                BlockPredicate.allOf(
                    BlockPredicate.not(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                    BlockPredicate.insideWorld(BlockPos(0, -5, 0)),
                ),
                32,
            ),
            SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Int.Companion.MIN_VALUE, -5),
            BiomeFilter.biome(),
        )

        PlacementUtils.register(
            context,
            LAKE_CRUDE_OIL_UNDERGROUND,
            getter.getOrThrow(RagiumConfiguredFeatures.LAKE_CRUDE_OIL),
            RarityFilter.onAverageOnceEvery(18),
            InSquarePlacement.spread(),
            HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.top())),
            EnvironmentScanPlacement.scanningFor(
                Direction.DOWN,
                BlockPredicate.allOf(
                    BlockPredicate.not(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                    BlockPredicate.insideWorld(BlockPos(0, -5, 0)),
                ),
                32,
            ),
            SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Int.Companion.MIN_VALUE, -5),
            BiomeFilter.biome(),
        )
    }
}
