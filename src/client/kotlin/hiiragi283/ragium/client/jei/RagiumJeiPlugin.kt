package hiiragi283.ragium.client.jei

import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper.addDisplayRecipes
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper.addFlatDisplayRecipes
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper.addLookupRecipes
import hiiragi283.core.api.integration.jei.HTJeiWorkstationHelper
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.client.jei.category.HTItemToItemRecipeCategory
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.impl.recipe.HTBasicItemOrFluidRecipe
import hiiragi283.core.impl.recipe.HTBasicItemToItemRecipe
import hiiragi283.core.impl.recipe.HTBasicItemToMultiItemRecipe
import hiiragi283.core.impl.recipe.viewer.display.HTRecipeDisplayFactories
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.jei.category.HTAlloyingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTAssemblingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTChemicalReactingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTCuttingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTFreezingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTImplodingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemAndFluidToItemRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemOrFluidRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMassFabricatingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMeltingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMixingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTPlantingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTRefiningRecipeCategory
import hiiragi283.ragium.client.jei.category.HTWashingRecipeCategory
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.RTPlantingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.custom.HTBookMeltingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeDisplayFactories
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.impl.recipe.HTBasicItemAndFluidToItemRecipe
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
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import kotlin.streams.asSequence

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
            HTItemToItemRecipeCategory(guiHelper, RagiumRecipeViewerTypes.COMPRESSING),
            HTCuttingRecipeCategory(guiHelper),
            HTPlantingRecipeCategory(guiHelper),
            // Machine - Advanced
            HTFreezingRecipeCategory(guiHelper),
            HTImplodingRecipeCategory(guiHelper),
            HTMeltingRecipeCategory(guiHelper),
            HTItemOrFluidRecipeCategory(guiHelper, RagiumRecipeViewerTypes.PYROLYZING),
            HTRefiningRecipeCategory(guiHelper),
            HTWashingRecipeCategory(guiHelper),
            // Machine - Elite
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumRecipeViewerTypes.BATHING),
            HTChemicalReactingRecipeCategory(guiHelper),
            HTMixingRecipeCategory(guiHelper),
            // Machine - Ultimate
            HTMassFabricatingRecipeCategory(guiHelper),
            // Device - Ultimate
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val ingredientManager: IIngredientManager = registration.ingredientManager

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
        addDisplayRecipes(registration, RagiumRecipeViewerTypes.ASSEMBLING, RagiumRecipeLookups.ASSEMBLING) {
            it.castRecipe<HTAssemblingRecipe>()?.let(RagiumRecipeDisplayFactories::assembling)
        }
        addDisplayRecipes(registration, RagiumRecipeViewerTypes.COMPRESSING, RagiumRecipeLookups.COMPRESSING) {
            it.castRecipe<HTBasicItemToItemRecipe>()?.let(HTRecipeDisplayFactories::itemToItem)
        }
        addDisplayRecipes(registration, RagiumRecipeViewerTypes.CUTTING, RagiumRecipeLookups.CUTTING) {
            it.castRecipe<HTBasicItemToMultiItemRecipe>()?.let(HTRecipeDisplayFactories::itemToMultiItem)
        }
        addDisplayRecipes(registration, RagiumRecipeViewerTypes.PLANTING, RagiumRecipeLookups.PLANTING) {
            it.castRecipe<RTPlantingRecipe>()?.let(RagiumRecipeDisplayFactories::planting)
        }
        // Machine - Advanced
        addDisplayRecipes(registration, RagiumRecipeViewerTypes.FREEZING, RagiumRecipeLookups.FREEZING) {
            it.castRecipe<HTFreezingRecipe>()?.let(RagiumRecipeDisplayFactories::freezing)
        }
        addFlatDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.IMPLODING,
            RagiumRecipeLookups.IMPLODING,
            RagiumRecipeDisplayFactories::imploding,
        )
        addDisplayRecipes(registration, RagiumRecipeViewerTypes.MELTING, RagiumRecipeLookups.MELTING) {
            it.castRecipe<HTMeltingRecipe>()?.let(RagiumRecipeDisplayFactories::melting)
        }
        itemOrFluid(RagiumRecipeViewerTypes.PYROLYZING, RagiumRecipeLookups.PYROLYZING)
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.REFINING,
            RagiumRecipeLookups.REFINING,
            RagiumRecipeDisplayFactories::refining,
        )
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.WASHING,
            RagiumRecipeLookups.WASHING,
            RagiumRecipeDisplayFactories::washing,
        )
        // Machine - Elite
        addDisplayRecipes(registration, RagiumRecipeViewerTypes.BATHING, RagiumRecipeLookups.BATHING) {
            it.castRecipe<HTBasicItemAndFluidToItemRecipe>()?.let(RagiumRecipeDisplayFactories::itemAndFluidToItem)
        }
        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.CHEMICAL_REACTING,
            RagiumRecipeLookups.CHEMICAL_REACTING,
            RagiumRecipeDisplayFactories::reacting,
        )
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

        registerCustomRecipes(registration)
    }

    private fun registerCustomRecipes(registration: IRecipeRegistration) {
        val access: RegistryAccess = Minecraft.getInstance().level?.registryAccess() ?: return

        addDisplayRecipes(
            registration,
            RagiumRecipeViewerTypes.MELTING,
            access
                .registryOrThrow(Registries.ENCHANTMENT)
                .holders()
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
            RagiumRecipeViewerTypes.COMPRESSING,
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
            RagiumRecipeViewerTypes.BATHING,
            RagiumRecipeViewerTypes.CHEMICAL_REACTING,
            RagiumRecipeViewerTypes.MIXING,
            // Machine - Ultimate
            RagiumRecipeViewerTypes.MASS_FABRICATING,
            // Device - Ultimate
            RagiumRecipeViewerTypes.ENCHANTING,
        )
    }
}
