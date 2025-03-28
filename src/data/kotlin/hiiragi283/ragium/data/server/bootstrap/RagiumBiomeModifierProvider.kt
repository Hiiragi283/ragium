package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.data.HTWorldGenData
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers

object RagiumBiomeModifierProvider : RegistrySetBuilder.RegistryBootstrap<BiomeModifier> {
    private lateinit var biomeGetter: HolderGetter<Biome>

    override fun run(context: BootstrapContext<BiomeModifier>) {
        biomeGetter = context.lookup(Registries.BIOME)
        // Ore
        register(
            context,
            RagiumWorldGenData.RAGINITE_ORE,
            BiomeTags.IS_OVERWORLD,
            GenerationStep.Decoration.UNDERGROUND_ORES,
        )
    }

    private fun register(
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
    }
}
