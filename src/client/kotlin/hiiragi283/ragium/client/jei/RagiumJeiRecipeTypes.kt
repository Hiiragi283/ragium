package hiiragi283.ragium.client.jei

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.recipe.viewer.HTFakeRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.ragium.api.data.tank.HTTankInteraction
import hiiragi283.ragium.api.recipe.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTElectrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RagiumDuplicatingRecipe
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.ItemLike

object RagiumJeiRecipeTypes {
    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
        recipeType: HTRecipeType.Managed<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTHolderRecipeViewerType<INPUT, RECIPE> = HTHolderRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(
        recipeType: HTRecipeType.Fake<INPUT, RECIPE>,
        icon: ItemLike,
        width: Int,
        height: Int = 18 * 1,
    ): HTFakeRecipeViewerType<INPUT, RECIPE> = HTFakeRecipeViewerType.create(recipeType, ItemStack(icon), HTBounds(0, 0, width, height))

    @JvmField
    val TANK_INTERACTION: HTFakeRecipeViewerType<RecipeInput, HTTankInteraction> =
        create(RagiumRecipeTypes.TANK_INTERACTION, RagiumBlocks.TANK, 18 * 5, 18 * 3)

    // Machine - Basic
    @JvmField
    val ALLOYING: HTHolderRecipeViewerType<HTShapelessRecipeInput, HTAlloyingRecipe> =
        create(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER, 18 * 6)

    @JvmField
    val ASSEMBLING: HTHolderRecipeViewerType<HTShapelessRecipeInput, HTAssemblingRecipe> =
        create(RagiumRecipeTypes.ASSEMBLING, RagiumBlocks.ALLOY_SMELTER, 18 * 5)

    @JvmField
    val COMPRESSING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToItemRecipe.Serializable> =
        create(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR, 18 * 4)

    @JvmField
    val CUTTING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> =
        create(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE, 18 * 6)

    @JvmField
    val PLANTING: HTHolderRecipeViewerType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> =
        create(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER, 18 * 6)

    // Machine - Heat
    @JvmField
    val FREEZING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTFreezingRecipe> =
        create(RagiumRecipeTypes.FREEZING, RagiumBlocks.FREEZER, 18 * 6)

    @JvmField
    val MELTING: HTHolderRecipeViewerType<SingleRecipeInput, HTMeltingRecipe> =
        create(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER, 18 * 4)

    @JvmField
    val PYROLYZING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        create(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER, 18 * 8)

    @JvmField
    val REFINING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe.Serializable> =
        create(RagiumRecipeTypes.REFINING, RagiumBlocks.REFINERY, 18 * 8)

    // Machine - Elite
    @JvmField
    val ELECTROLYZING: HTHolderRecipeViewerType<HTSingleFluidRecipeInput, HTElectrolyzingRecipe> =
        create(RagiumRecipeTypes.ELECTROLYZING, RagiumBlocks.MIXER, 18 * 7)

    @JvmField
    val MIXING: HTHolderRecipeViewerType<HTChemicalRecipeInput, HTMixingRecipe> =
        create(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER, 18 * 9)

    @JvmField
    val WASHING: HTHolderRecipeViewerType<HTItemAndFluidRecipeInput, HTWashingRecipe> =
        create(RagiumRecipeTypes.WASHING, RagiumBlocks.WASHER, 18 * 8)

    // Machine - Ultimate
    @JvmField
    val DUPLICATING: HTFakeRecipeViewerType<HTItemAndFluidRecipeInput, RagiumDuplicatingRecipe> =
        create(RagiumRecipeTypes.DUPLICATING, RagiumBlocks.TANK, 18 * 6)

    @JvmField
    val ENCHANTING: HTHolderRecipeViewerType<HTEnchantingRecipe.Input, HTEnchantingRecipe> =
        create(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER, 18 * 8)
}
