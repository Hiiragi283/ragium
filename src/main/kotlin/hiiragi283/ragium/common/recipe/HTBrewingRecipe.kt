package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.item.alchemy.HTPotionContents
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.common.recipe.base.HTBasicCombineRecipe
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

    override fun testFluid(stack: ImmutableFluidStack): Boolean = FLUID_INGREDIENT.test(stack)

    override fun getRequiredAmount(input: HTRecipeInput): Int = FLUID_INGREDIENT.getRequiredAmount()

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        HTPotionHelper.createPotion(RagiumItems.POTION_DROP, contents).toImmutable()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BREWING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BREWING.get()
}
