package hiiragi283.ragium.client.jei

import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTJeiRecipeHelper
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.integration.jei.JeiRecipeType
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.client.jei.category.HTCombiningRecipeCategory
import hiiragi283.ragium.client.jei.category.HTCuttingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTFreezingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemOrFluidRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMeltingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMixingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTWashingRecipeCategory
import hiiragi283.ragium.common.recipe.HTFluidMixingRecipe
import hiiragi283.ragium.common.recipe.HTItemMixingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.viewer.HTViewerMixingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
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
            RagiumItems.LOOT_TICKET.get(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(
            // Machine - Basic
            HTCombiningRecipeCategory(guiHelper, RagiumRecipeViewerTypes.ALLOYING, RagiumRecipeSerializers.ALLOYING, 3),
            HTCombiningRecipeCategory(guiHelper, RagiumRecipeViewerTypes.ASSEMBLING, RagiumRecipeSerializers.ASSEMBLING, 2),
            HTCuttingRecipeCategory(guiHelper),
            // Machine - Advanced
            HTFreezingRecipeCategory(guiHelper),
            HTMeltingRecipeCategory(guiHelper),
            HTItemOrFluidRecipeCategory(guiHelper, RagiumRecipeViewerTypes.PYROLYZING, RagiumRecipeSerializers.PYROLYZING),
            HTItemOrFluidRecipeCategory(guiHelper, RagiumRecipeViewerTypes.REFINING, RagiumRecipeSerializers.REFINING),
            // Machine - Elite
            HTItemOrFluidRecipeCategory(guiHelper, RagiumRecipeViewerTypes.CHEMICAL_WASHING, RagiumRecipeSerializers.CHEMICAL_WASHING),
            HTMixingRecipeCategory(guiHelper),
            HTWashingRecipeCategory(guiHelper),
            // Machine - Ultimate
            // Device
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Machine - Basic
        HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.ALLOYING, RagiumRecipeLookups.ALLOYING)
        HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.ASSEMBLING, RagiumRecipeLookups.ASSEMBLING)
        HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.CUTTING, RagiumRecipeLookups.CUTTING)
        // HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.PLANTING, RagiumRecipeLookups.PLANTING)
        // Machine - Advanced
        HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.FREEZING, RagiumRecipeLookups.FREEZING)
        HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.MELTING, RagiumRecipeLookups.MELTING)
        HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.PYROLYZING, RagiumRecipeLookups.PYROLYZING)
        HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.REFINING, RagiumRecipeLookups.REFINING)
        // Machine - Elite
        HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.CHEMICAL_WASHING, RagiumRecipeLookups.CHEMICAL_WASHING)
        // HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.ELECTROLYZING, RagiumRecipeLookups.ELECTROLYZING)
        HTJeiRecipeHelper.addHolderRecipes(
            registration,
            RagiumRecipeViewerTypes.MIXING,
            RagiumRecipeLookups.MIXING
                .getAllRecipes()
                .mapNotNull { holder: HTRecipeHolder<HTMixingRecipe> ->
                    holder.mapRecipeOrNull { recipe: HTMixingRecipe ->
                        when (recipe) {
                            is HTItemMixingRecipe ->
                                HTViewerMixingRecipe(
                                    recipe.itemIngredients,
                                    listOf(recipe.fluidIngredient),
                                    listOfNotNull(recipe.result.getLeft()),
                                    listOfNotNull(recipe.result.getRight()),
                                    recipe.time,
                                )
                            is HTFluidMixingRecipe ->
                                HTViewerMixingRecipe(
                                    recipe.itemIngredient.stream().toList(),
                                    recipe.fluidIngredients,
                                    emptyList(),
                                    recipe.results,
                                    recipe.time,
                                )
                            else -> null
                        }
                    }
                },
        )
        HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.WASHING, RagiumRecipeLookups.WASHING)
        // Machine - Ultimate
        // HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.DUPLICATING, RagiumRecipeLookups.DUPLICATING)
        // HTJeiRecipeHelper.addLookupRecipes(registration, RagiumRecipeViewerTypes.ENCHANTING, RagiumRecipeLookups.ENCHANTING)
        // Device
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalysts(
            getRecipeType(HCRecipeViewerTypes.CHARGING),
            RagiumBlocks.BATTERY,
            RagiumBlocks.CREATIVE_BATTERY,
        )
        arrayOf(HCRecipeViewerTypes.EMPTYING, HCRecipeViewerTypes.FILLING)
            .map { getRecipeType(it) }
            .forEach { recipeType: JeiRecipeType<*> ->
                registration.addRecipeCatalysts(recipeType, RagiumBlocks.TANK, RagiumBlocks.VOID_TANK, RagiumBlocks.CREATIVE_TANK)
            }

        registration.addRecipeCatalysts(getRecipeType(HCRecipeViewerTypes.BREWING), RagiumBlocks.BREWERY)
        registration.addRecipeCatalysts(getRecipeType(HCRecipeViewerTypes.CRUSHING), RagiumBlocks.CRUSHER)

        registration.addRecipeCatalysts(RecipeTypes.SMELTING, RagiumBlocks.ELECTRIC_FURNACE)
        registration.addRecipeCatalysts(RecipeTypes.STONECUTTING, RagiumBlocks.AUTO_CHISEL)

        registration.addRecipeCatalysts(
            // Machine - Basic
            RagiumRecipeViewerTypes.ALLOYING,
            RagiumRecipeViewerTypes.ASSEMBLING,
            RagiumRecipeViewerTypes.CUTTING,
            RagiumRecipeViewerTypes.PLANTING,
            // Machine - Advanced
            RagiumRecipeViewerTypes.FREEZING,
            RagiumRecipeViewerTypes.MELTING,
            RagiumRecipeViewerTypes.PYROLYZING,
            RagiumRecipeViewerTypes.REFINING,
            // Machine - Elite
            RagiumRecipeViewerTypes.CHEMICAL_WASHING,
            RagiumRecipeViewerTypes.ELECTROLYZING,
            RagiumRecipeViewerTypes.MIXING,
            RagiumRecipeViewerTypes.WASHING,
            // Machine - Ultimate
            RagiumRecipeViewerTypes.ENCHANTING,
            // Device
        )

        registration.addRecipeCatalysts(getRecipeType(RagiumRecipeViewerTypes.MIXING), RagiumBlocks.FLUID_MIXER)
    }
}
