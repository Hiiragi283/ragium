package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.RecipeKey
import java.util.function.Supplier
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

/**
 * バニラの[Recipe]に基づいた[HTRecipeLookup]の実装クラスです。
 * @param INPUT レシピの入力のクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class HTVanillaRecipeLookup<INPUT : RecipeInput, out RECIPE : Recipe<INPUT>>(private val recipeType: Supplier<out RecipeType<RECIPE>>) : HTRecipeLookup<RECIPE> {
    constructor(recipeType: RecipeType<RECIPE>) : this(Supplier { recipeType })

    override fun getAllRecipes(contextMap: ContextMap): Map<RecipeKey, RECIPE> = contextMap.getOrThrow(HTRecipeLookupContext.RECIPES)
        .byType(recipeType.get())
        .associate { holder: RecipeHolder<RECIPE> -> holder.id() to holder.value() }
}
