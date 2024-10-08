package hiiragi283.ragium.client.integration.rei.category

import hiiragi283.ragium.api.machine.HTMachineBlockRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.client.integration.rei.categoryId
import hiiragi283.ragium.client.integration.rei.display.HTMachineRecipeDisplay
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Tooltip
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryIngredients
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
class HTMachineRecipeCategory(private val type: HTMachineType) : HTDisplayCategory<HTMachineRecipeDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out HTMachineRecipeDisplay> = type.categoryId

    override fun getTitle(): Text = type.text

    override fun getIcon(): Renderer = HTMachineBlockRegistry.get(type, HTMachineTier.BASIC)?.let { EntryStacks.of(it) }
        ?: EntryStacks.of(RagiumContents.Hulls.RAGI_ALLOY)

    override fun setupDisplay(display: HTMachineRecipeDisplay, bounds: Rectangle): List<Widget> = buildList {
        this += Widgets.createRecipeBase(bounds)
        this += Widgets.createArrow(getPoint(bounds, 3.25, 0.0)).animationDurationTicks(200.0)
        // inputs
        this +=
            Widgets
                .createSlot(getPoint(bounds, 0, 0))
                .entries(display.inputEntries.getOrNull(0) ?: listOf())
                .markInput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 1, 0))
                .entries(display.inputEntries.getOrNull(1) ?: listOf())
                .markInput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 2, 0))
                .entries(display.inputEntries.getOrNull(2) ?: listOf())
                .markInput()
        // catalyst
        this +=
            Widgets
                .createSlot(getPoint(bounds, 3.5, 1.0))
                .entries(EntryIngredients.ofIngredient(display.catalyst))
                .markInput()
        // outputs
        this +=
            Widgets
                .createSlot(getPoint(bounds, 5, 0))
                .entries(display.outputEntries.getOrNull(0) ?: listOf())
                .markOutput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 6, 0))
                .entries(display.outputEntries.getOrNull(1) ?: listOf())
                .markOutput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 7, 0))
                .entries(display.outputEntries.getOrNull(2) ?: listOf())
                .markOutput()
        // info
        this +=
            Widgets
                .createSlot(getPoint(bounds, 7, 1))
                .entries(listOf(createInfoEntry(display.recipe)))
                .markOutput()
    }

    private fun createInfoEntry(recipe: HTMachineRecipe): EntryStack<*> {
        val entry: RegistryEntry<Item> = Registries.ITEM.getEntry(Items.WRITABLE_BOOK)
        val components: ComponentChanges = ComponentChanges
            .builder()
            .add(
                DataComponentTypes.ITEM_NAME,
                Text.translatable(RagiumTranslationKeys.REI_RECIPE_INFO).formatted(Formatting.LIGHT_PURPLE),
            ).build()
        val stack = ItemStack(entry, 1, components)
        val tier: HTMachineTier = recipe.minTier
        return EntryStacks.of(stack).tooltipProcessor { _: EntryStack<ItemStack>, tooltip: Tooltip ->
            tooltip.add(tier.tierText)
            tooltip.add(tier.recipeCostText)
            if (recipe.requireScan) {
                tooltip.add(
                    Text.translatable(RagiumTranslationKeys.REI_RECIPE_REQUIRE_SCAN).formatted(Formatting.RED),
                )
            }
            tooltip
        }
    }

    override fun getDisplayHeight(): Int = 18 * 2 + 8

    override fun getDisplayWidth(display: HTMachineRecipeDisplay): Int = 18 * 8 + 8
}
