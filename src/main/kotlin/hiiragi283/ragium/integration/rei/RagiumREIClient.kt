package hiiragi283.ragium.integration.rei

import hiiragi283.ragium.client.gui.HTMachineScreen
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.util.EntryStacks

object RagiumREIClient : REIClientPlugin {

    init {
        Ragium.log { info("REI Integration enabled!") }
    }

    @JvmField
    val IDENTIFIERS: Map<HTMachineType, CategoryIdentifier<HTRecipeDisplay>> =
        HTMachineType.entries.associateWith { CategoryIdentifier.of(it.id) }

    @JvmStatic
    fun getCategoryId(type: HTMachineType): CategoryIdentifier<HTRecipeDisplay> = IDENTIFIERS[type]!!

    @JvmStatic
    fun forEachId(action: (CategoryIdentifier<HTRecipeDisplay>) -> Unit) {
        IDENTIFIERS.values.forEach(action)
    }

    override fun registerCategories(registry: CategoryRegistry) {
        HTMachineType.entries.forEach { type: HTMachineType ->
            registry.add(HTRecipeCategory(type))
            registry.addWorkstations(getCategoryId(type), EntryStacks.of(type))
        }
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        HTMachineType.entries.forEach { type: HTMachineType ->
            registry.registerRecipeFiller(HTMachineRecipe::class.java, type, ::HTRecipeDisplay)
        }
    }

    override fun registerScreens(registry: ScreenRegistry) {
        forEachId { id: CategoryIdentifier<HTRecipeDisplay> ->
            registry.registerContainerClickArea(
                Rectangle(5 + 18 * 4, 5 + 18 * 1, 18, 18),
                HTMachineScreen::class.java,
                id
            )
        }
    }

    @Suppress("UnstableApiUsage")
    override fun registerTransferHandlers(registry: TransferHandlerRegistry) {
        forEachId { id: CategoryIdentifier<HTRecipeDisplay> ->
            registry.register(
                SimpleTransferHandler.create(
                    HTMachineScreenHandler::class.java,
                    id,
                    SimpleTransferHandler.IntRange(0, 3)
                )
            )
        }
    }

}