package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.storage.fluid.ImmutableFluidStack
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [FluidStack]向けの[HTIngredient]の拡張インターフェース
 */
interface HTFluidIngredient : HTIngredient<FluidStack> {
    fun unwrap(): Either<Pair<TagKey<Fluid>, Int>, List<FluidStack>>

    fun getRequiredAmount(stack: ImmutableFluidStack): Int = getRequiredAmount(stack.stack)

    fun interface CountGetter {
        fun getRequiredAmount(stack: FluidStack): Int

        fun getRequiredAmount(stack: ImmutableFluidStack): Int = getRequiredAmount(stack.stack)
    }
}
