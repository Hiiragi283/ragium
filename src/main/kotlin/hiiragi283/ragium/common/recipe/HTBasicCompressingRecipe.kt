package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.recipe.base.HTBasicSingleOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer

class HTBasicCompressingRecipe(val ingredient: HTItemIngredient, val mold: HTMoldType, result: HTItemResult) :
    HTBasicSingleOutputRecipe(result),
    HTCompressingRecipe {
    override fun test(first: ImmutableItemStack, second: ImmutableItemStack): Boolean = ingredient.test(first) && second.isOf(mold.asItem())

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSING

    override fun getRequiredCount(): Int = ingredient.getRequiredAmount()
}
