package hiiragi283.ragium.data.server.worldgen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.LakeFeature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredBlock

object RagiumConfiguredFeatures {
    @JvmField
    val OVERWORLD_RAGINITE: ResourceKey<ConfiguredFeature<*, *>> = createKey("overworld_raginite")

    @JvmField
    val NETHER_RAGINITE: ResourceKey<ConfiguredFeature<*, *>> = createKey("nether_raginite")

    @JvmField
    val END_RAGINITE: ResourceKey<ConfiguredFeature<*, *>> = createKey("end_raginite")

    @JvmField
    val LAKE_CRUDE_OIL: ResourceKey<ConfiguredFeature<*, *>> = createKey("lake_crude_oil")

    @JvmStatic
    private fun createKey(path: String): ResourceKey<ConfiguredFeature<*, *>> =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, RagiumAPI.id(path))

    @Suppress("DEPRECATION")
    @JvmStatic
    fun boostrap(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        fun createTarget(target: TagKey<Block>, variant: HTOreVariant, key: HTMaterialKey): OreConfiguration.TargetBlockState {
            val ore: DeferredBlock<out Block> = RagiumBlocks.ORES.get(variant, key) ?: error("Unknown ore found")
            return OreConfiguration.target(TagMatchTest(target), ore.get().defaultBlockState())
        }

        fun register(key: ResourceKey<ConfiguredFeature<*, *>>, count: Int, vararg targets: OreConfiguration.TargetBlockState) {
            FeatureUtils.register(
                context,
                key,
                Feature.ORE,
                OreConfiguration(listOf(*targets), count),
            )
        }

        // Overworld
        register(
            OVERWORLD_RAGINITE,
            8,
            createTarget(BlockTags.STONE_ORE_REPLACEABLES, HTOreVariant.OVERWORLD, RagiumMaterials.RAGINITE),
            createTarget(BlockTags.DEEPSLATE_ORE_REPLACEABLES, HTOreVariant.DEEPSLATE, RagiumMaterials.RAGINITE),
        )
        // Nether
        register(
            NETHER_RAGINITE,
            8,
            createTarget(BlockTags.BASE_STONE_NETHER, HTOreVariant.NETHER, RagiumMaterials.RAGINITE),
        )
        // The End
        register(END_RAGINITE, 8, createTarget(Tags.Blocks.END_STONES, HTOreVariant.END, RagiumMaterials.RAGI_CRYSTAL))

        // Crude Oil
        FeatureUtils.register(
            context,
            LAKE_CRUDE_OIL,
            Feature.LAKE,
            LakeFeature.Configuration(
                BlockStateProvider.simple(RagiumBlocks.CRUDE_OIL.get().defaultBlockState()),
                BlockStateProvider.simple(Blocks.TUFF.defaultBlockState()),
            ),
        )
    }
}
