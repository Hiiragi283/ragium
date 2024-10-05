package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.api.recipe.alchemy.HTAlchemyRecipe
import hiiragi283.ragium.api.recipe.alchemy.HTInfusionRecipe
import hiiragi283.ragium.api.recipe.alchemy.HTTransformRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.client.gui.HTMachineScreen
import hiiragi283.ragium.client.integration.rei.category.HTAlchemyRecipeCategory
import hiiragi283.ragium.client.integration.rei.category.HTFluidPumpCategory
import hiiragi283.ragium.client.integration.rei.category.HTMachineRecipeCategory
import hiiragi283.ragium.client.integration.rei.display.HTDisplay
import hiiragi283.ragium.client.integration.rei.display.HTInfusionRecipeDisplay
import hiiragi283.ragium.client.integration.rei.display.HTMachineRecipeDisplay
import hiiragi283.ragium.client.integration.rei.display.HTTransformRecipeDisplay
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.data.HTFluidPumpEntryLoader
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.item.HTFluidCubeItem
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
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
import net.minecraft.registry.RegistryKey
import net.minecraft.world.biome.Biome

@Environment(EnvType.CLIENT)
object RagiumREIClient : REIClientPlugin {
    init {
        RagiumAPI.log { info("REI Integration enabled!") }
    }

    @JvmField
    val FLUID_PUMP: CategoryIdentifier<HTFluidPumpCategory.FluidDisplay> =
        CategoryIdentifier.of(RagiumAPI.MOD_ID, "fluid_pump")

    @JvmStatic
    val ALCHEMY: CategoryIdentifier<HTDisplay<out HTAlchemyRecipe>> =
        CategoryIdentifier.of(RagiumAPI.MOD_ID, "alchemical_infusion")

    @JvmStatic
    fun getMachineIds(): List<CategoryIdentifier<HTMachineRecipeDisplay>> = HTMachineTypeRegistry.types.map { CategoryIdentifier.of(it.id) }

    @JvmStatic
    fun forEachId(action: (CategoryIdentifier<HTMachineRecipeDisplay>) -> Unit) {
        getMachineIds().forEach(action)
    }

    //    REIClientPlugin    //

    override fun registerCategories(registry: CategoryRegistry) {
        // Machines
        HTMachineTypeRegistry.processors.forEach { type ->
            registry.add(HTMachineRecipeCategory(type))
            HTMachineTier.entries.mapNotNull(type::createEntryStack).forEach { stack: EntryStack<ItemStack> ->
                registry.addWorkstations(type.categoryId, stack)
            }
        }
        registry.addWorkstations(
            RagiumMachineTypes.Processor.GRINDER.categoryId,
            EntryStacks.of(RagiumContents.MANUAL_GRINDER),
        )
        // Fluid Pump
        registry.add(HTFluidPumpCategory)
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
        // Fluid Pumps
        HTFluidPumpEntryLoader.registry.forEach { (biomeKey: RegistryKey<Biome>, item: HTFluidCubeItem) ->
            registry.add(HTFluidPumpCategory.FluidDisplay(biomeKey, item))
        }
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
            HTMachineScreen::class.java,
            *getMachineIds().toTypedArray(),
        )
    }

    @Suppress("UnstableApiUsage")
    override fun registerTransferHandlers(registry: TransferHandlerRegistry) {
        // Machines
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
