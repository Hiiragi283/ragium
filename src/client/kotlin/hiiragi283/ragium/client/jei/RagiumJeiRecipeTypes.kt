package hiiragi283.ragium.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTDistillingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
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
        recipeType: HTRecipeType<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int = 18 * 8,
        height: Int = 18 * 3,
    ): HTHolderRecipeViewerType<INPUT, RECIPE> = HTHolderRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    // Machine - Basic
    @JvmField
    val ALLOYING: HTHolderRecipeViewerType<HTShapelessRecipeInput, HTAlloyingRecipe> =
        processor(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val BENDING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToItemRecipe> =
        processor(RagiumRecipeTypes.BENDING, RagiumBlocks.BENDING_MACHINE)

    @JvmField
    val COMPRESSING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToItemRecipe> =
        processor(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)

    @JvmField
    val CRUSHING: HTHolderRecipeViewerType<SingleRecipeInput, HTCrushingRecipe> =
        processor(RagiumRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTHolderRecipeViewerType<SingleRecipeInput, HTCuttingRecipe> =
        processor(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val LATHING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToItemRecipe> =
        processor(RagiumRecipeTypes.LATHING, RagiumBlocks.LATHE)

    @JvmField
    val PRINTING: HTHolderRecipeViewerType<HTDoubleRecipeInput, HTItemAndItemRecipe> =
        processor(RagiumRecipeTypes.PRINTING, Items.WRITABLE_BOOK)

    @JvmField
    val PRESSING: HTHolderRecipeViewerType<HTShapelessRecipeInput, HTPressingRecipe> =
        processor(RagiumRecipeTypes.PRESSING, RagiumBlocks.FORMING_PRESS)

    @JvmField
    val WIRING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToItemRecipe> =
        processor(RagiumRecipeTypes.WIRING, RagiumBlocks.WIREMILL)

    // Machine - Heat
    @JvmField
    val DISTILLING: HTHolderRecipeViewerType<HTSingleFluidRecipeInput, HTDistillingRecipe> =
        processor(RagiumRecipeTypes.DISTILLING, Items.FIREWORK_STAR, height = 18 * 1)

    @JvmField
    val MELTING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        processor(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val PYROLYZING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        processor(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER)

    // Machine - Cool
    @JvmField
    val FREEZING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        processor(RagiumRecipeTypes.FREEZING, RagiumBlocks.FREEZER)

    // Machine - Chemical
    @JvmField
    val CANNING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        processor(RagiumRecipeTypes.CANNING, RagiumBlocks.CANNING_MACHINE)

    @JvmField
    val MIXING: HTHolderRecipeViewerType<HTChemicalRecipeInput, HTMixingRecipe> =
        processor(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER)

    @JvmField
    val WASHING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTWashingRecipe> =
        processor(RagiumRecipeTypes.WASHING, RagiumBlocks.WASHER)

    // Machine - Matter

    // Device
    @JvmField
    val ENCHANTING: HTHolderRecipeViewerType<HTEnchantingRecipe.Input, HTEnchantingRecipe> =
        processor(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)

    @JvmField
    val PLANTING: HTHolderRecipeViewerType<HTPlantingRecipe.Input, HTPlantingRecipe> =
        processor(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)
}
