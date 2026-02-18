package hiiragi283.ragium.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.integration.jei.HTJeiHolderRecipeType
import hiiragi283.core.api.integration.jei.HTJeiRecipeType
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTBendingRecipe
import hiiragi283.ragium.common.recipe.HTCanningRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTDistillingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTLathingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.HTWiringRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike

object RagiumJeiRecipeTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> processor(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int = 18 * 8,
        height: Int = 18 * 3,
    ): HTJeiHolderRecipeType<RECIPE> = HTJeiRecipeType.createRecipe(recipeType, recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    // Machine - Basic
    @JvmField
    val ALLOYING: HTJeiHolderRecipeType<HTAlloyingRecipe> =
        processor(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val BENDING: HTJeiHolderRecipeType<HTBendingRecipe> =
        processor(RagiumRecipeTypes.BENDING, RagiumBlocks.BENDING_MACHINE)

    @JvmField
    val COMPRESSING: HTJeiHolderRecipeType<HTCompressingRecipe> =
        processor(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)

    @JvmField
    val CRUSHING: HTJeiHolderRecipeType<HTCrushingRecipe> =
        processor(RagiumRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTJeiHolderRecipeType<HTCuttingRecipe> =
        processor(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val LATHING: HTJeiHolderRecipeType<HTLathingRecipe> =
        processor(RagiumRecipeTypes.LATHING, RagiumBlocks.LATHE)

    @JvmField
    val PRESSING: HTJeiHolderRecipeType<HTPressingRecipe> =
        processor(RagiumRecipeTypes.PRESSING, RagiumBlocks.FORMING_PRESS)

    @JvmField
    val WIRING: HTJeiHolderRecipeType<HTWiringRecipe> =
        processor(RagiumRecipeTypes.WIRING, RagiumBlocks.LATHE)

    // Machine - Heat
    @JvmField
    val DISTILLING: HTJeiHolderRecipeType<HTDistillingRecipe> =
        processor(RagiumRecipeTypes.DISTILLING, Items.FIREWORK_STAR, height = 18 * 1)

    @JvmField
    val MELTING: HTJeiHolderRecipeType<HTMeltingRecipe> =
        processor(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val PYROLYZING: HTJeiHolderRecipeType<HTPyrolyzingRecipe> =
        processor(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER)

    // Machine - Cool
    @JvmField
    val FREEZING: HTJeiHolderRecipeType<HTFreezingRecipe> =
        processor(RagiumRecipeTypes.FREEZING, RagiumBlocks.FREEZER)

    // Machine - Chemical
    @JvmField
    val CANNING: HTJeiHolderRecipeType<HTCanningRecipe> =
        processor(RagiumRecipeTypes.CANNING, Items.GLASS_BOTTLE)

    @JvmField
    val MIXING: HTJeiHolderRecipeType<HTMixingRecipe> =
        processor(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER)

    @JvmField
    val WASHING: HTJeiHolderRecipeType<HTWashingRecipe> =
        processor(RagiumRecipeTypes.WASHING, RagiumBlocks.WASHER)

    // Machine - Matter

    // Device
    @JvmField
    val ENCHANTING: HTJeiHolderRecipeType<HTEnchantingRecipe> =
        processor(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)

    @JvmField
    val PLANTING: HTJeiHolderRecipeType<HTPlantingRecipe> =
        processor(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)
}
