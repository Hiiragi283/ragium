package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.createEnchBook
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTEnchanterRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

class HTEnchanterRecipeCategory(val guiHelper: IGuiHelper) : HTRecipeCategory<HTEnchanterRecipe> {
    override fun getRecipeType(): RecipeType<HTEnchanterRecipe> = RagiumJEIRecipeTypes.ENCHANTER

    override fun getTitle(): Component = HTMachineType.ARCANE_ENCHANTER.text

    override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(HTMachineType.ARCANE_ENCHANTER)

    @Suppress("DEPRECATION")
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTEnchanterRecipe, focuses: IFocusGroup) {
        // Target Item
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addItemStacks(
                recipe.enchantment
                    .value()
                    .supportedItems
                    .map(::ItemStack),
            )
        // First Item Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.firstInput)
        // Second Item Input
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.secondInput.getOrNull())
        // Item Output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(createEnchBook(recipe.enchantment))
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTEnchanterRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(3.5), getPosition(0))
    }

    override fun getWidth(): Int = 18 * 6 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTEnchanterRecipe> =
        HTEnchanterRecipe.CODEC.codec()
}
