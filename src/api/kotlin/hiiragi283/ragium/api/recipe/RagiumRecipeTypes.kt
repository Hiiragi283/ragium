package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput

object RagiumRecipeTypes {
    @JvmField
    val CRAFTING: HTDeferredRecipeType<CraftingInput, CraftingRecipe> = HTDeferredRecipeType.createType(vanillaId("crafting"))

    @JvmField
    val SMITHING: HTDeferredRecipeType<SmithingRecipeInput, SmithingRecipe> = HTDeferredRecipeType.createType(vanillaId("smithing"))

    @JvmField
    val STONECUTTER: HTDeferredRecipeType<SingleRecipeInput, SingleItemRecipe> = HTDeferredRecipeType.createType(vanillaId("stonecutting"))

    @JvmField
    val SAWMILL: HTDeferredRecipeType<SingleRecipeInput, SingleItemRecipe> = create("sawmill")

    // Machine
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTMultiItemRecipeInput, HTCombineItemToItemRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedItemRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTMultiItemRecipeInput, HTCombineItemToItemRecipe> = create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val FLUID_TRANSFORM: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTFluidTransformRecipe> = create(RagiumConst.FLUID_TRANSFORM)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToFluidRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val SIMULATING: HTDeferredRecipeType<HTMultiItemRecipeInput, HTItemWithCatalystToItemRecipe> = create(RagiumConst.SIMULATING)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(path: String): HTDeferredRecipeType<INPUT, RECIPE> =
        HTDeferredRecipeType.createType(RagiumAPI.id(path))
}
