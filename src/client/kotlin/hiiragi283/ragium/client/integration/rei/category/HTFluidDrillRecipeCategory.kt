package hiiragi283.ragium.client.integration.rei.category

import hiiragi283.ragium.client.integration.rei.RagiumREIClient
import hiiragi283.ragium.client.integration.rei.display.HTFluidDrillRecipeDisplay
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.item.HTFluidCubeItem
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.biome.Biome

object HTFluidDrillRecipeCategory : DisplayCategory<HTFluidDrillRecipeDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out HTFluidDrillRecipeDisplay> = RagiumREIClient.FLUID_DRILL

    override fun getTitle(): Text = Text.literal("Fluid Pump")

    override fun getIcon(): Renderer = EntryStacks.of(Items.BUCKET)

    override fun setupDisplay(display: HTFluidDrillRecipeDisplay, bounds: Rectangle): List<Widget> = buildList {
        val startPoint = Point(bounds.centerX - 41, bounds.centerY - 13)
        add(Widgets.createRecipeBase(bounds))
        add(Widgets.createArrow(Point(startPoint.x + 27, startPoint.y + 4)))
        add(Widgets.createResultSlotBackground(Point(startPoint.x + 61, startPoint.y + 5)))
        add(
            Widgets
                .createSlot(Point(startPoint.x + 61, startPoint.y + 5))
                .entries(display.outputEntries[0])
                .disableBackground()
                .markOutput(),
        )
        add(
            Widgets
                .createSlot(Point(startPoint.x + 4, startPoint.y + 5))
                .entries(
                    listOf(
                        EntryStacks.of(RagiumContents.EMPTY_FLUID_CUBE),
                    ),
                ),
        )
    }

    //    Display    //

    class FluidDisplay(private val biomeKey: RegistryKey<Biome>, val item: HTFluidCubeItem) : Display {
        override fun getInputEntries(): List<EntryIngredient> = listOf()

        override fun getOutputEntries(): List<EntryIngredient> = listOf(
            EntryIngredient.of(
                EntryStacks.of(item).tooltip(
                    Text.literal("Found in ${biomeKey.value}").formatted(Formatting.LIGHT_PURPLE),
                ),
            ),
        )

        override fun getCategoryIdentifier(): CategoryIdentifier<*> = RagiumREIClient.FLUID_DRILL
    }
}
