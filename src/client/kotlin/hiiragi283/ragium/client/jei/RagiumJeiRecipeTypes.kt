package hiiragi283.ragium.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.integration.jei.HTJeiHolderRecipeType
import hiiragi283.core.api.integration.jei.HTJeiRecipeType
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTBendingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTLathingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike

object RagiumJeiRecipeTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> processor(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        icon: ItemLike,
        bounds: HTBounds = HTBounds(0, 0, 18 * 8, 18 * 3),
    ): HTJeiHolderRecipeType<RECIPE> = HTJeiRecipeType.createRecipe(recipeType, recipeType, ItemStack(icon), bounds)

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
}
