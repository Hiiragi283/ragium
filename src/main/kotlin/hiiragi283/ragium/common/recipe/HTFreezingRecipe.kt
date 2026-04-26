package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTFreezingRecipe(
    val ingredient: HTFluidIngredient,
    val catalyst: Ingredient,
    val result: HTItemResult,
    override val time: Int,
) : HTProcessingRecipe.Serializable<HTItemAndFluidRecipeInput> {
    override fun test(input: HTItemAndFluidRecipeInput): Boolean = ingredient.test(input.fluid) && catalyst.test(input.item)

    override fun assemble(input: HTItemAndFluidRecipeInput, preview: Boolean): ItemStack = result.getOrEmpty(preview)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.FREEZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.FREEZING.get()
}
