package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.recipe.HTSingleItemRecipe
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
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class HTSingleItemRecipeCategory<T : HTSingleItemRecipe>(
    private val guiHelper: IGuiHelper,
    private val recipeType: RecipeType<T>,
    private val machine: HTMachineKey,
    private val serializer: RecipeSerializer<T>,
) : HTRecipeCategory<T> {
    override fun getRecipeType(): RecipeType<T> = recipeType

    override fun getTitle(): Component = machine.text

    override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(machine.getBlock())

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: T, focuses: IFocusGroup) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.input)
        // Catalyst
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.catalyst.orElse(Ingredient.of()))
        // Item Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.getItemOutput())
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: T, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(2.5), getPosition(0))
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<T> = serializer.codec().codec()
}
