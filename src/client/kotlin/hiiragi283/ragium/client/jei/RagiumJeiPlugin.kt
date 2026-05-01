package hiiragi283.ragium.client.jei

import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper.addDisplayRecipes
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper.addLookupRecipes
import hiiragi283.core.api.integration.jei.HTJeiWorkstationHelper
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.impl.recipe.HTBasicItemOrFluidRecipe
import hiiragi283.core.impl.recipe.HTBasicItemToMultiItemRecipe
import hiiragi283.core.impl.recipe.viewer.display.HTRecipeDisplayFactories
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.jei.category.HTAlloyingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTAssemblingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTChemicalReactingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTCuttingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTFreezingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemOrFluidRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMassFabricatingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMeltingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMixingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTWashingRecipeCategory
import hiiragi283.ragium.common.recipe.RTPlantingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeDisplayFactories
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.world.item.ItemStack

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
            // Machine - Basic
            HTAlloyingRecipeCategory(guiHelper),
            HTAssemblingRecipeCategory(guiHelper),
            HTCuttingRecipeCategory(guiHelper),
            // HTPlantingRecipeCategory(guiHelper),
            // Machine - Advanced
            HTFreezingRecipeCategory(guiHelper),
            HTMeltingRecipeCategory(guiHelper),
            HTItemOrFluidRecipeCategory(guiHelper, RagiumRecipeViewerTypes.PYROLYZING),
            HTItemOrFluidRecipeCategory(guiHelper, RagiumRecipeViewerTypes.REFINING),
            HTWashingRecipeCategory(guiHelper),
            // Machine - Elite
            HTChemicalReactingRecipeCategory(guiHelper),
            HTItemOrFluidRecipeCategory(guiHelper, RagiumRecipeViewerTypes.CHEMICAL_WASHING),
            HTMixingRecipeCategory(guiHelper),
            // Machine - Ultimate
            HTMassFabricatingRecipeCategory(guiHelper),
            // Device - Ultimate
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        fun itemOrFluid(viewerType: HTRecipeViewerType<HTProgressRecipeDisplay>, lookup: HTRecipeLookup<HTItemOrFluidRecipe>) {
            addDisplayRecipes(registration, viewerType, lookup) {
                it.castRecipe<HTBasicItemOrFluidRecipe>()?.let(RagiumRecipeDisplayFactories::itemOrFluid)
            }
        }

        // Machine - Basic
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.ALLOYING,
            RagiumRecipeLookups.ALLOYING,
            RagiumRecipeDisplayFactories::alloying,
        )
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.ASSEMBLING,
            RagiumRecipeLookups.ASSEMBLING,
            RagiumRecipeDisplayFactories::assembling,
        )
        addDisplayRecipes(registration, RagiumRecipeViewerTypes.CUTTING, RagiumRecipeLookups.CUTTING) {
            it.castRecipe<HTBasicItemToMultiItemRecipe>()?.let(HTRecipeDisplayFactories::itemToMultiItem)
        }
        addDisplayRecipes(registration, RagiumRecipeViewerTypes.PLANTING, RagiumRecipeLookups.PLANTING) {
            it.castRecipe<RTPlantingRecipe>()?.let(RagiumRecipeDisplayFactories::planting)
        }
        // Machine - Advanced
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.FREEZING,
            RagiumRecipeLookups.FREEZING,
            RagiumRecipeDisplayFactories::freezing,
        )
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.MELTING,
            RagiumRecipeLookups.MELTING,
            RagiumRecipeDisplayFactories::melting,
        )
        itemOrFluid(RagiumRecipeViewerTypes.PYROLYZING, RagiumRecipeLookups.PYROLYZING)
        itemOrFluid(RagiumRecipeViewerTypes.REFINING, RagiumRecipeLookups.REFINING)
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.WASHING,
            RagiumRecipeLookups.WASHING,
            RagiumRecipeDisplayFactories::washing,
        )
        // Machine - Elite
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.CHEMICAL_REACTING,
            RagiumRecipeLookups.CHEMICAL_REACTING,
            RagiumRecipeDisplayFactories::reacting,
        )
        itemOrFluid(RagiumRecipeViewerTypes.CHEMICAL_WASHING, RagiumRecipeLookups.CHEMICAL_WASHING)
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.MIXING,
            RagiumRecipeLookups.MIXING,
            RagiumRecipeDisplayFactories::mixing,
        )
        // Machine - Ultimate
        addLookupRecipes(
            registration,
            RagiumRecipeViewerTypes.MASS_FABRICATING,
            RagiumRecipeLookups.MASS_FABRICATING,
            sorter = compareBy { it.point },
        )
        // Device - Ultimate
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        HTJeiWorkstationHelper.add(registration, HCRecipeViewerTypes.BREWING, RagiumBlocks.BREWERY)
        HTJeiWorkstationHelper.add(registration, HCRecipeViewerTypes.CHARGING, RagiumBlocks.BATTERY, RagiumBlocks.CREATIVE_BATTERY)
        HTJeiWorkstationHelper.add(registration, HCRecipeViewerTypes.CRUSHING, RagiumBlocks.CRUSHER)
        val tanks: List<ItemStack> =
            listOf(RagiumBlocks.TANK, RagiumBlocks.VOID_TANK, RagiumBlocks.CREATIVE_TANK).map { it.toStack() }
        HTJeiWorkstationHelper.add(registration, HCRecipeViewerTypes.EMPTYING, tanks)
        HTJeiWorkstationHelper.add(registration, HCRecipeViewerTypes.FILLING, tanks)

        registration.addRecipeCatalysts(RecipeTypes.SMELTING, RagiumBlocks.ELECTRIC_FURNACE)
        registration.addRecipeCatalysts(RecipeTypes.STONECUTTING, RagiumBlocks.AUTO_CHISEL)

        HTJeiWorkstationHelper.addFromViewerType(
            registration,
            // Machine - Basic
            RagiumRecipeViewerTypes.ALLOYING,
            RagiumRecipeViewerTypes.ASSEMBLING,
            RagiumRecipeViewerTypes.CUTTING,
            RagiumRecipeViewerTypes.PLANTING,
            // Machine - Advanced
            RagiumRecipeViewerTypes.FREEZING,
            RagiumRecipeViewerTypes.IMPLODING,
            RagiumRecipeViewerTypes.MELTING,
            RagiumRecipeViewerTypes.PYROLYZING,
            RagiumRecipeViewerTypes.REFINING,
            RagiumRecipeViewerTypes.WASHING,
            // Machine - Elite
            RagiumRecipeViewerTypes.CHEMICAL_REACTING,
            RagiumRecipeViewerTypes.CHEMICAL_WASHING,
            RagiumRecipeViewerTypes.MIXING,
            // Machine - Ultimate
            RagiumRecipeViewerTypes.MASS_FABRICATING,
            // Device - Ultimate
            RagiumRecipeViewerTypes.ENCHANTING,
        )
    }
}
