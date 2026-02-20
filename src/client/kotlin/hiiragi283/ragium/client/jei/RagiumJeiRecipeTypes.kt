package hiiragi283.ragium.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.integration.jei.type.HTHolderJeiRecipeType
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTBendingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTDistillingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTLathingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.HTWiringRecipe
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
    ): HTHolderJeiRecipeType<INPUT, RECIPE> = HTHolderJeiRecipeType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    // Machine - Basic
    @JvmField
    val ALLOYING: HTHolderJeiRecipeType<HTShapelessRecipeInput, HTAlloyingRecipe> =
        processor(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val BENDING: HTHolderJeiRecipeType<SingleRecipeInput, HTBendingRecipe> =
        processor(RagiumRecipeTypes.BENDING, RagiumBlocks.BENDING_MACHINE)

    @JvmField
    val COMPRESSING: HTHolderJeiRecipeType<SingleRecipeInput, HTCompressingRecipe> =
        processor(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)

    @JvmField
    val CRUSHING: HTHolderJeiRecipeType<SingleRecipeInput, HTCrushingRecipe> =
        processor(RagiumRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTHolderJeiRecipeType<SingleRecipeInput, HTCuttingRecipe> =
        processor(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val LATHING: HTHolderJeiRecipeType<SingleRecipeInput, HTLathingRecipe> =
        processor(RagiumRecipeTypes.LATHING, RagiumBlocks.LATHE)

    @JvmField
    val PRESSING: HTHolderJeiRecipeType<HTShapelessRecipeInput, HTPressingRecipe> =
        processor(RagiumRecipeTypes.PRESSING, RagiumBlocks.FORMING_PRESS)

    @JvmField
    val WIRING: HTHolderJeiRecipeType<SingleRecipeInput, HTWiringRecipe> =
        processor(RagiumRecipeTypes.WIRING, RagiumBlocks.WIREMILL)

    // Machine - Heat
    @JvmField
    val DISTILLING: HTHolderJeiRecipeType<HTSingleFluidRecipeInput, HTDistillingRecipe> =
        processor(RagiumRecipeTypes.DISTILLING, Items.FIREWORK_STAR, height = 18 * 1)

    @JvmField
    val MELTING: HTHolderJeiRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        processor(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val PYROLYZING: HTHolderJeiRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        processor(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER)

    // Machine - Cool
    @JvmField
    val FREEZING: HTHolderJeiRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        processor(RagiumRecipeTypes.FREEZING, RagiumBlocks.FREEZER)

    // Machine - Chemical
    @JvmField
    val CANNING: HTHolderJeiRecipeType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> =
        processor(RagiumRecipeTypes.CANNING, RagiumBlocks.CANNING_MACHINE)

    @JvmField
    val MIXING: HTHolderJeiRecipeType<HTChemicalRecipeInput, HTMixingRecipe> =
        processor(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER)

    @JvmField
    val WASHING: HTHolderJeiRecipeType<HTItemAndFluidRecipeInput, HTWashingRecipe> =
        processor(RagiumRecipeTypes.WASHING, RagiumBlocks.WASHER)

    // Machine - Matter

    // Device
    @JvmField
    val ENCHANTING: HTHolderJeiRecipeType<HTEnchantingRecipe.Input, HTEnchantingRecipe> =
        processor(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)

    @JvmField
    val PLANTING: HTHolderJeiRecipeType<HTPlantingRecipe.Input, HTPlantingRecipe> =
        processor(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)
}
