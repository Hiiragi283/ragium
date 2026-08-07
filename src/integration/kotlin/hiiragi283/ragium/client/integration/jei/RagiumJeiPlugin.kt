package hiiragi283.ragium.client.integration.jei

import hiiragi283.core.api.HTPhysicalSideHelper
import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper
import hiiragi283.core.api.integration.jei.HTJeiWorkstationHelper
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.castRecipe
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.util.getOrThrow
import hiiragi283.core.client.integration.jei.category.HTItemToItemRecipeCategory
import hiiragi283.core.client.integration.jei.category.base.HTDoubleItemToItemRecipeCategory
import hiiragi283.core.client.integration.jei.category.base.HTItemAndFluidToItemRecipeCategory
import hiiragi283.core.client.integration.jei.category.base.HTItemOrFluidRecipeCategory
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.support.recipe.base.HTBasicDoubleItemToItemRecipe
import hiiragi283.core.support.recipe.base.HTBasicItemAndFluidToItemRecipe
import hiiragi283.core.support.recipe.base.HTBasicItemOrFluidRecipe
import hiiragi283.core.support.recipe.base.HTBasicItemToItemRecipe
import hiiragi283.core.support.recipe.base.HTBasicItemToMultiItemRecipe
import hiiragi283.core.support.recipe.viewer.display.HTRecipeDisplayFactories
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.base.HTPlantingRecipe
import hiiragi283.ragium.client.integration.jei.category.HTAlloyingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.HTChemicalReactingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.HTCuttingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.HTImplodingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.HTMeltingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.HTMixingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.HTPlantingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.HTRefiningRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.HTWashingRecipeCategory
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.RTPlantingRecipe
import hiiragi283.ragium.common.recipe.RTSmeltingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.custom.HTBookMeltingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeDisplayFactories
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import kotlin.streams.asSequence
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment

@JeiPlugin
class RagiumJeiPlugin : HTJeiPlugin(RagiumAPI.MOD_ID) {
    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        registration.registerSubtypeInterpreter(
            RagiumBlocks.UNIVERSAL_CHEST.asItem(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(HCDataComponents.COLOR) },
        )
        registration.registerSubtypeInterpreter(
            RagiumBlocks.IMITATION_SPAWNER.asItem(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(RagiumDataComponents.SPAWNER_MOB) },
        )

