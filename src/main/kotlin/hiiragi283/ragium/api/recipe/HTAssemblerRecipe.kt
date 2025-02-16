package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTAssemblerRecipe(group: String, itemInputs: List<HTItemIngredient>, itemOutput: HTItemOutput) :
    HTMultiItemRecipe(group, itemInputs, itemOutput) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ASSEMBLER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ASSEMBLER.get()
}
