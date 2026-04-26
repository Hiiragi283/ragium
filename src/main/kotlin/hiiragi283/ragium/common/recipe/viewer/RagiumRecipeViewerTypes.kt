package hiiragi283.ragium.common.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProcessingRecipeDisplay
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.util.Either
import hiiragi283.core.impl.recipe.viewer.HTRecipeViewerTypeImpl
import hiiragi283.ragium.common.recipe.HTMassFabricatingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * @see hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
 */
data object RagiumRecipeViewerTypes {
    // Machine - Basic
    @JvmField
    val ALLOYING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER, 18 * 7)

    @JvmField
    val ASSEMBLING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.ASSEMBLING, RagiumBlocks.ASSEMBLER, 18 * 6)

    @JvmField
    val CUTTING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE, 18 * 5, 18 * 2)

    @JvmField
    val PLANTING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER, 18 * 5, 18 * 3)

    // Machine - Advanced
    @JvmField
    val FREEZING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.FREEZING, RagiumBlocks.FREEZER, 18 * 6)

    @JvmField
    val IMPLODING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.IMPLODING, RagiumBlocks.INDUSTRIAL_TNT, 18 * 7, 18 * 2)

    @JvmField
    val MELTING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER, 18 * 4)

    @JvmField
    val PYROLYZING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER, 18 * 8)

    @JvmField
    val REFINING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.REFINING, RagiumBlocks.REFINERY, 18 * 8)

    @JvmField
    val WASHING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.WASHING, RagiumBlocks.WASHER, 18 * 7, 18 * 3)

    // Machine - Elite
    @JvmField
    val CHEMICAL_REACTING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.CHEMICAL_REACTING, RagiumBlocks.CHEMICAL_WASHER, 18 * 8, 18 * 3)

    @JvmField
    val CHEMICAL_WASHING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.CHEMICAL_WASHING, RagiumBlocks.CHEMICAL_WASHER, 18 * 8)

    @JvmField
    val MIXING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER, 18 * 6, 18 * 2)

    // Machine - Ultimate
    @JvmField
    val MASS_FABRICATING: HTHolderRecipeViewerType<HTMassFabricatingRecipe> =
        createHolder(RagiumRecipeLookups.MASS_FABRICATING, RagiumItems.RAGI_MATTER, 18 * 8)

    // Device - Ultimate
    @JvmField
    val ENCHANTING: HTRecipeViewerType<HTProcessingRecipeDisplay> =
        create(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER, 18 * 8, 18 * 3)

    @JvmStatic
    private inline fun <reified T : Any> create(
        recipeType: HTIdLike.Translatable,
        iconItem: ItemLike,
        width: Int,
        height: Int = 18 * 1,
        builderAction: HTRecipeViewerTypeImpl.Builder.() -> Unit = {},
    ): HTRecipeViewerType<T> = HTRecipeViewerTypeImpl.create<T> {
        id = recipeType
        title = recipeType
        val iconStack = ItemStack(iconItem)
        icon = Either.Right(iconStack)
        bounds = HTBounds(0, 0, width, height)
        workStations += iconStack
        builderAction()
    }

    @JvmStatic
    private inline fun <reified RECIPE : Any> createHolder(
        recipeType: HTIdLike.Translatable,
        iconItem: ItemLike,
        width: Int,
        height: Int = 18 * 1,
        builderAction: HTRecipeViewerTypeImpl.Builder.() -> Unit = {},
    ): HTHolderRecipeViewerType<RECIPE> = create<HTRecipeHolder<RECIPE>>(recipeType, iconItem, width, height, builderAction)
}
