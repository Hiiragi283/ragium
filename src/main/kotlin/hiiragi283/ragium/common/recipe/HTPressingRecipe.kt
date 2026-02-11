package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTSingleCatalystRecipe
import hiiragi283.ragium.common.recipe.input.HTSingleCatalystRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTPressingRecipe(
    ingredient: HTItemIngredient,
    catalyst: HTItemIngredient,
    val result: HTItemResult,
    val copyComponent: Boolean,
    parameters: SubParameters,
) : HTSingleCatalystRecipe<HTItemIngredient>(ingredient, catalyst, parameters) {
    override fun matchIngredient(input: HTSingleCatalystRecipeInput): Boolean = ingredient.test(input.getItem(0))

    override fun assemble(input: HTSingleCatalystRecipeInput, registries: HolderLookup.Provider): ItemStack {
        val stack: ItemStack = getResultItem(registries)
        if (!stack.isEmpty && copyComponent) {
            stack.applyComponents(input.catalyst.components)
        }
        return stack
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PRESSING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PRESSING.get()
}
