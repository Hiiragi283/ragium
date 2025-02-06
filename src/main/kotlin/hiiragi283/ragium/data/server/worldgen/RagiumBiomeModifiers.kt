package hiiragi283.ragium.data.server.worldgen

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers
import net.neoforged.neoforge.registries.NeoForgeRegistries

object RagiumBiomeModifiers {
    @JvmField
    val OVERWORLD_RAGINITE: ResourceKey<BiomeModifier> = createKey("overworld_raginite")

    @JvmField
    val NETHER_RAGINITE: ResourceKey<BiomeModifier> = createKey("nether_raginite")

    @JvmField
    val END_RAGINITE: ResourceKey<BiomeModifier> = createKey("end_raginite")

    @JvmField
    val LAKE_CRUDE_OIL_SURFACE: ResourceKey<BiomeModifier> = createKey("lake_crude_oil_surface")

    @JvmField
    val LAKE_CRUDE_OIL_UNDERGROUND: ResourceKey<BiomeModifier> = createKey("lake_crude_oil_underground")

    @JvmStatic
    private fun createKey(path: String): ResourceKey<BiomeModifier> =
        ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RagiumAPI.id(path))

    @JvmStatic
    fun boostrap(context: BootstrapContext<BiomeModifier>) {
        val featureGetter: HolderGetter<PlacedFeature> = context.lookup(Registries.PLACED_FEATURE)
        val biomeGetter: HolderGetter<Biome> = context.lookup(Registries.BIOME)
        // Overworld
        context.register(
            OVERWORLD_RAGINITE,
            BiomeModifiers.AddFeaturesBiomeModifier(
                biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(featureGetter.getOrThrow(RagiumPlacedFeatures.OVERWORLD_RAGINITE)),
                GenerationStep.Decoration.UNDERGROUND_ORES,
            ),
        )
        // Nether
        context.register(
            NETHER_RAGINITE,
            BiomeModifiers.AddFeaturesBiomeModifier(
                biomeGetter.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(featureGetter.getOrThrow(RagiumPlacedFeatures.NETHER_RAGINITE)),
                GenerationStep.Decoration.UNDERGROUND_ORES,
            ),
        )
        // The End
        context.register(
            END_RAGINITE,
            BiomeModifiers.AddFeaturesBiomeModifier(
                biomeGetter.getOrThrow(BiomeTags.IS_END),
                HolderSet.direct(featureGetter.getOrThrow(RagiumPlacedFeatures.END_RAGINITE)),
                GenerationStep.Decoration.UNDERGROUND_ORES,
            ),
        )

        // Crude Oil
        context.register(
            LAKE_CRUDE_OIL_SURFACE,
            BiomeModifiers.AddFeaturesBiomeModifier(
                biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(featureGetter.getOrThrow(RagiumPlacedFeatures.LAKE_CRUDE_OIL_SURFACE)),
                GenerationStep.Decoration.LAKES,
            ),
        )

        context.register(
            LAKE_CRUDE_OIL_UNDERGROUND,
            BiomeModifiers.AddFeaturesBiomeModifier(
                biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(featureGetter.getOrThrow(RagiumPlacedFeatures.LAKE_CRUDE_OIL_UNDERGROUND)),
                GenerationStep.Decoration.LAKES,
            ),
        )
    }
}
