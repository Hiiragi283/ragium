package hiiragi283.ragium.data.bootstrap

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.worldgen.HTWorldGenData
import hiiragi283.core.api.data.worldgen.HTWorldGenHelper
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.getResult
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.util.getOrThrow
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries

/**
 * @see net.minecraft.data.worldgen.features.OreFeatures
 * @see net.minecraft.data.worldgen.placement.OrePlacements
 */
data object RagiumWorldData {
    @JvmField
    val ORE_RAGINITE = HTWorldGenData(RagiumAPI.id("ore_raginite"))

    private val STONE_ORE = TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES)
    private val DEEPSLATE_ORE = TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES)

    @JvmStatic
    fun bootsrap(builder: RegistrySetBuilder) {
        builder
            .add(Registries.CONFIGURED_FEATURE) { context: BootstrapContext<ConfiguredFeature<*, *>> ->
                HTWorldGenHelper.register(
                    context,
                    ORE_RAGINITE,
                    Feature.ORE,
                    OreConfiguration(
                        mapOf(
                            STONE_ORE to getBlockOrThrow(CommonParts.ORE, RagiumMaterialKeys.RAGINITE),
                            DEEPSLATE_ORE to getBlockOrThrow(CommonParts.ORE_DEEPSLATE, RagiumMaterialKeys.RAGINITE),
                        ).map { (rule: TagMatchTest, block: HTMaterialContents.BlockEntry) ->
                            OreConfiguration.target(rule, block.get().defaultBlockState())
                        },
                        10,
                    ),
                )
            }.add(Registries.PLACED_FEATURE) { context: BootstrapContext<PlacedFeature> ->
                HTWorldGenHelper.register(
                    context,
                    ORE_RAGINITE,
                    listOf(
                        InSquarePlacement.spread(),
                        BiomeFilter.biome(),
                        CountPlacement.of(16),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112)),
                    ),
                )
            }.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS) { context: BootstrapContext<BiomeModifier> ->
                HTWorldGenHelper.register(
                    context,
                    ORE_RAGINITE,
                    BiomeTags.IS_OVERWORLD,
                    GenerationStep.Decoration.UNDERGROUND_ORES,
                )
            }
    }

    @JvmStatic
    private fun getBlockOrThrow(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.BlockEntry = HiiragiCoreAccess.INSTANCE.registeredContents.blocks.getResult(part, material).getOrThrow()
}
