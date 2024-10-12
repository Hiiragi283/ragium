package hiiragi283.ragium.client.integration.patchouli

import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.WeightedIngredient
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack
import vazkii.patchouli.client.book.gui.GuiBookEntry

//    GuiBookEntry    //

fun GuiBookEntry.renderIngredient(
    context: DrawContext,
    x: Int,
    y: Int,
    mouseX: Int,
    mouseY: Int,
    ingredient: WeightedIngredient?,
) {
    val stacks: Array<out ItemStack> = ingredient?.matchingStacks ?: return
    if (stacks.isNotEmpty()) {
        renderItemStack(context, x, y, mouseX, mouseY, stacks[(ticksInBook / 20) % stacks.size])
    }
}

fun GuiBookEntry.renderResult(
    context: DrawContext,
    x: Int,
    y: Int,
    mouseX: Int,
    mouseY: Int,
    result: HTRecipeResult?,
) {
    result?.toStack()?.let {
        renderItemStack(context, x, y, mouseX, mouseY, it)
    }
}
