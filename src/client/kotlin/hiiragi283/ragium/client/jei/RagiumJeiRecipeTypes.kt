package hiiragi283.ragium.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.base.HTItemFluidMultiOutputRecipe
import hiiragi283.ragium.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTElectrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.RagiumDuplicatingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike

object RagiumJeiRecipeTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(
        recipeType: HTRecipeType<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTLookupRecipeViewerType<INPUT, RECIPE> = HTLookupRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    // Machine - Basic
    @JvmField
    val ALLOYING: HTLookupRecipeViewerType<HTShapelessRecipeInput, HTAlloyingRecipe> =
        create(RagiumRecipeLookups.ALLOYING, RagiumBlocks.ALLOY_SMELTER, 18 * 6)

    @JvmField
    val ASSEMBLING: HTLookupRecipeViewerType<HTShapelessRecipeInput, HTAssemblingRecipe> =
        create(RagiumRecipeLookups.ASSEMBLING, RagiumBlocks.ASSEMBLER, 18 * 5)

    @JvmField
    val CUTTING: HTLookupRecipeViewerType<SingleRecipeInput, HTSingleMultiOutputRecipe> =
        create(RagiumRecipeLookups.CUTTING, RagiumBlocks.CUTTING_MACHINE, 18 * 5, 18 * 2)

    @JvmField
    val PLANTING: HTLookupRecipeViewerType<HTDoubleRecipeInput, HTDoubleMultiOutputRecipe> =
        create(RagiumRecipeLookups.PLANTING, RagiumBlocks.PLANTER, 18 * 6, 18 * 3)

    // Machine - Heat
    @JvmField
    val FREEZING: HTLookupRecipeViewerType<HTItemAndFluidRecipeInput, HTFreezingRecipe> =
        create(RagiumRecipeLookups.FREEZING, RagiumBlocks.FREEZER, 18 * 6)

    @JvmField
    val MELTING: HTLookupRecipeViewerType<SingleRecipeInput, HTMeltingRecipe> =
        create(RagiumRecipeLookups.MELTING, RagiumBlocks.MELTER, 18 * 4)

    @JvmField
    val PYROLYZING: HTLookupRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        create(RagiumRecipeLookups.PYROLYZING, RagiumBlocks.PYROLYZER, 18 * 8)

    @JvmField
    val REFINING: HTLookupRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        create(RagiumRecipeLookups.REFINING, RagiumBlocks.REFINERY, 18 * 8)

    // Machine - Elite
    @JvmField
    val ELECTROLYZING: HTLookupRecipeViewerType<HTSingleFluidRecipeInput, HTElectrolyzingRecipe> =
        create(RagiumRecipeLookups.ELECTROLYZING, RagiumBlocks.MIXER, 18 * 7)

    @JvmField
    val MIXING: HTLookupRecipeViewerType<HTChemicalRecipeInput, HTMixingRecipe> =
        create(RagiumRecipeLookups.MIXING, RagiumBlocks.MIXER, 18 * 9)

    @JvmField
    val WASHING: HTLookupRecipeViewerType<HTItemAndFluidRecipeInput, HTItemFluidMultiOutputRecipe> =
        create(RagiumRecipeLookups.WASHING, RagiumBlocks.WASHER, 18 * 8)

    // Machine - Ultimate
    @JvmField
    val DUPLICATING: HTLookupRecipeViewerType<HTItemAndFluidRecipeInput, RagiumDuplicatingRecipe> =
        create(RagiumRecipeLookups.DUPLICATING, RagiumBlocks.TANK, 18 * 6)

    @JvmField
    val ENCHANTING: HTLookupRecipeViewerType<HTEnchantingRecipe.Input, HTEnchantingRecipe> =
        create(RagiumRecipeLookups.ENCHANTING, RagiumBlocks.ENCHANTER, 18 * 8)
}
