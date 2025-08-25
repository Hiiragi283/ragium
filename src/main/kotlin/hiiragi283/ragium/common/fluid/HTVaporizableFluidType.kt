package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

open class HTVaporizableFluidType(val drop: HTItemResult, properties: Properties) : FluidType(properties) {
    companion object {
        @JvmStatic
        fun create(drop: HTItemResult): (Properties) -> HTVaporizableFluidType = { prop: Properties ->
            HTVaporizableFluidType(drop, prop)
        }
    }

    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = level.dimensionType().ultraWarm

    override fun onVaporize(
        player: Player?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        super.onVaporize(player, level, pos, stack)
        drop.getStackResult(level.registryAccess()).ifSuccess { crystal: ItemStack ->
            if (player != null) {
                dropStackAt(player, crystal)
            } else {
                dropStackAt(level, pos, crystal)
            }
        }
    }
}
