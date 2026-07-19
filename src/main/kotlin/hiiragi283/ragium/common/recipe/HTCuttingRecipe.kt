package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.impl.recipe.HTBasicItemToMultiItemRecipe
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTCuttingRecipe(ingredient: HTItemIngredient, results: List<HTChancedItemResult>, progressData: HTProgressData) :
    HTBasicItemToMultiItemRecipe(ingredient, results, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCuttingRecipe> = codec(2, ::HTCuttingRecipe)
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CUTTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CUTTING
}
