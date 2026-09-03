package hiiragi283.ragium.client.integration.jei

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.bounds
import hiiragi283.lib.gui.toRec2i
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.recipe.widget.HTGhostWidget
import hiiragi283.lib.recipe.widget.HTIngredientWidget
import hiiragi283.ragium.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.ragium.client.gui.widget.HTGuiWidget
import mezz.jei.api.gui.builder.IClickableIngredientFactory
import mezz.jei.api.gui.handlers.IGhostIngredientHandler
import mezz.jei.api.gui.handlers.IGuiContainerHandler
import mezz.jei.api.ingredients.ITypedIngredient
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.runtime.IClickableIngredient
import net.minecraft.client.renderer.Rect2i
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

data object HTWidgetContainerJeiHandler :
    IGuiContainerHandler<HTWidgetContainerScreen>,
    IGhostIngredientHandler<HTWidgetContainerScreen> {
    @JvmStatic
    private fun getWidgets(screen: HTWidgetContainerScreen): Sequence<Pair<HTBounds, HTWidget>> = screen
        .children()
        .asSequence()
        .filterIsInstance<HTGuiWidget<*>>()
        .map { it.bounds to it.widget }

    override fun getClickableIngredientUnderMouse(
        builder: IClickableIngredientFactory,
        containerScreen: HTWidgetContainerScreen,
        mouseX: Double,
        mouseY: Double
    ): Optional<out IClickableIngredient<*>> = getWidgets(containerScreen)
        .filter { (bounds: HTBounds, _) -> bounds.contains(mouseX.toInt(), mouseY.toInt()) }
        .mapNotNull { (bounds: HTBounds, widget: HTWidget) ->
            when (val ingredient = (widget as? HTIngredientWidget)?.getIngredient()) {
                is FluidStack if !ingredient.isEmpty -> builder.createBuilder(NeoForgeTypes.FLUID_STACK, ingredient)
                is ItemStack if !ingredient.isEmpty -> builder.createBuilder(ingredient)
                else -> return@mapNotNull null
            }.buildWithArea(bounds.toRec2i())
        }.firstOrNull()
        ?: Optional.empty()

    /*override fun getGuiClickableAreas(containerScreen: HTWidgetContainerScreen, guiMouseX: Double, guiMouseY: Double): Collection<IGuiClickableArea> {
        val guiMouseX: Double = guiMouseX + containerScreen.leftPos
        val guiMouseY: Double = guiMouseY + containerScreen.topPos
        return getGuiClickableAreas(containerScreen.children(), guiMouseX, guiMouseY)
    }

    @JvmStatic
    private fun getGuiClickableAreas(children: Iterable<GuiEventListener>, guiMouseX: Double, guiMouseY: Double): Collection<IGuiClickableArea> {
        for (listener: GuiEventListener in children) {
            if (listener is ContainerEventHandler) {
                val childrenIn: List<GuiEventListener> = listener.children()
                if (!childrenIn.isEmpty()) {
                    return getGuiClickableAreas(childrenIn, guiMouseX, guiMouseY)
                }
            }
            if (listener is HTGuiWidget<*>) {
                val widget: HTWidget = listener.widget
                val bounds: HTBounds = listener.bounds
                if (widget is HTRecipeAreaWidget<*>) {
                    HiiragiCoreAPI.LOGGER.debug("Widget Bounds: {}", bounds)
                    HiiragiCoreAPI.LOGGER.debug("Mouse X: {}, Y: {}", guiMouseX, guiMouseY)
                    if (bounds.contains(guiMouseX.toInt(), guiMouseY.toInt())) {
                        HiiragiCoreAPI.LOGGER.debug("Found Widget!")
                        return object : IGuiClickableArea {
                            override fun getArea(): Rect2i = bounds.toRec2i()

                            override fun onClick(focusFactory: IFocusFactory, recipesGui: IRecipesGui) {
                                recipesGui.showTypes(widget.getSupportedRecipeTypes().map { HTJeiPlugin.getRecipeType(it) })
                            }
                        }.let(::listOf)
                    }
                }
            }
        }
        return emptyList()
    }*/

    //    IGhostIngredientHandler    //

    override fun <I : Any> getTargetsTyped(
        gui: HTWidgetContainerScreen,
        ingredient: ITypedIngredient<I>,
        doStart: Boolean
    ): List<IGhostIngredientHandler.Target<I>> = getWidgets(gui)
        .mapNotNull { (bounds: HTBounds, widget: HTWidget) ->
            val consumer: HTGhostWidget.GhostIngredientConsumer = (widget as? HTGhostWidget)
                ?.getGhostConsumer()
                ?: return@mapNotNull null
            bounds to consumer
        }.filter { (_, consumer: HTGhostWidget.GhostIngredientConsumer) ->
            consumer.supportedTarget(ingredient.ingredient)?.let(ingredient.type::getCastIngredient) != null
        }.map { (bounds: HTBounds, consumer: HTGhostWidget.GhostIngredientConsumer) ->
            object : IGhostIngredientHandler.Target<I> {
                override fun getArea(): Rect2i = bounds.toRec2i()

                override fun accept(ingredient: I) {
                    consumer.accept(ingredient)
                }
            }
        }.toList()

    override fun onComplete(): Unit = Unit
}
