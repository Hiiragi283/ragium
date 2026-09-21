package hiiragi283.ragium.common.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import hiiragi283.ragium.api.data.oreSlurry.HTOreSlurryDataHelper
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidType

data object RTOreSlurryWashingRecipe :
    RTReactingRecipe,
    HTProgressRecipe.Simple<HTFluidRecipeInput> {
    override fun test(first: FluidInstance, second: FluidInstance): Boolean {
        // Water
        if (!second.`is`(Tags.Fluids.WATER) || second.amount() < FluidType.BUCKET_VOLUME) return false
        // Ore Slurry
        return HTOreSlurryDataHelper.getHolder(first) != null
    }

    override fun apply(first: FluidInstance, second: FluidInstance): HTItemAndFluidResult =
        HTOreSlurryDataHelper.getData(first)?.result.let(HTItemAndFluidResult.Companion::from)

    override fun getMatchingStack(first: FluidInstance, second: FluidInstance): Pair<FluidInstance, FluidInstance> =
        Pair(
            HTIngredientHelper.copyWithAmount(first, FluidType.BUCKET_VOLUME),
            HTIngredientHelper.copyWithAmount(second, FluidType.BUCKET_VOLUME)
        )

    override val progressData: HTProgressData = HTProgressData.time(200)
}
