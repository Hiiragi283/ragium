package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.function.BiPredicate

interface HTCompressingRecipe :
    HTRecipe,
    BiPredicate<ImmutableItemStack, ImmutableItemStack>,
    HTItemIngredient.CountGetter {
    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val first: ImmutableItemStack = input.item(0) ?: return false
        val second: ImmutableItemStack = input.item(1) ?: return false
        return test(first, second)
    }

    abstract override fun test(first: ImmutableItemStack, second: ImmutableItemStack): Boolean

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSING.get()
}
