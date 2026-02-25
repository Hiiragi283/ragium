package hiiragi283.ragium.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.ragium.api.recipe.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.HTItemAndFluidToChancedRecipe
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike

object RagiumJeiRecipeTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> processor(
        recipeType: HTRecipeType.Managed<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int = 18 * 8,
        height: Int = 18 * 3,
    ): HTHolderRecipeViewerType<INPUT, RECIPE> = HTHolderRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    // Machine - Basic
    @JvmField
    val ALLOYING: HTHolderRecipeViewerType<HTShapelessRecipeInput, HTAlloyingRecipe> =
        processor(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val COMPRESSING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToItemRecipe.Serializable> =
        processor(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR, 18 * 4, 18 * 1)

    @JvmField
    val CUTTING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> =
        processor(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE, 18 * 5, 18 * 1)

    @JvmField
    val PRINTING: HTHolderRecipeViewerType<HTDoubleRecipeInput, HTItemAndItemRecipe.Serializable> =
        processor(RagiumRecipeTypes.PRINTING, Items.WRITABLE_BOOK, 18 * 6, 18 * 1)

    @JvmField
    val PRESSING: HTHolderRecipeViewerType<HTDoubleRecipeInput, HTItemAndItemRecipe.Serializable> =
        processor(RagiumRecipeTypes.PRESSING, RagiumBlocks.FORMING_PRESS, 18 * 6, 18 * 1)

    @JvmField
    val WIRING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToItemRecipe.Serializable> =
        processor(RagiumRecipeTypes.WIRING, RagiumBlocks.WIREMILL, 18 * 4, 18 * 1)

    // Machine - Heat
    @JvmField
    val MELTING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        processor(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val PYROLYZING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        processor(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER)

    @JvmField
    val REFINING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        processor(RagiumRecipeTypes.REFINING, Items.FIREWORK_STAR)

    // Machine - Cool
    @JvmField
    val FREEZING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        processor(RagiumRecipeTypes.FREEZING, RagiumBlocks.FREEZER)

    // Machine - Chemical
    @JvmField
    val CANNING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        processor(RagiumRecipeTypes.CANNING, RagiumBlocks.CANNING_MACHINE)

    @JvmField
    val MIXING: HTHolderRecipeViewerType<HTChemicalRecipeInput, HTMixingRecipe> =
        processor(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER)

    @JvmField
    val WASHING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemAndFluidToChancedRecipe.Serializable> =
        processor(RagiumRecipeTypes.WASHING, RagiumBlocks.WASHER)

    // Machine - Matter

    // Device
    @JvmField
    val ENCHANTING: HTHolderRecipeViewerType<HTEnchantingRecipe.Input, HTEnchantingRecipe.Serializable> =
        processor(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)

    @JvmField
    val PLANTING: HTHolderRecipeViewerType<HTPlantingRecipe.Input, HTPlantingRecipe> =
        processor(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)
}
