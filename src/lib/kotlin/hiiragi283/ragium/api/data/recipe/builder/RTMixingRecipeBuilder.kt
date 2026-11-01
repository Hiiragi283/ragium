package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.recipe.builder.HTItemOrFluidIngredientAcceptor
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.result.HTItemOrFluidResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTMixingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier

class RTMixingRecipeBuilder :
    HTProgressRecipeBuilder<RTMixingRecipe>(RagiumConstants.MIXING),
    HTItemOrFluidIngredientAcceptor,
    HTItemOrFluidResultBuilder.Holder {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RTMixingRecipe = RTMixingRecipe(
        itemIngredients.toNel(),
        fluidIngredient,
        result,
        progressData,
    )

    // Ingredients
    private val itemIngredients: MutableList<HTItemIngredient> = ObjectArrayList()
    private var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    override operator fun HTItemIngredient.unaryPlus() {
        itemIngredients += this
    }

    override operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = this
    }

    // Results
    override val builder: HTItemOrFluidResultBuilder = HTItemOrFluidResultBuilder()
}
