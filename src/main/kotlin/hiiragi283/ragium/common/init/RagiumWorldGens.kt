package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.*
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

object RagiumWorldGens {

    @JvmField
    val CONFIGURED_RAGINITE_OVERWORLD: ResourceKey<ConfiguredFeature<*, *>> = createConfigured("raginite_overworld")

    @JvmField
    val PLACED_RAGINITE_OVERWORLD: ResourceKey<PlacedFeature> = createPlaced("raginite_overworld")

    @JvmStatic
    fun createConfigured(path: String): ResourceKey<ConfiguredFeature<*, *>> =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, RagiumAPI.id(path))

    @JvmStatic
    fun createPlaced(path: String): ResourceKey<PlacedFeature> =
        ResourceKey.create(Registries.PLACED_FEATURE, RagiumAPI.id(path))

    @JvmStatic
    fun boostrapConfiguredFeatures(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        fun register(
            key: ResourceKey<ConfiguredFeature<*, *>>,
            target: TagKey<Block>,
            ore: RagiumBlocks.Ores,
            count: Int,
        ) {
            context.register(
                key,
                ConfiguredFeature(
                    Feature.ORE,
                    OreConfiguration(
                        listOf(OreConfiguration.target(TagMatchTest(target), ore.get().defaultBlockState())),
                        count
                    )
                )
            )
        }

        // Overworld
        register(
            CONFIGURED_RAGINITE_OVERWORLD,
            BlockTags.BASE_STONE_OVERWORLD,
            RagiumBlocks.Ores.CRUDE_RAGINITE,
            8
        )
    }

    @JvmStatic
    fun boostrapPlacedFeatures(context: BootstrapContext<PlacedFeature>) {
        val getter: HolderGetter<ConfiguredFeature<*, *>> = context.lookup(Registries.CONFIGURED_FEATURE)
        // Overworld
        context.register(
            PLACED_RAGINITE_OVERWORLD,
            PlacedFeature(
                getter.getOrThrow(CONFIGURED_RAGINITE_OVERWORLD),
                listOf(
                    CountPlacement.of(32),
                    InSquarePlacement.spread(),
                    HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
                    BiomeFilter.biome()
                )
            )
        )
    }
}
