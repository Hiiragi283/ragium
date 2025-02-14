package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.*

class HTCompressorRecipe(
    group: String,
    input: HTItemIngredient,
    catalyst: Optional<Ingredient>,
    itemResult: HTItemResult,
) : HTSingleItemRecipe(group, input, catalyst, itemResult) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSOR.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSOR.get()
}
