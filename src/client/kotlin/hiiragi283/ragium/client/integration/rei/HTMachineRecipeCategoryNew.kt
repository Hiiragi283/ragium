package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeNew
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Slot
import me.shedaniel.rei.api.client.gui.widgets.Tooltip
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryStacks
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.component.ComponentChanges
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.util.Formatting

@Environment(EnvType.CLIENT)
class HTMachineRecipeCategoryNew(private val type: HTMachineType) : DisplayCategory<HTMachineRecipeDisplayNew> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out HTMachineRecipeDisplayNew> = type.categoryIdNew

    override fun getTitle(): Text = type.text

    override fun getIcon(): Renderer = type.createEntryStack(HTMachineTier.PRIMITIVE)

    override fun setupDisplay(display: HTMachineRecipeDisplayNew, bounds: Rectangle): List<Widget> = buildList {
        this += Widgets.createRecipeBase(bounds)
        this += Widgets.createArrow(getPoint(bounds, 3.25, 0.0)).animationDurationTicks(200.0)
        this.addAll(
            when (display) {
                is HTMachineRecipeDisplayNew.Simple -> setupSimple(display, bounds)
                is HTMachineRecipeDisplayNew.Large -> setupLarge(display, bounds)
            },
        )
        // catalyst
        this += createSlot(bounds, 3.5, 1.0, display.catalyst).markInput()
        // info
        this += createInfoSlot(bounds, 7, 1, display).markOutput()
    }

    private fun setupSimple(display: HTMachineRecipeDisplayNew, bounds: Rectangle): List<Widget> = buildList {
        // inputs
        this += createSlot(bounds, 1, 0, display.inputEntries.getOrNull(0)).markInput()
        this += createSlot(bounds, 2, 0, display.inputEntries.getOrNull(1)).markInput()
        this += createSlot(bounds, 2, 1, display.inputEntries.getOrNull(2)).markInput()
        // outputs
        this += createSlot(bounds, 5, 0, display.outputEntries.getOrNull(0)).markOutput()
        this += createSlot(bounds, 6, 0, display.outputEntries.getOrNull(1)).markOutput()
        this += createSlot(bounds, 5, 1, display.outputEntries.getOrNull(2)).markOutput()
    }

    private fun setupLarge(display: HTMachineRecipeDisplayNew, bounds: Rectangle): List<Widget> = buildList {
        // inputs
        this += createSlot(bounds, 0, 0, display.inputEntries.getOrNull(0)).markInput()
        this += createSlot(bounds, 1, 0, display.inputEntries.getOrNull(1)).markInput()
        this += createSlot(bounds, 2, 0, display.inputEntries.getOrNull(2)).markInput()
        this += createSlot(bounds, 1, 1, display.inputEntries.getOrNull(3)).markInput()
        this += createSlot(bounds, 2, 1, display.inputEntries.getOrNull(4)).markInput()
        // outputs
        this += createSlot(bounds, 5, 0, display.outputEntries.getOrNull(0)).markOutput()
        this += createSlot(bounds, 6, 0, display.outputEntries.getOrNull(1)).markOutput()
        this += createSlot(bounds, 7, 0, display.outputEntries.getOrNull(2)).markOutput()
        this += createSlot(bounds, 5, 1, display.outputEntries.getOrNull(3)).markOutput()
        this += createSlot(bounds, 6, 1, display.outputEntries.getOrNull(4)).markOutput()
    }

    override fun getDisplayHeight(): Int = 18 * 2 + 8

    override fun getDisplayWidth(display: HTMachineRecipeDisplayNew): Int = 18 * 8 + 8

    //    Utils    //

    fun getPoint(bounds: Rectangle, x: Int, y: Int): Point = Point(bounds.x + 5 + x * 18, bounds.y + 5 + y * 18)

    fun getPoint(bounds: Rectangle, x: Double, y: Double): Point = Point(bounds.x + 5 + x * 18, bounds.y + 5 + y * 18)

    fun createSlot(
        bounds: Rectangle,
        x: Int,
        y: Int,
        ingredient: Collection<EntryStack<*>>?,
    ): Slot = Widgets
        .createSlot(getPoint(bounds, x, y))
        .entries(ingredient ?: listOf())

    fun createSlot(
        bounds: Rectangle,
        x: Double,
        y: Double,
        ingredient: Collection<EntryStack<*>>?,
    ): Slot = Widgets
        .createSlot(getPoint(bounds, x, y))
        .entries(ingredient ?: listOf())

    fun createInfoSlot(
        bounds: Rectangle,
        x: Int,
        y: Int,
        display: HTMachineRecipeDisplayNew,
    ): Slot = Widgets
        .createSlot(getPoint(bounds, x, y))
        .entries(listOf(createInfoEntry(display.recipe)))

    protected fun createInfoEntry(recipe: HTMachineRecipeNew<*>): EntryStack<*> {
        val entry: RegistryEntry<Item> = Registries.ITEM.getEntry(Items.WRITABLE_BOOK)
        val components: ComponentChanges = ComponentChanges
            .builder()
            .add(
                DataComponentTypes.ITEM_NAME,
                Text.translatable(RagiumTranslationKeys.REI_RECIPE_INFO).formatted(Formatting.LIGHT_PURPLE),
            ).build()
        val stack = ItemStack(entry, 1, components)
        val tier: HTMachineTier = recipe.tier
        return EntryStacks.of(stack).tooltipProcessor { _: EntryStack<ItemStack>, tooltip: Tooltip ->
            tooltip.add(tier.tierText)
            tooltip.add(tier.recipeCostText)
            tooltip
        }
    }
}
