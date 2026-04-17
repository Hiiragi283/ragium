package hiiragi283.ragium.common.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTSimpleRecipeViewerType
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTChemicalWashingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTElectrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * @see hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
 */
object RagiumRecipeViewerTypes {
    @JvmStatic
    private fun <BASE : Any, RECIPE : BASE> lookup(
        recipeType: HTRecipeType<*, BASE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTLookupRecipeViewerType<BASE, RECIPE> = HTLookupRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    @JvmStatic
    private inline fun <reified RECIPE : Any> simple(
        recipeType: HTRecipeType<*, RECIPE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTHolderRecipeViewerType<RECIPE> =
        HTSimpleRecipeViewerType.create<HTRecipeHolder<RECIPE>>(recipeType, recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    // Machine - Basic
    @JvmField
    val ALLOYING: HTHolderRecipeViewerType<HTAlloyingRecipe> =
        simple(RagiumRecipeLookups.ALLOYING, RagiumBlocks.ALLOY_SMELTER, 18 * 6)

    @JvmField
    val ASSEMBLING: HTHolderRecipeViewerType<HTAssemblingRecipe> =
        simple(RagiumRecipeLookups.ASSEMBLING, RagiumBlocks.ASSEMBLER, 18 * 5)

    @JvmField
    val CUTTING: HTLookupRecipeViewerType<HTSingleMultiOutputRecipe, HTCuttingRecipe> =
        lookup(RagiumRecipeLookups.CUTTING, RagiumBlocks.CUTTING_MACHINE, 18 * 5, 18 * 2)

    @JvmField
    val PLANTING: HTLookupRecipeViewerType<HTDoubleMultiOutputRecipe, HTPlantingRecipe> =
        lookup(RagiumRecipeLookups.PLANTING, RagiumBlocks.PLANTER, 18 * 6, 18 * 3)

    // Machine - Heat
    @JvmField
    val FREEZING: HTHolderRecipeViewerType<HTFreezingRecipe> =
        simple(RagiumRecipeLookups.FREEZING, RagiumBlocks.FREEZER, 18 * 6)

    @JvmField
    val MELTING: HTHolderRecipeViewerType<HTMeltingRecipe> =
        simple(RagiumRecipeLookups.MELTING, RagiumBlocks.MELTER, 18 * 4)

    @JvmField
    val PYROLYZING: HTLookupRecipeViewerType<HTItemOrFluidRecipe, HTPyrolyzingRecipe> =
        lookup(RagiumRecipeLookups.PYROLYZING, RagiumBlocks.PYROLYZER, 18 * 8)

    @JvmField
    val REFINING: HTLookupRecipeViewerType<HTItemOrFluidRecipe, HTRefiningRecipe> =
        lookup(RagiumRecipeLookups.REFINING, RagiumBlocks.REFINERY, 18 * 8)

    // Machine - Elite
    @JvmField
    val CHEMICAL_WASHING: HTLookupRecipeViewerType<HTItemOrFluidRecipe, HTChemicalWashingRecipe> =
        lookup(RagiumRecipeLookups.CHEMICAL_WASHING, RagiumBlocks.CHEMICAL_WASHER, 18 * 8)

    @JvmField
    val ELECTROLYZING: HTHolderRecipeViewerType<HTElectrolyzingRecipe> =
        simple(RagiumRecipeLookups.ELECTROLYZING, RagiumBlocks.FLUID_MIXER, 18 * 7)

    @JvmField
    val MIXING: HTLookupRecipeViewerType<HTMixingRecipe, HTViewerMixingRecipe> =
        lookup(RagiumRecipeLookups.MIXING, RagiumBlocks.MIXER, 18 * 6, 18 * 2)

    @JvmField
    val WASHING: HTHolderRecipeViewerType<HTWashingRecipe> =
        simple(RagiumRecipeLookups.WASHING, RagiumBlocks.WASHER, 18 * 7, 18 * 3)

    // Machine - Ultimate
    @JvmField
    val ENCHANTING: HTHolderRecipeViewerType<HTEnchantingRecipe> =
        simple(RagiumRecipeLookups.ENCHANTING, RagiumBlocks.ENCHANTER, 18 * 8)
}
