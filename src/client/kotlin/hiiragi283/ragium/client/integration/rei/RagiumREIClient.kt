package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeNew
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEnchantments
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.screen.HTProcessorScreenHandler
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryStacks
import me.shedaniel.rei.plugin.common.BuiltinPlugin
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeEntry

@Environment(EnvType.CLIENT)
object RagiumREIClient : REIClientPlugin {
    init {
        RagiumAPI.log { info("REI Integration enabled!") }
    }

    //    REIClientPlugin    //

    override fun registerCategories(registry: CategoryRegistry) {
        // Machines
        RagiumAPI.getInstance().machineTypeRegistry.processors.forEach { type: HTMachineType ->
            registry.add(HTMachineRecipeCategory(type))
            registry.add(HTMachineRecipeCategoryNew(type))
            HTMachineTier.entries.map(type::createEntryStack).forEach { stack: EntryStack<ItemStack> ->
                registry.addWorkstations(type.categoryId, stack)
                registry.addWorkstations(type.categoryIdNew, stack)
            }
        }
        registry.addWorkstations(
            RagiumMachineTypes.Processor.GRINDER.categoryId,
            EntryStacks.of(RagiumBlocks.MANUAL_GRINDER),
        )

        // Enchantment
        registry.addWorkstations(
            BuiltinPlugin.SMELTING,
            createEnchantedBook(RagiumEnchantments.SMELTING),
        )
        registry.addWorkstations(
            RagiumMachineTypes.Processor.GRINDER.categoryId,
            createEnchantedBook(RagiumEnchantments.SLEDGE_HAMMER),
        )
        registry.addWorkstations(
            RagiumMachineTypes.SAW_MILL.categoryId,
            createEnchantedBook(RagiumEnchantments.BUZZ_SAW),
        )
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        // Machines
        registry.registerRecipeFiller(
            HTMachineRecipe::class.java,
            RagiumRecipeTypes.MACHINE,
            ::HTMachineRecipeDisplay,
        )

        registry.registerRecipeFiller(
            HTMachineRecipeNew.Large::class.java,
            RagiumRecipeTypes.LARGE_MACHINE,
        ) { entry: RecipeEntry<HTMachineRecipeNew.Large> ->
            HTMachineRecipeDisplayNew.Large(
                entry.value,
                entry.id,
            )
        }

        registry.registerRecipeFiller(
            HTMachineRecipeNew.Simple::class.java,
            RagiumRecipeTypes.SIMPLE_MACHINE,
        ) { entry: RecipeEntry<HTMachineRecipeNew.Simple> ->
            HTMachineRecipeDisplayNew.Simple(
                entry.value,
                entry.id,
            )
        }
    }

    /*override fun registerScreens(registry: ScreenRegistry) {
        // Machines
        registry.registerContainerClickArea(
            Rectangle(5 + 18 * 4, 5 + 18 * 1, 18, 18),
            HTProcessorScreen::class.java,
     *getMachineIds().toTypedArray(),
        )
    }*/

    @Suppress("UnstableApiUsage")
    override fun registerTransferHandlers(registry: TransferHandlerRegistry) {
        // Machines
        RagiumAPI
            .getInstance()
            .machineTypeRegistry
            .processors
            .map(HTMachineType::categoryIdNew)
            .forEach { id: CategoryIdentifier<HTMachineRecipeDisplayNew> ->
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
