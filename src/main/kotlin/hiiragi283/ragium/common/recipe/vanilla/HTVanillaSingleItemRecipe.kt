package hiiragi283.ragium.common.recipe.vanilla

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.HTSingleItemInputRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTVanillaSingleItemRecipe<RECIPE : Recipe<SingleRecipeInput>>(
    protected val recipe: RECIPE,
    protected val ingredient: HTItemIngredient,
    protected val resultFactory: HTVanillaResultFactory,
) : HTRecipe.Fake,
    HTSingleItemInputRecipe {
    constructor(recipe: RECIPE) : this(
        recipe,
        HTItemIngredient(recipe.ingredients[0], 1),
        { input: HTRecipeInput, provider: HolderLookup.Provider ->
            val item: ItemStack? = input.item(0)?.unwrap()
            if (item != null) {
                recipe.assemble(SingleRecipeInput(item), provider)
            } else {
                ItemStack.EMPTY
            }
        },
    )

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        resultFactory.assemble(input, provider).toImmutable()

    override fun test(stack: ImmutableItemStack): Boolean = ingredient.test(stack)

    override fun getRequiredCount(): Int = ingredient.getRequiredAmount()
}
