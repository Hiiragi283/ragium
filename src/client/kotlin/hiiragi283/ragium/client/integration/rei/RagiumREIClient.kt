package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.alchemy.HTAlchemyRecipe
import hiiragi283.ragium.api.recipe.alchemy.HTInfusionRecipe
import hiiragi283.ragium.api.recipe.alchemy.HTTransformRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.trade.HTTradeOfferRegistry
import hiiragi283.ragium.client.gui.HTProcessorScreen
import hiiragi283.ragium.client.integration.rei.category.HTAlchemyRecipeCategory
import hiiragi283.ragium.client.integration.rei.category.HTMachineRecipeCategory
import hiiragi283.ragium.client.integration.rei.category.HTTradeOfferCategory
import hiiragi283.ragium.client.integration.rei.display.*
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEnchantments
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
import me.shedaniel.rei.plugin.common.BuiltinPlugin
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack

@Environment(EnvType.CLIENT)
object RagiumREIClient : REIClientPlugin {
    init {
        RagiumAPI.log { info("REI Integration enabled!") }
    }

    @JvmField
    val ALCHEMY: CategoryIdentifier<HTDisplay<out HTAlchemyRecipe>> =
        CategoryIdentifier.of(RagiumAPI.MOD_ID, "alchemical_infusion")

    @JvmField
    val TRADE_OFFER: CategoryIdentifier<HTTradeOfferDisplay> =
        CategoryIdentifier.of(RagiumAPI.MOD_ID, "trade_offer")

    @JvmStatic
    fun getMachineIds(): List<CategoryIdentifier<HTMachineRecipeDisplay>> = RagiumAPI
        .getInstance()
        .machineTypeRegistry
        .processors
        .map(HTMachineType::categoryId)

    @JvmStatic
    fun forEachId(action: (CategoryIdentifier<HTMachineRecipeDisplay>) -> Unit) {
        getMachineIds().forEach(action)
    }

    //    REIClientPlugin    //

    override fun registerCategories(registry: CategoryRegistry) {
        // Machines
        RagiumAPI.getInstance().machineTypeRegistry.processors.forEach { type: HTMachineType ->
            registry.add(HTMachineRecipeCategory(type))
            HTMachineTier.entries.map(type::createEntryStack).forEach { stack: EntryStack<ItemStack> ->
                registry.addWorkstations(type.categoryId, stack)
            }
        }
        registry.addWorkstations(
            RagiumMachineTypes.Processor.GRINDER.categoryId,
            EntryStacks.of(RagiumBlocks.MANUAL_GRINDER),
        )

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
        // Alchemy Recipe
        registry.add(HTAlchemyRecipeCategory)
        registry.addWorkstations(ALCHEMY, EntryStacks.of(RagiumBlocks.ALCHEMICAL_INFUSER))
        registry.addWorkstations(ALCHEMY, EntryStacks.of(RagiumContents.Misc.ALCHEMY_STUFF))
        // Trade Offer
        registry.add(HTTradeOfferCategory)
        registry.addWorkstations(TRADE_OFFER, EntryStacks.of(RagiumContents.Ingots.RAGI_STEEL))
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        // Machines
        registry.registerRecipeFiller(
            HTMachineRecipe::class.java,
            RagiumRecipeTypes.MACHINE,
            ::HTMachineRecipeDisplay,
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
        // Trade Offer
        HTTradeOfferRegistry.registry
            .map { (tier: HTMachineTier, factory: List<HTTradeOfferRegistry.Factory>) ->
                factory
                    .mapNotNull { it.create(MinecraftClient.getInstance().player) }
                    .map { HTTradeOfferDisplay(tier, it) }
            }.flatten()
            .forEach(registry::add)
        /*registry.registerVisibilityPredicate { _: DisplayCategory<*>, display: Display ->
            if (display is HTDisplay<*>) {
                val id: Identifier = display.id
                val recipe: HTRecipeBase<*> = display.recipe
                if (recipe is HTRequireScanRecipe && recipe.requireScan) {
                    if (MinecraftClient.getInstance().server?.dataDriveManager?.contains(id) != true) {
                        EventResult.interruptFalse()
                    }
                }
            }
            EventResult.pass()
        }*/
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
