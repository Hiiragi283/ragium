package hiiragi283.lib.recipe.handler.lookup

import hiiragi283.lib.recipe.handler.cache.HTSingleRecipeCache
import hiiragi283.lib.util.Either
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.function.Predicate

/**
 * 単一の入力に対する[HTRecipeLookupHandler]の拡張インターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTSingleRecipeLookupHandler<INPUT : Any, RECIPE : Predicate<INPUT>> : HTRecipeLookupHandler {
    val recipeCache: HTSingleRecipeCache<INPUT, RECIPE>

    fun hasRecipe(input: INPUT): Boolean = recipeCache.findAllHolder(input, createLookupContext()).any()

    fun findFirstRecipe(input: INPUT): RECIPE? = recipeCache.findFirstRecipe(input, createLookupContext())

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface ForFluid<RECIPE : Predicate<FluidInstance>> : HTSingleRecipeLookupHandler<FluidInstance, RECIPE> {
        fun hasRecipe(input: FluidResource): Boolean = hasRecipe(input.toStack(Int.MAX_VALUE))
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface ForItem<RECIPE : Predicate<ItemInstance>> : HTSingleRecipeLookupHandler<ItemInstance, RECIPE> {
        fun hasRecipe(input: ItemResource): Boolean = hasRecipe(input.toStack(input.maxStackSize))
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface ForItemOrFluid<RECIPE : Predicate<Either<ItemInstance, FluidInstance>>> :
        HTSingleRecipeLookupHandler<Either<ItemInstance, FluidInstance>, RECIPE> {
        fun hasRecipe(input: FluidResource): Boolean = hasRecipe(Either.Right(input.toStack(Int.MAX_VALUE)))

        fun hasRecipe(input: ItemResource): Boolean = hasRecipe(Either.Left(input.toStack(input.maxStackSize)))
    }
}
