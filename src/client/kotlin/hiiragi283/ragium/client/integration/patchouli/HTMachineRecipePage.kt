package hiiragi283.ragium.client.integration.patchouli

import com.mojang.blaze3d.systems.RenderSystem
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.patchouli.client.book.gui.GuiBook
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry

class HTMachineRecipePage : PageDoubleRecipeRegistry<HTMachineRecipe>(RagiumRecipeTypes.MACHINE) {
    override fun drawRecipe(
        graphics: DrawContext,
        recipe: HTMachineRecipe,
        recipeX: Int,
        recipeY: Int,
        mouseX: Int,
        mouseY: Int,
        second: Boolean,
    ) {
        RenderSystem.enableBlend()
        // background tex
        graphics.drawTexture(book.craftingTexture, recipeX - 2, recipeY - 2, 0F, 0F, 100, 62, 128, 256)
        // header
        parent.drawCenteredStringNoShadow(
            graphics,
            getTitle(second).asOrderedText(),
            GuiBook.PAGE_WIDTH / 2,
            recipeY - 10,
            book.headerColor,
        )
        // catalyst
        parent.renderIngredient(graphics, recipeX + 79, recipeY + 22, mouseX, mouseY, recipe.catalyst)
        // input
        parent.renderIngredient(graphics, recipeX + 0 * 19 + 3, recipeY + 0 * 19 + 3, mouseX, mouseY, recipe.getInput(0))
        parent.renderIngredient(graphics, recipeX + 1 * 19 + 3, recipeY + 0 * 19 + 3, mouseX, mouseY, recipe.getInput(1))
        parent.renderIngredient(graphics, recipeX + 2 * 19 + 3, recipeY + 0 * 19 + 3, mouseX, mouseY, recipe.getInput(2))
        // output
        parent.renderResult(graphics, recipeX + 0 * 19 + 3, recipeY + 2 * 19 + 3, mouseX, mouseY, recipe.getOutput(0))
        parent.renderResult(graphics, recipeX + 1 * 19 + 3, recipeY + 2 * 19 + 3, mouseX, mouseY, recipe.getOutput(1))
        parent.renderResult(graphics, recipeX + 2 * 19 + 3, recipeY + 2 * 19 + 3, mouseX, mouseY, recipe.getOutput(2))
        // icon
        parent.renderItemStack(graphics, recipeX + 79, recipeY + 41, mouseX, mouseY, recipe.createIcon())
    }

    override fun getRecipeOutput(level: World?, recipe: HTMachineRecipe?): ItemStack {
        if (level == null || recipe == null) return ItemStack.EMPTY
        return recipe.getResult(level.registryManager)
    }

    override fun getRecipeHeight(): Int = 78
}
