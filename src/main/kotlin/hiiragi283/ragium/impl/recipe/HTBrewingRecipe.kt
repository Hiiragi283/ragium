package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.item.alchemy.HTPotionContents
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.impl.recipe.base.HTBasicCombineRecipe
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTBrewingRecipe(itemIngredients: Pair<HTItemIngredient, HTItemIngredient>, val contents: HTPotionContents) :
    HTBasicCombineRecipe(itemIngredients) {
    companion object {
        @JvmField
        val FLUID_INGREDIENT: HTFluidIngredient = RagiumPlatform.INSTANCE.fluidCreator().water(1000)
    }

    override fun testFluid(input: HTMultiRecipeInput): Boolean = FLUID_INGREDIENT.test(input.getFluid(0))

    override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = when (test(input)) {
        true -> HTPotionHelper.createPotion(RagiumItems.POTION_DROP, contents).toImmutable()
        false -> null
    }

    override fun isIncomplete(): Boolean {
        val (left: HTItemIngredient, right: HTItemIngredient) = itemIngredients
        val bool1: Boolean = left.hasNoMatchingStacks()
        val bool2: Boolean = right.hasNoMatchingStacks()
        val bool3: Boolean = contents.isEmpty()
        return bool1 || bool2 || bool3
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BREWING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BREWING.get()

    override fun getRequiredAmount(input: HTMultiRecipeInput, stack: ImmutableFluidStack): Int = FLUID_INGREDIENT.getRequiredAmount(stack)
}
