package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTDuplicatingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTSimpleDuplicatingRecipe(val ingredient: HTItemIngredient, val requiredMatter: Int, override val time: Int) :
    HTDuplicatingRecipe {
    override fun testItem(stack: ItemStack): Boolean = ingredient.testOnlyType(stack)

    override fun getRequiredMatter(stack: ItemStack): Int = requiredMatter

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.DUPLICATING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.DUPLICATING.get()
}
