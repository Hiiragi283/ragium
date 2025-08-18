package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

class HTNetherVaporizableFluidType(drop: HTRecipeResult<ItemStack>, properties: Properties) : HTVaporizableFluidType(drop, properties) {
    companion object {
        @JvmStatic
        fun create(drop: HTRecipeResult<ItemStack>): (Properties) -> HTNetherVaporizableFluidType = { prop: Properties ->
            HTNetherVaporizableFluidType(drop, prop)
        }
    }

    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean =
        !super.isVaporizedOnPlacement(level, pos, stack)
}
