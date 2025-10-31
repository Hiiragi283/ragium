package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [FluidStack]向けの[HTIngredient]の拡張インターフェース
 */
interface HTFluidIngredient : HTIngredient<ImmutableFluidStack> {
    fun unwrap(): Either<Pair<TagKey<Fluid>, Int>, List<ImmutableFluidStack>>

    fun test(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::test) ?: false

    fun testOnlyType(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::testOnlyType) ?: false

    fun interface CountGetter {
        fun getRequiredAmount(stack: ImmutableFluidStack): Int
    }
}
