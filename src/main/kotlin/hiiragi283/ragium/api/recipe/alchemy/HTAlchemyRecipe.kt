package hiiragi283.ragium.api.recipe.alchemy

import hiiragi283.ragium.api.recipe.HTRecipeBase
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.WeightedIngredient
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.RegistryWrapper

interface HTAlchemyRecipe : HTRecipeBase<HTAlchemyRecipe.Input> {
    val result: HTRecipeResult

    override val outputs: List<HTRecipeResult>
        get() = listOf(result)

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = result.toStack()

    override fun createIcon(): ItemStack = RagiumContents.ALCHEMICAL_INFUSER.asItem().defaultStack

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALCHEMY

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
