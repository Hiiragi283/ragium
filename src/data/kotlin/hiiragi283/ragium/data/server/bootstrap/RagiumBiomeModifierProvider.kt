package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.data.HTWorldGenData
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers

object RagiumBiomeModifierProvider : RegistrySetBuilder.RegistryBootstrap<BiomeModifier> {
    private lateinit var biomeGetter: HolderGetter<Biome>

    override fun run(context: BootstrapContext<BiomeModifier>) {
        biomeGetter = context.lookup(Registries.BIOME)
        // Ore
        registerFeature(
            context,
            RagiumWorldGenData.ORE_RAGINITE,
            biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            RagiumWorldGenData.ORE_RAGINITE_LOWER,
        )

        registerFeature(
            context,
            RagiumWorldGenData.ORE_RESONANT_DEBRIS,
            HolderSet.direct(biomeGetter::getOrThrow, Biomes.DEEP_DARK),
            GenerationStep.Decoration.UNDERGROUND_ORES,
        )
    }

    private fun registerFeature(
        context: BootstrapContext<BiomeModifier>,
        data: HTWorldGenData,
        biomes: HolderSet<Biome>,
        step: GenerationStep.Decoration,
        vararg subData: HTWorldGenData,
    ) {
        val allData: Set<HTWorldGenData> = setOf(data, *subData)
        val lookup: HolderGetter<PlacedFeature> = context.lookup(Registries.PLACED_FEATURE)
        context.register(
            data.modifierKey,
            BiomeModifiers.AddFeaturesBiomeModifier(
                biomes,
                HolderSet.direct(lookup::getOrThrow, allData.map(HTWorldGenData::placedKey)),
                step,
            ),
        )
    }
}
