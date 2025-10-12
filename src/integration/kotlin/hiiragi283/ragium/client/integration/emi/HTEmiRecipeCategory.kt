package hiiragi283.ragium.client.integration.emi

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiRecipeSorting
import dev.emi.emi.api.render.EmiRenderable
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.client.integration.emi.type.HTRecipeViewerType
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

/**
 * @see [mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory]
 */
class HTEmiRecipeCategory private constructor(
    val bounds: HTBounds,
    val viewerType: HTRecipeViewerType<*>,
    icon: EmiRenderable,
    simplified: EmiRenderable,
    sorter: Comparator<EmiRecipe>,
) : EmiRecipeCategory(viewerType.getId(), icon, simplified, sorter) {
    companion object {
        fun create(viewerType: HTRecipeViewerType<*>): HTEmiRecipeCategory {
            val iconStack: ItemStack = viewerType.iconStack
            val icon: ResourceLocation? = viewerType.icon
            if (iconStack.isEmpty) {
                return HTEmiRecipeCategory(
                    viewerType,
                    checkNotNull(icon) {
                        "Expected recipe type to have either an icon stack or an icon location"
                    }.let(::renderIcon),
                )
            }
            return when (icon) {
                null -> HTEmiRecipeCategory(viewerType, EmiStack.of(iconStack))
                else -> HTEmiRecipeCategory(viewerType, EmiStack.of(iconStack), renderIcon(icon))
            }
        }

        @JvmStatic
        private fun renderIcon(icon: ResourceLocation): EmiRenderable = EmiRenderable { graphics: GuiGraphics, x: Int, y: Int, _ ->
            graphics.blit(icon, x - 1, y - 1, 0f, 0f, 18, 18, 18, 18)
        }
    }

    private constructor(
        viewerType: HTRecipeViewerType<*>,
        icon: EmiRenderable,
        simplified: EmiRenderable = icon,
    ) : this(viewerType.getBounds(), viewerType, icon, simplified, EmiRecipeSorting.compareOutputThenInput())

    override fun getName(): Component = viewerType.getText()
}
