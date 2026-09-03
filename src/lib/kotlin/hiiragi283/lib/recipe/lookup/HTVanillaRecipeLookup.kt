package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.HTRecipeHolder
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier

/**
 * バニラの[Recipe]に基づいた[HTRecipeLookup]の実装クラスです。
 * @param INPUT レシピの入力のクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTVanillaRecipeLookup<INPUT : RecipeInput, out RECIPE : Recipe<INPUT>>(
    private val recipeType: Supplier<out RecipeType<RECIPE>>
) : HTRecipeLookup<RECIPE> {
    constructor(recipeType: RecipeType<RECIPE>) : this(Supplier { recipeType })

    override fun getAllRecipesN(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>> =
        context.byType(recipeType.get())
}
