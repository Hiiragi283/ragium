package hiiragi283.ragium.api.recipe.alchemy

import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.recipe.HTRecipeBase
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.common.init.RagiumBlocks
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

    override fun createIcon(): ItemStack = RagiumBlocks.ALCHEMICAL_INFUSER.asItem().defaultStack

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALCHEMY

    //    Input    //

    class Input(
        private val first: ItemStack,
        private val second: ItemStack,
        private val third: ItemStack,
        private val fourth: ItemStack,
    ) : RecipeInput {
        fun matches(
            first: HTIngredient?,
            second: HTIngredient?,
            third: HTIngredient?,
            fourth: HTIngredient?,
        ): Boolean = matchesInternal(
            first ?: HTIngredient.EMPTY,
            second ?: HTIngredient.EMPTY,
            third ?: HTIngredient.EMPTY,
            fourth ?: HTIngredient.EMPTY,
        )

        private fun matchesInternal(
            first: HTIngredient,
            second: HTIngredient,
            third: HTIngredient,
            fourth: HTIngredient,
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
