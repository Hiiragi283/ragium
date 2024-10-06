package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.api.recipe.alchemy.HTAlchemyRecipe
import hiiragi283.ragium.api.recipe.alchemy.HTInfusionRecipe
import hiiragi283.ragium.api.recipe.alchemy.HTTransformRecipe
import hiiragi283.ragium.api.recipe.machine.HTFluidDrillRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.client.gui.HTProcessorScreen
import hiiragi283.ragium.client.integration.rei.category.HTAlchemyRecipeCategory
import hiiragi283.ragium.client.integration.rei.category.HTFluidDrillRecipeCategory
import hiiragi283.ragium.client.integration.rei.category.HTMachineRecipeCategory
import hiiragi283.ragium.client.integration.rei.display.*
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.screen.HTProcessorScreenHandler
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryStacks
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.ItemStack

@Environment(EnvType.CLIENT)
object RagiumREIClient : REIClientPlugin {
    init {
        RagiumAPI.log { info("REI Integration enabled!") }
    }

    @JvmStatic
    val ALCHEMY: CategoryIdentifier<HTDisplay<out HTAlchemyRecipe>> =
        CategoryIdentifier.of(RagiumAPI.MOD_ID, "alchemical_infusion")

    @JvmField
    val FLUID_DRILL: CategoryIdentifier<HTFluidDrillRecipeDisplay> =
        CategoryIdentifier.of(RagiumAPI.MOD_ID, "fluid_drill")

    @JvmStatic
    fun getMachineIds(): List<CategoryIdentifier<HTMachineRecipeDisplay>> = HTMachineTypeRegistry.types.map { CategoryIdentifier.of(it.id) }

    @JvmStatic
    fun forEachId(action: (CategoryIdentifier<HTMachineRecipeDisplay>) -> Unit) {
        getMachineIds().forEach(action)
    }

    //    REIClientPlugin    //

    override fun registerCategories(registry: CategoryRegistry) {
        // Machines
        HTMachineTypeRegistry.processors.forEach { type: HTMachineType.Processor ->
            registry.add(HTMachineRecipeCategory(type))
            HTMachineTier.entries.mapNotNull(type::createEntryStack).forEach { stack: EntryStack<ItemStack> ->
                registry.addWorkstations(type.categoryId, stack)
            }
        }
        registry.addWorkstations(
            RagiumMachineTypes.Processor.GRINDER.categoryId,
            EntryStacks.of(RagiumContents.MANUAL_GRINDER),
        )
        // Fluid Drilling
        registry.add(HTFluidDrillRecipeCategory)
        // Alchemy Recipe
        registry.add(HTAlchemyRecipeCategory)
        registry.addWorkstations(ALCHEMY, EntryStacks.of(RagiumContents.ALCHEMICAL_INFUSER))
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        // Machines
        registry.registerRecipeFiller(
            HTMachineRecipe::class.java,
            RagiumRecipeTypes.MACHINE,
            ::HTMachineRecipeDisplay,
        )
        // Fluid Drill
        registry.registerRecipeFiller(
            HTFluidDrillRecipe::class.java,
            RagiumRecipeTypes.FLUID_DRILL,
            ::HTFluidDrillRecipeDisplay,
        )
        // Alchemy Infusion
        registry.registerRecipeFiller(
            HTInfusionRecipe::class.java,
            RagiumRecipeTypes.ALCHEMY,
            ::HTInfusionRecipeDisplay,
        )
        registry.registerRecipeFiller(
            HTTransformRecipe::class.java,
            RagiumRecipeTypes.ALCHEMY,
            ::HTTransformRecipeDisplay,
        )
    }

    override fun registerScreens(registry: ScreenRegistry) {
        // Machines
        registry.registerContainerClickArea(
            Rectangle(5 + 18 * 4, 5 + 18 * 1, 18, 18),
            HTProcessorScreen::class.java,
            *getMachineIds().toTypedArray(),
        )
    }

    @Suppress("UnstableApiUsage")
    override fun registerTransferHandlers(registry: TransferHandlerRegistry) {
        // Machines
        forEachId { id: CategoryIdentifier<HTMachineRecipeDisplay> ->
            registry.register(
                SimpleTransferHandler.create(
                    HTProcessorScreenHandler::class.java,
                    id,
                    SimpleTransferHandler.IntRange(0, 3),
                ),
            )
        }
    }
}
