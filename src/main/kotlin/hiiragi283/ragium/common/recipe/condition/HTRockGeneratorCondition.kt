package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.tags.FluidTags
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.FluidState

data class HTRockGeneratorCondition(override val itemIngredient: Ingredient) : HTMachineRecipeCondition.ItemBased {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRockGeneratorCondition> = Ingredient.CODEC_NONEMPTY
            .fieldOf("ingredient")
            .xmap(::HTRockGeneratorCondition, HTRockGeneratorCondition::itemIngredient)
    }

    override val codec: MapCodec<HTRockGeneratorCondition> = CODEC
    override val text: MutableComponent = Component.literal("Require water and lava blocks around the machine")

    override fun test(level: Level, pos: BlockPos): Boolean {
        val aroundFluids: List<FluidState> = Direction.entries.map(pos::relative).map(level::getFluidState)
        if (aroundFluids.none { stateIn: FluidState -> stateIn.`is`(FluidTags.WATER) }) {
            return false
        }
        if (aroundFluids.none { stateIn: FluidState -> stateIn.`is`(FluidTags.LAVA) }) {
            return false
        }
        return HTProcessorCatalystCondition(itemIngredient).test(level, pos)
    }
}
