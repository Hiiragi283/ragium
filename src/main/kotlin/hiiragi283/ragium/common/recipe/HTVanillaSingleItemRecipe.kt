package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTVanillaSingleItemRecipe<RECIPE : Recipe<SingleRecipeInput>>(
    protected val recipe: RECIPE,
    protected val ingredient: HTItemIngredient,
    protected val resultFactory: HTVanillaResultFactory<SingleRecipeInput>,
) : HTRecipe.Fake<SingleRecipeInput>,
    HTItemIngredient.CountGetter {
    constructor(recipe: RECIPE) : this(
        recipe,
        HTItemIngredient.convert(recipe.ingredients[0]),
        { input: SingleRecipeInput, provider: HolderLookup.Provider -> recipe.assemble(input, provider) },
    )

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    override fun assembleItem(input: SingleRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        resultFactory.assemble(input, provider).toImmutable()

    final override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks()

    final override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)
}
