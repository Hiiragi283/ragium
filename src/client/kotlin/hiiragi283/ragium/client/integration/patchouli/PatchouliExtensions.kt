package hiiragi283.ragium.client.integration.patchouli

import hiiragi283.ragium.api.extension.itemStack
import hiiragi283.ragium.api.extension.matchingStacks
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
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
    ingredient: HTItemIngredient?,
) {
    val stacks: List<ItemStack> = ingredient?.matchingStacks ?: return
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
    result: HTItemResult?,
) {
    result?.resourceAmount?.itemStack?.let {
        renderItemStack(context, x, y, mouseX, mouseY, it)
    }
}
