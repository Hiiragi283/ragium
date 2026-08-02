package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import hiiragi283.core.support.recipe.base.HTBasicDoubleItemToItemRecipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTAssemblingRecipe(primary: HTItemIngredient, secondary: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTBasicDoubleItemToItemRecipe(primary, secondary, true, result, progressData),
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTAssemblingRecipe> = codec(::HTAssemblingRecipe)
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ASSEMBLING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ASSEMBLING
}
