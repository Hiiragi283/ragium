package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.chance.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.recipe.multi.HTShapelessInputsRecipe
import hiiragi283.ragium.api.recipe.single.HTExpRequiredRecipe
import hiiragi283.ragium.api.recipe.single.HTSingleFluidRecipe
import hiiragi283.ragium.api.recipe.single.HTSingleItemRecipe
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    // Machine
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTMultiRecipeInput, HTShapelessInputsRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val BREWING: HTDeferredRecipeType<SingleRecipeInput, HTSingleItemRecipe> = create(RagiumConst.BREWING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<HTMultiRecipeInput, HTItemWithCatalystRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedItemRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedItemRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val ENCHANTING: HTDeferredRecipeType<SingleRecipeInput, HTExpRequiredRecipe> = create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<HTMultiRecipeInput, HTComplexRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTSingleFluidRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTMultiRecipeInput, HTComplexRecipe> = create(RagiumConst.MIXING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> = create(RagiumConst.PLANTING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTMultiRecipeInput, HTComplexRecipe> = create(RagiumConst.REFINING)

    @JvmField
    val SIMULATING: HTDeferredRecipeType<HTMultiRecipeInput, HTComplexRecipe> = create(RagiumConst.SIMULATING)

    @JvmField
    val WASHING: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> = create(RagiumConst.WASHING)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(path: String): HTDeferredRecipeType<INPUT, RECIPE> =
        HTDeferredRecipeType(RagiumAPI.id(path))
}
