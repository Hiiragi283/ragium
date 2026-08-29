package hiiragi283.ragium.common.recipe.viewer

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.util.Either
import hiiragi283.core.support.recipe.viewer.HTSimpleRecipeViewerType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * @see hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
 */
data object RagiumRecipeViewerTypes {
    // Mechanical
    @JvmField
    val ALLOYING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER, 18 * 7)

    @JvmField
    val ASSEMBLING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.ASSEMBLING, RagiumBlocks.ASSEMBLER, 18 * 6)

    @JvmField
    val COMPRESSING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR, 18 * 4)

    @JvmField
    val CUTTING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE, 18 * 4, 18 * 2)

    @JvmField
    val SMELTING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.SMELTING, RagiumBlocks.ELECTRIC_FURNACE, 18 * 4)

    // Heat
    @JvmField
    val FREEZING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.FREEZING, RagiumBlocks.FREEZER, 18 * 6)

    @JvmField
    val IMPLODING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.IMPLODING, RagiumBlocks.INDUSTRIAL_TNT, 18 * 6)

    @JvmField
    val MELTING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER, 18 * 4)

    @JvmField
    val PYROLYZING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER, 18 * 8)

    @JvmField
    val REFINING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.REFINING, RagiumBlocks.REFINERY, 18 * 6, 18 * 3)

    // Chemical
    @JvmField
    val BATHING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.BATHING, RagiumBlocks.MIXER, 18 * 6)

    @JvmField
    val CHEMICAL_REACTING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.CHEMICAL_REACTING, RagiumBlocks.MIXER, 18 * 8, 18 * 3)

    @JvmField
    val MIXING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER, 18 * 8, 18 * 3)

    @JvmField
    val WASHING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.WASHING, RagiumBlocks.WASHER, 18 * 7, 18 * 3)

    // Bio
    @JvmField
    val PLANTING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER, 18 * 5, 18 * 3)

    // Electronics
    @JvmField
    val PRINTING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.PRINTING, RagiumBlocks.PRINTER, 18 * 6)

    // Arcane
    @JvmField
    val ENCHANTING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER, 18 * 8, 18 * 3)

    @JvmStatic
    private inline fun <reified T : Any> create(
        recipeType: HTIdLike.Translatable,
        iconItem: ItemLike,
        width: Int,
        height: Int = 18 * 1,
        builderAction: HTSimpleRecipeViewerType.Builder.() -> Unit = {},
    ): HTRecipeViewerType<T> = HTSimpleRecipeViewerType.create<T> {
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
        builderAction: HTSimpleRecipeViewerType.Builder.() -> Unit = {},
    ): HTHolderRecipeViewerType<RECIPE> = create<HTRecipeHolder<RECIPE>>(recipeType, iconItem, width, height, builderAction)
}
