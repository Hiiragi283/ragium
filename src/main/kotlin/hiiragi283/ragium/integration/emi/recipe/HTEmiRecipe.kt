package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.resources.ResourceLocation

interface HTEmiRecipe : EmiRecipe {
    fun getPosition(index: Int): Int = index * 18

    fun getPosition(index: Double): Int = (index * 18).toInt()

    fun WidgetHolder.addOutput(
        result: EmiIngredient?,
        x: Int,
        y: Int,
        large: Boolean = false,
        drawBack: Boolean = false,
    ): SlotWidget = when {
        large -> addSlot(result ?: EmiStack.EMPTY, x - 4, y - 4).large(true)
        else -> addSlot(result ?: EmiStack.EMPTY, x, y)
    }.recipeContext(this@HTEmiRecipe).drawBack(drawBack)

    abstract class Impl(private val id: ResourceLocation) : HTEmiRecipe {
        final override fun getId(): ResourceLocation = id
    }
}
