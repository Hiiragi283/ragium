package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome

class HTBiomeCondition(val biome: ResourceKey<Biome>) : HTMachineRecipeCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBiomeCondition> = ResourceKey
            .codec(Registries.BIOME)
            .fieldOf("biome")
            .xmap(::HTBiomeCondition, HTBiomeCondition::biome)
    }

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: MutableComponent = Component.literal("Required biome: ${biome.location()}")

    override fun test(level: Level, pos: BlockPos): Boolean = level.getBiome(pos).`is`(biome)
}