        registration.registerSubtypeInterpreter(
            RagiumItems.RAGI_TICKET.get(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(
            // Mechanical
            HTAlloyingRecipeCategory(guiHelper),
            HTDoubleItemToItemRecipeCategory(guiHelper, RagiumRecipeViewerTypes.ASSEMBLING),
            HTItemToItemRecipeCategory(guiHelper, RagiumRecipeViewerTypes.COMPRESSING),
            HTCuttingRecipeCategory(guiHelper),
            HTItemToItemRecipeCategory(guiHelper, RagiumRecipeViewerTypes.SMELTING),
            // Heat
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumRecipeViewerTypes.FREEZING),
            HTImplodingRecipeCategory(guiHelper),
            HTMeltingRecipeCategory(guiHelper),
            HTItemOrFluidRecipeCategory(guiHelper, RagiumRecipeViewerTypes.PYROLYZING),
            HTRefiningRecipeCategory(guiHelper),
            // Chemical
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumRecipeViewerTypes.BATHING),
            HTChemicalReactingRecipeCategory(guiHelper),
            HTMixingRecipeCategory(guiHelper),
            HTWashingRecipeCategory(guiHelper),
            // Bio
            HTPlantingRecipeCategory(guiHelper),
            // Electronics
            HTDoubleItemToItemRecipeCategory(guiHelper, RagiumRecipeViewerTypes.PRINTING),
            // Arcane
        )
    }

    override fun registerRecipes(helper: HTJeiRecipeHelper) {
        fun itemOrFluid(viewerType: HTRecipeViewerType<HTProgressRecipeDisplay>, lookup: HTRecipeLookup<HTItemOrFluidRecipe>) {
            helper.addDisplayRecipes(viewerType, lookup) {
                it.castRecipe<HTItemOrFluidRecipe, HTBasicItemOrFluidRecipe>()?.let(RagiumRecipeDisplayFactories::itemOrFluid)
            }
        }

        // Mechanical
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.ALLOYING, RagiumRecipeLookups.ALLOYING, RagiumRecipeDisplayFactories::alloying)
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.ASSEMBLING, RagiumRecipeLookups.ASSEMBLING) {
            it.castRecipe<HTDoubleItemToItemRecipe, HTBasicDoubleItemToItemRecipe>()?.let(RagiumRecipeDisplayFactories::doubleItem)
        }
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.COMPRESSING, RagiumRecipeLookups.COMPRESSING) {
            it.castRecipe<HTItemToItemRecipe, HTBasicItemToItemRecipe>()?.let(HTRecipeDisplayFactories::itemToItem)
        }
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.CUTTING, RagiumRecipeLookups.CUTTING) {
            it.castRecipe<HTItemToMultiItemRecipe, HTBasicItemToMultiItemRecipe>()?.let(HTRecipeDisplayFactories::itemToMultiItem)
        }
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.SMELTING, RagiumRecipeLookups.SMELTING) {
            it.castRecipe<HTItemToItemRecipe, RTSmeltingRecipe>()?.let(HTRecipeDisplayFactories::itemToItem)
        }
        // Heat
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.FREEZING, RagiumRecipeLookups.FREEZING) {
            it.castRecipe<HTItemAndFluidToItemRecipe, HTBasicItemAndFluidToItemRecipe>()?.let(RagiumRecipeDisplayFactories::itemAndFluidToItem)
        }
        helper.addFlatDisplayRecipes(RagiumRecipeViewerTypes.IMPLODING, RagiumRecipeLookups.IMPLODING, RagiumRecipeDisplayFactories::imploding)
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.MELTING, RagiumRecipeLookups.MELTING) {
            it.castRecipe<HTItemToFluidRecipe, HTMeltingRecipe>()?.let(RagiumRecipeDisplayFactories::melting)
        }
        itemOrFluid(RagiumRecipeViewerTypes.PYROLYZING, RagiumRecipeLookups.PYROLYZING)
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.REFINING, RagiumRecipeLookups.REFINING, RagiumRecipeDisplayFactories::refining)

        // Chemical
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.BATHING, RagiumRecipeLookups.BATHING) {
            it.castRecipe<HTItemAndFluidToItemRecipe, HTBasicItemAndFluidToItemRecipe>()?.let(RagiumRecipeDisplayFactories::itemAndFluidToItem)
        }
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.CHEMICAL_REACTING, RagiumRecipeLookups.CHEMICAL_REACTING, RagiumRecipeDisplayFactories::reacting)
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.MIXING, RagiumRecipeLookups.MIXING, RagiumRecipeDisplayFactories::mixing)
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.WASHING, RagiumRecipeLookups.WASHING, RagiumRecipeDisplayFactories::washing)
        // Bio
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.PLANTING, RagiumRecipeLookups.PLANTING) {
            it.castRecipe<HTPlantingRecipe, RTPlantingRecipe>()?.let(RagiumRecipeDisplayFactories::planting)
        }

        // Electronics
        helper.addDisplayRecipes(RagiumRecipeViewerTypes.PRINTING, RagiumRecipeLookups.PRINTING) {
            it.castRecipe<HTDoubleItemToItemRecipe, HTBasicDoubleItemToItemRecipe>()?.let(RagiumRecipeDisplayFactories::doubleItem)
        }

        // Arcane

        registerCustomRecipes(helper)
    }

    private fun registerCustomRecipes(helper: HTJeiRecipeHelper) {
        // Melting
        helper.addDisplayRecipes(
            RagiumRecipeViewerTypes.MELTING,
            HTPhysicalSideHelper.lookup(Registries.ENCHANTMENT)
                .getOrThrow()
                .listElements()
                .asSequence()
                .map { holder: Holder.Reference<Enchantment> ->
                    HTProgressRecipeDisplay(
                        holder.toLike().getId().withPrefix("${RagiumConst.MELTING}/exp_from_ench_book/"),
                        HTRecipeContents.create {
                            val enchBook: ItemStack = createEnchantedBook(holder)
                            addInput(enchBook)
                            addOutput(HTBookMeltingRecipe.assemble(enchBook))
                        },
                        HTProgressData.time(100),
                    )
                },
        )
    }

    override fun registerRecipeCatalysts(helper: HTJeiWorkstationHelper) {
        helper.add(HCRecipeViewerTypes.BREWING, RagiumBlocks.BREWERY.toStack())
        helper.addAll(HCRecipeViewerTypes.CHARGING, setOf(RagiumBlocks.BATTERY, RagiumBlocks.CREATIVE_BATTERY).map { it.toStack() })
        helper.add(HCRecipeViewerTypes.CRUSHING, RagiumBlocks.CRUSHER.toStack())
        val tanks: List<ItemStack> = listOf(RagiumBlocks.TANK, RagiumBlocks.VOID_TANK, RagiumBlocks.CREATIVE_TANK).map { it.toStack() }
        helper.addAll(HCRecipeViewerTypes.EMPTYING, tanks)
        helper.addAll(HCRecipeViewerTypes.FILLING, tanks)

        helper.add(RecipeTypes.SMELTING, RagiumBlocks.ELECTRIC_FURNACE.toStack())
        helper.add(RecipeTypes.STONECUTTING, RagiumBlocks.AUTO_CHISEL.toStack())

        helper.addFromViewerType(
            // Mechanical
            RagiumRecipeViewerTypes.ALLOYING,
            RagiumRecipeViewerTypes.ASSEMBLING,
            RagiumRecipeViewerTypes.COMPRESSING,
            RagiumRecipeViewerTypes.CUTTING,
            RagiumRecipeViewerTypes.SMELTING,
            // Heat
            RagiumRecipeViewerTypes.FREEZING,
            RagiumRecipeViewerTypes.IMPLODING,
            RagiumRecipeViewerTypes.MELTING,
            RagiumRecipeViewerTypes.PYROLYZING,
            RagiumRecipeViewerTypes.REFINING,
            // Chemical
            RagiumRecipeViewerTypes.BATHING,
            RagiumRecipeViewerTypes.CHEMICAL_REACTING,
            RagiumRecipeViewerTypes.MIXING,
            RagiumRecipeViewerTypes.WASHING,
            // Bio
            RagiumRecipeViewerTypes.PLANTING,
            // Electronics
            RagiumRecipeViewerTypes.PRINTING,
            // Arcane
            RagiumRecipeViewerTypes.ENCHANTING,
        )
    }
}
