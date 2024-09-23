package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.common.RagiumContents
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.RegistryWrapper

interface HTAlchemyRecipe : HTRecipeBase<HTAlchemyRecipe.Input> {
    val result: ItemStack

    override val outputs: List<HTRecipeResult>
        get() = listOf(HTRecipeResult.item(result.item, result.count))

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = result.copy()

    override fun createIcon(): ItemStack = RagiumContents.ALCHEMICAL_INFUSER.asItem().defaultStack

    override fun getType(): RecipeType<*> = Type

    //    Type    //

    data object Type : RecipeType<HTAlchemyRecipe>

    //    Input    //

    class Input(
        private val first: ItemStack,
        private val second: ItemStack,
        private val third: ItemStack,
        private val fourth: ItemStack,
    ) : RecipeInput {
        fun matches(
            first: WeightedIngredient?,
            second: WeightedIngredient?,
            third: WeightedIngredient?,
            fourth: WeightedIngredient?,
        ): Boolean = matchesInternal(
            first ?: WeightedIngredient.EMPTY,
            second ?: WeightedIngredient.EMPTY,
            third ?: WeightedIngredient.EMPTY,
            fourth ?: WeightedIngredient.EMPTY,
        )

        private fun matchesInternal(
            first: WeightedIngredient,
            second: WeightedIngredient,
            third: WeightedIngredient,
            fourth: WeightedIngredient,
        ): Boolean = when {
            !first.test(this.first) -> false
            !second.test(this.second) -> false
            !third.test(this.third) -> false
            !fourth.test(this.fourth) -> false
            else -> true
        }

        //    RecipeInput    //

        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> first
            1 -> second
            2 -> third
            3 -> fourth
            else -> throw IndexOutOfBoundsException()
        }

        override fun getSize(): Int = 4
    }
}
