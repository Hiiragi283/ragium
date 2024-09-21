package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.client.gui.HTMachineScreen
import hiiragi283.ragium.client.integration.rei.category.HTAlchemyRecipeCategory
import hiiragi283.ragium.client.integration.rei.category.HTFluidPumpCategory
import hiiragi283.ragium.client.integration.rei.category.HTMachineRecipeCategory
import hiiragi283.ragium.client.integration.rei.display.HTDisplay
import hiiragi283.ragium.client.integration.rei.display.HTInfusionRecipeDisplay
import hiiragi283.ragium.client.integration.rei.display.HTMachineRecipeDisplay
import hiiragi283.ragium.client.integration.rei.display.HTTransformRecipeDisplay
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.data.HTFluidPumpEntryLoader
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.item.HTFluidCubeItem
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTAlchemyRecipe
import hiiragi283.ragium.common.recipe.HTInfusionRecipe
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTTransformRecipe
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
import net.minecraft.registry.RegistryKey
import net.minecraft.world.biome.Biome

@Environment(EnvType.CLIENT)
object RagiumREIClient : REIClientPlugin {
    init {
        Ragium.log { info("REI Integration enabled!") }
    }

    @JvmField
    val FLUID_PUMP: CategoryIdentifier<HTFluidPumpCategory.FluidDisplay> =
        CategoryIdentifier.of(Ragium.MOD_ID, "fluid_pump")

    @JvmStatic
    val ALCHEMY: CategoryIdentifier<HTDisplay<out HTAlchemyRecipe>> =
        CategoryIdentifier.of(Ragium.MOD_ID, "alchemical_infusion")

    private val CATEGORY_IDS: Map<HTMachineType, CategoryIdentifier<HTMachineRecipeDisplay>> =
        HTMachineType.getEntries().associateWith { CategoryIdentifier.of(it.id) }

    @JvmStatic
    fun getCategoryId(type: HTMachineType): CategoryIdentifier<HTMachineRecipeDisplay> = CATEGORY_IDS[type]!!

    @JvmStatic
    fun forEachId(action: (CategoryIdentifier<HTMachineRecipeDisplay>) -> Unit) {
        CATEGORY_IDS.values.forEach(action)
    }

    //    REIClientPlugin    //

    override fun registerCategories(registry: CategoryRegistry) {
        // Machines
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            registry.add(HTMachineRecipeCategory(type))
            registry.addWorkstations(type.categoryId, EntryStacks.of(type))
        }
        registry.addWorkstations(
            HTMachineType.Single.GRINDER.categoryId,
            EntryStacks.of(RagiumBlocks.MANUAL_GRINDER),
        )
        // Fluid Pump
        registry.add(HTFluidPumpCategory)
        // Alchemy Recipe
        registry.add(HTAlchemyRecipeCategory)
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        // Machines
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            registry.registerRecipeFiller(HTMachineRecipe::class.java, type, ::HTMachineRecipeDisplay)
        }
        // Fluid Pumps
        HTFluidPumpEntryLoader.registry.forEach { (biomeKey: RegistryKey<Biome>, item: HTFluidCubeItem) ->
            registry.add(HTFluidPumpCategory.FluidDisplay(biomeKey, item))
        }
        // Alchemy Infusion
        registry.registerRecipeFiller(HTInfusionRecipe::class.java, HTAlchemyRecipe.Type, ::HTInfusionRecipeDisplay)
        registry.registerRecipeFiller(HTTransformRecipe::class.java, HTAlchemyRecipe.Type, ::HTTransformRecipeDisplay)
    }

    override fun registerScreens(registry: ScreenRegistry) {
        // Machines
        registry.registerContainerClickArea(
            Rectangle(5 + 18 * 4, 5 + 18 * 1, 18, 18),
            HTMachineScreen::class.java,
            *CATEGORY_IDS.values.toTypedArray(),
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
