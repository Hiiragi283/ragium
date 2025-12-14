package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.extra.HTPlantingRecipe
import hiiragi283.ragium.api.recipe.extra.HTSingleExtraItemRecipe
import hiiragi283.ragium.api.recipe.fluid.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.fluid.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.fluid.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.fluid.HTSimulatingRecipe
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.multi.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.multi.HTFluidWithCatalystRecipe
import hiiragi283.ragium.api.recipe.multi.HTRockGeneratingRecipe
import hiiragi283.ragium.api.recipe.multi.HTShapelessInputsRecipe
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

object RagiumRecipeTypes {
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTRecipeInput, HTShapelessInputsRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val BREWING: HTDeferredRecipeType<HTRecipeInput, HTCombineRecipe> = create(RagiumConst.BREWING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<HTRecipeInput, HTCompressingRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<HTRecipeInput, HTSingleExtraItemRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<HTRecipeInput, HTSingleExtraItemRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTRecipeInput, HTCombineRecipe> = create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<HTRecipeInput, HTExtractingRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val MELTING: HTDeferredRecipeType<HTRecipeInput, HTMeltingRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTRecipeInput, HTComplexRecipe> = create(RagiumConst.MIXING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<HTRecipeInput, HTPlantingRecipe> = create(RagiumConst.PLANTING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTRecipeInput, HTRefiningRecipe> = create(RagiumConst.REFINING)

    @JvmField
    val ROCK_GENERATING: HTDeferredRecipeType<HTRecipeInput, HTRockGeneratingRecipe> = create(RagiumConst.ROCK_GENERATING)

    @JvmField
    val SIMULATING: HTDeferredRecipeType<HTRecipeInput, HTSimulatingRecipe> = create(RagiumConst.SIMULATING)

    @JvmField
    val SOLIDIFYING: HTDeferredRecipeType<HTRecipeInput, HTFluidWithCatalystRecipe> = create(RagiumConst.SOLIDIFYING)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(path: String): HTDeferredRecipeType<INPUT, RECIPE> =
        HTDeferredRecipeType(RagiumAPI.id(path))
}
