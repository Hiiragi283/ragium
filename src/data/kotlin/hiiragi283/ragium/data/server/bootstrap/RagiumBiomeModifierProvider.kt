package hiiragi283.ragium.data.server.bootstrap

import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.NetherPlacements
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biome
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers

object RagiumBiomeModifierProvider : RegistrySetBuilder.RegistryBootstrap<BiomeModifier> {
    private lateinit var biomeGetter: HolderGetter<Biome>

    override fun run(context: BootstrapContext<BiomeModifier>) {
        biomeGetter = context.lookup(Registries.BIOME)
        // Ore
        /*registerFeature(
            context,
            RagiumWorldGenData.ORE_RAGINITE,
            BiomeTags.IS_OVERWORLD,
            GenerationStep.Decoration.UNDERGROUND_ORES,
        )*/

        // Remove Lava Springs
        context.register(
            RagiumWorldGenData.REMOVE_SPRING_NETHER.modifierKey,
            BiomeModifiers.RemoveFeaturesBiomeModifier.allSteps(
                biomeGetter.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(
                    context.lookup(Registries.PLACED_FEATURE)::getOrThrow,
                    NetherPlacements.SPRING_OPEN,
                    NetherPlacements.SPRING_CLOSED,
                    NetherPlacements.SPRING_CLOSED_DOUBLE,
                ),
            ),
        )
    }

    /*private fun registerFeature(
        context: BootstrapContext<BiomeModifier>,
        data: HTWorldGenData,
        biomeTag: TagKey<Biome>,
        step: GenerationStep.Decoration,
    ) {
        context.register(
            data.modifierKey,
            BiomeModifiers.AddFeaturesBiomeModifier(
                biomeGetter.getOrThrow(biomeTag),
                HolderSet.direct(data.placedHolder),
                step,
            ),
        )
    }*/
}
