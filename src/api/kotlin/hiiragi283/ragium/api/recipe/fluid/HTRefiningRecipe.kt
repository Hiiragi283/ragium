package hiiragi283.ragium.api.recipe.fluid

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.function.Predicate

interface HTRefiningRecipe :
    HTFluidRecipe,
    Predicate<ImmutableFluidStack>,
    HTFluidIngredient.AmountGetter {
    override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testFluid(0, this::test)

    override fun test(stack: ImmutableFluidStack): Boolean

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINING.get()
}
