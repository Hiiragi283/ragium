package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.FluidState

object HTRockGeneratorCondition : HTMachineRecipeCondition {
    override val codec: MapCodec<HTRockGeneratorCondition> = MapCodec.unit(HTRockGeneratorCondition)
    override val text: MutableComponent = Component.literal("Require water and lava blocks around the machine")

    override fun test(level: Level, pos: BlockPos): Boolean {
        val aroundFluids: List<FluidState> = Direction.entries.map(pos::relative).map(level::getFluidState)
        return when {
            !aroundFluids.any { stateIn: FluidState -> stateIn.`is`(FluidTags.WATER) } -> false
            !aroundFluids.any { stateIn: FluidState -> stateIn.`is`(FluidTags.LAVA) } -> false
            else -> true
        }
    }
}
