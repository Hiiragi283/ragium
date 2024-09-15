package hiiragi283.ragium.integration.rei

import hiiragi283.ragium.client.gui.HTMachineScreen
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumBlocks
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
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object RagiumREIClient : REIClientPlugin {
    init {
        Ragium.log { info("REI Integration enabled!") }
    }

    @JvmField
    val IDENTIFIERS: Map<HTMachineType, CategoryIdentifier<HTMachineRecipeDisplay>> =
        HTMachineType.getEntries().associateWith { CategoryIdentifier.of(it.id) }

    @JvmStatic
    fun getCategoryId(type: HTMachineType): CategoryIdentifier<HTMachineRecipeDisplay> = IDENTIFIERS[type]!!

    @JvmStatic
    fun forEachId(action: (CategoryIdentifier<HTMachineRecipeDisplay>) -> Unit) {
        IDENTIFIERS.values.forEach(action)
    }

    override fun registerCategories(registry: CategoryRegistry) {
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            registry.add(HTMachineRecipeCategory(type))
            registry.addWorkstations(type.categoryId, EntryStacks.of(type))
        }
        registry.addWorkstations(
            HTMachineType.Single.GRINDER.categoryId,
            EntryStacks.of(RagiumBlocks.MANUAL_GRINDER),
        )
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            registry.registerRecipeFiller(HTMachineRecipe::class.java, type, ::HTMachineRecipeDisplay)
        }
    }

    override fun registerScreens(registry: ScreenRegistry) {
        registry.registerContainerClickArea(
            Rectangle(5 + 18 * 4, 5 + 18 * 1, 18, 18),
            HTMachineScreen::class.java,
            *IDENTIFIERS.values.toTypedArray(),
        )
    }

    @Suppress("UnstableApiUsage")
    override fun registerTransferHandlers(registry: TransferHandlerRegistry) {
        forEachId { id: CategoryIdentifier<HTMachineRecipeDisplay> ->
            registry.register(
                SimpleTransferHandler.create(
                    HTMachineScreenHandler::class.java,
                    id,
                    SimpleTransferHandler.IntRange(0, 3),
                ),
            )
        }
    }
}
