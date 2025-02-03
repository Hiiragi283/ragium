package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.extension.asHolderText
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.machine.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome

class HTBiomeCondition(val biomes: HolderSet<Biome>) : HTMachineRecipeCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBiomeCondition> = RegistryCodecs
            .homogeneousList(Registries.BIOME)
            .fieldOf("biome")
            .xmap(::HTBiomeCondition, HTBiomeCondition::biomes)
    }

    constructor(biome: Holder<Biome>) : this(HolderSet.direct(biome))

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: Component = Component
        .translatable(
            RagiumTranslationKeys.BIOME_CONDITION,
            biomes.asHolderText { holder: Holder<Biome> -> Component.literal(holder.idOrThrow.toString()) },
        ).withStyle(ChatFormatting.BLUE)

    override fun test(level: Level, pos: BlockPos): Boolean = level.getBiome(pos) in biomes
}
