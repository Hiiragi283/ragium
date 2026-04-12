package hiiragi283.ragium.client.jei

import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.client.jei.category.HTItemOrFluidRecipeCategory
import hiiragi283.core.client.jei.category.base.HTDoubleMultiOutputRecipeCategory
import hiiragi283.core.client.jei.category.base.HTSingleMultiOutputRecipeCategory
import hiiragi283.core.client.jei.extension.HTBasicDoubleMultiOutputRecipeCategoryExtension
import hiiragi283.core.client.jei.extension.HTBasicItemOrFluidRecipeCategoryExtension
import hiiragi283.core.client.jei.extension.HTBasicSingleMultiOutputRecipeCategoryExtension
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.jei.category.HTCombiningRecipeCategory
import hiiragi283.ragium.client.jei.category.HTCuttingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTElectrolyzingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTEnchantingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTFreezingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMeltingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMixingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTPlantingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTWashingRecipeCategory
import hiiragi283.ragium.client.jei.category.RagiumDuplicatingRecipeCategory
import hiiragi283.ragium.client.jei.extension.HTHolderEnchantingRecipeCategoryExtension
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
import net.minecraft.world.item.ItemStack

@JeiPlugin
class RagiumJeiPlugin : HTJeiPlugin(RagiumAPI.MOD_ID) {
    companion object {
        // SingleMultiOutput
        @JvmStatic
        lateinit var cutting: HTSingleMultiOutputRecipeCategory
            private set

        // DoubleMultiOutput
        @JvmStatic
        lateinit var planting: HTDoubleMultiOutputRecipeCategory
            private set

        // ItemOrFluid
        @JvmStatic
        lateinit var pyrolyzing: HTItemOrFluidRecipeCategory
            private set

        @JvmStatic
        lateinit var refining: HTItemOrFluidRecipeCategory
            private set

        @JvmStatic
        lateinit var chemicalWashing: HTItemOrFluidRecipeCategory
            private set

        // Other
        @JvmStatic
        lateinit var enchanting: HTEnchantingRecipeCategory
            private set
    }

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
        val manager: IIngredientManager = registration.jeiHelpers.ingredientManager

        initSingleMultiOutput(guiHelper, manager)
        initDoubleMultiOutput(guiHelper, manager)
        initItemOrFluid(guiHelper, manager)

        enchanting = HTEnchantingRecipeCategory(guiHelper)
        enchanting.addExtension(HTHolderEnchantingRecipeCategoryExtension)

        registration.addRecipeCategories(
            // Machine - Basic
            HTCombiningRecipeCategory(3, guiHelper, RagiumJeiRecipeTypes.ALLOYING),
            HTCombiningRecipeCategory(2, guiHelper, RagiumJeiRecipeTypes.ASSEMBLING),
            cutting,
            planting,
            // Machine - Advanced
            HTFreezingRecipeCategory(guiHelper),
            HTMeltingRecipeCategory(guiHelper),
            pyrolyzing,
            refining,
            // Machine - Elite
            chemicalWashing,
            HTElectrolyzingRecipeCategory(guiHelper),
            HTMixingRecipeCategory(guiHelper),
            HTWashingRecipeCategory(guiHelper),
            // Machine - Ultimate
            enchanting,
            RagiumDuplicatingRecipeCategory(guiHelper),
            // Device
        )
    }

    private fun initSingleMultiOutput(guiHelper: IGuiHelper, manager: IIngredientManager) {
        cutting = HTCuttingRecipeCategory(guiHelper)

        cutting.addExtension(HTBasicSingleMultiOutputRecipeCategoryExtension())
    }

    private fun initDoubleMultiOutput(guiHelper: IGuiHelper, manager: IIngredientManager) {
        planting = HTPlantingRecipeCategory(guiHelper)

        planting.addExtension(HTBasicDoubleMultiOutputRecipeCategoryExtension())
    }

    private fun initItemOrFluid(guiHelper: IGuiHelper, manager: IIngredientManager) {
        pyrolyzing = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PYROLYZING)
        refining = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.REFINING)
        chemicalWashing = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CHEMICAL_WASHING)

        pyrolyzing.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
        refining.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
        chemicalWashing.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Machine - Basic
        registration.addRecipes(RagiumJeiRecipeTypes.ALLOYING)
        registration.addRecipes(RagiumJeiRecipeTypes.ASSEMBLING)
        registration.addRecipes(RagiumJeiRecipeTypes.CUTTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PLANTING)
        // Machine - Advanced
        registration.addRecipes(RagiumJeiRecipeTypes.FREEZING)
        registration.addRecipes(RagiumJeiRecipeTypes.MELTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PYROLYZING)
        registration.addRecipes(RagiumJeiRecipeTypes.REFINING)
        // Machine - Elite
        registration.addRecipes(RagiumJeiRecipeTypes.CHEMICAL_WASHING)
        registration.addRecipes(RagiumJeiRecipeTypes.ELECTROLYZING)
        registration.addRecipes(RagiumJeiRecipeTypes.MIXING)
        registration.addRecipes(RagiumJeiRecipeTypes.WASHING)
        // Machine - Ultimate
        registration.addRecipes(RagiumJeiRecipeTypes.DUPLICATING)
        registration.addRecipes(RagiumJeiRecipeTypes.ENCHANTING)
        // Device
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalysts(
            getRecipeType(HCRecipeViewerTypes.CHARGING),
            RagiumBlocks.BATTERY,
            RagiumBlocks.CREATIVE_BATTERY,
        )
        registration.addRecipeCatalysts(
            getRecipeType(HCRecipeViewerTypes.TANK_INTERACTION),
            RagiumBlocks.TANK,
            RagiumBlocks.VOID_TANK,
            RagiumBlocks.CREATIVE_TANK,
        )

        registration.addRecipeCatalysts(getRecipeType(HCRecipeViewerTypes.BREWING), RagiumBlocks.BREWERY)
        registration.addRecipeCatalysts(getRecipeType(HCRecipeViewerTypes.CRUSHING), RagiumBlocks.CRUSHER)

        registration.addRecipeCatalysts(RecipeTypes.SMELTING, RagiumBlocks.ELECTRIC_FURNACE)
        registration.addRecipeCatalysts(RecipeTypes.STONECUTTING, RagiumBlocks.AUTO_CHISEL)

        registration.addRecipeCatalysts(
            // Machine - Basic
            RagiumJeiRecipeTypes.ALLOYING,
            RagiumJeiRecipeTypes.ASSEMBLING,
            RagiumJeiRecipeTypes.CUTTING,
            RagiumJeiRecipeTypes.PLANTING,
            // Machine - Advanced
            RagiumJeiRecipeTypes.FREEZING,
            RagiumJeiRecipeTypes.MELTING,
            RagiumJeiRecipeTypes.PYROLYZING,
            RagiumJeiRecipeTypes.REFINING,
            // Machine - Elite
            RagiumJeiRecipeTypes.CHEMICAL_WASHING,
            RagiumJeiRecipeTypes.ELECTROLYZING,
            RagiumJeiRecipeTypes.MIXING,
            RagiumJeiRecipeTypes.WASHING,
            // Machine - Ultimate
            RagiumJeiRecipeTypes.ENCHANTING,
            RagiumJeiRecipeTypes.DUPLICATING,
            // Device
        )
    }
}
