package hiiragi283.ragium.client.jei

import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.client.jei.category.HTItemToChancedRecipeCategory
import hiiragi283.core.client.jei.category.HTItemToItemRecipeCategory
import hiiragi283.core.client.jei.extension.HTBasicItemToChancedRecipeCategoryExtension
import hiiragi283.core.client.jei.extension.HTBasicItemToItemRecipeCategoryExtension
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.jei.category.HTAlloyingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTEnchantingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemAndItemRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemOrFluidRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMixingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTWashingRecipeCategory
import hiiragi283.ragium.client.jei.extension.HTBasicItemAndItemRecipeCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTBasicItemOrFluidRecipeCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTBookCopyingRecipeCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTBucketFillingRecipeCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTDrainingRecipeCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTHolderEnchantingRecipeCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTPotionFillingRecipeCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTPrintingRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTBucketDrainingRecipe
import hiiragi283.ragium.common.recipe.special.HTPotionDrainingRecipe
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
        // ItemToItem
        @JvmStatic
        lateinit var compressing: HTItemToItemRecipeCategory
            private set

        // ItemToChanced
        @JvmStatic
        lateinit var cutting: HTItemToChancedRecipeCategory
            private set

        // ItemAndItem
        @JvmStatic
        lateinit var pressing: HTItemAndItemRecipeCategory
            private set

        @JvmStatic
        lateinit var printing: HTItemAndItemRecipeCategory
            private set

        // ItemOrFluid
        @JvmStatic
        lateinit var melting: HTItemOrFluidRecipeCategory
            private set

        @JvmStatic
        lateinit var pyrolyzing: HTItemOrFluidRecipeCategory
            private set

        @JvmStatic
        lateinit var refining: HTItemOrFluidRecipeCategory
            private set

        @JvmStatic
        lateinit var freezing: HTItemOrFluidRecipeCategory
            private set

        @JvmStatic
        lateinit var canning: HTItemOrFluidRecipeCategory
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

        initItemToItem(guiHelper, manager)
        initItemToChanced(guiHelper, manager)
        initItemAndItem(guiHelper, manager)
        initItemOrFluid(guiHelper, manager)

        enchanting = HTEnchantingRecipeCategory(guiHelper)
        enchanting.addExtension(HTHolderEnchantingRecipeCategoryExtension)

        registration.addRecipeCategories(
            // Machine - Basic
            HTAlloyingRecipeCategory(guiHelper),
            compressing,
            cutting,
            pressing,
            printing,
            // Machine - Heat
            melting,
            pyrolyzing,
            refining,
            // Machine - Cool
            freezing,
            // Machine - Chemical
            canning,
            HTMixingRecipeCategory(guiHelper),
            HTWashingRecipeCategory(guiHelper),
            // Device
            enchanting,
        )
    }

    private fun initItemToItem(guiHelper: IGuiHelper, manager: IIngredientManager) {
        compressing = HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.COMPRESSING)

        compressing.addExtension(HTBasicItemToItemRecipeCategoryExtension())
    }

    private fun initItemToChanced(guiHelper: IGuiHelper, manager: IIngredientManager) {
        cutting = HTItemToChancedRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CUTTING)

        cutting.addExtension(HTBasicItemToChancedRecipeCategoryExtension())
    }

    private fun initItemAndItem(guiHelper: IGuiHelper, manager: IIngredientManager) {
        pressing = HTItemAndItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PRESSING)
        printing = HTItemAndItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PRINTING)

        pressing.addExtension(HTBasicItemAndItemRecipeCategoryExtension())

        printing.addExtension(HTPrintingRecipeCategoryExtension)
        printing.addExtension(HTBookCopyingRecipeCategoryExtension)
    }

    private fun initItemOrFluid(guiHelper: IGuiHelper, manager: IIngredientManager) {
        melting = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CANNING)
        pyrolyzing = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.FREEZING)
        refining = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.REFINING)
        freezing = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MELTING)
        canning = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PYROLYZING)

        melting.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
        pyrolyzing.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
        refining.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
        freezing.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
        canning.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())

        canning.addExtension(HTDrainingRecipeCategoryExtension<HTBucketDrainingRecipe>(manager, HTBucketDrainingRecipe::isFilledBucket))
        canning.addExtension(HTBucketFillingRecipeCategoryExtension(manager))

        canning.addExtension(HTDrainingRecipeCategoryExtension<HTPotionDrainingRecipe>(manager, HTPotionDrainingRecipe::isPotion))
        canning.addExtension(HTPotionFillingRecipeCategoryExtension(manager))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Machine - Basic
        registration.addRecipes(RagiumJeiRecipeTypes.ALLOYING)
        registration.addRecipes(RagiumJeiRecipeTypes.COMPRESSING)
        registration.addRecipes(RagiumJeiRecipeTypes.CUTTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PRESSING)
        registration.addRecipes(RagiumJeiRecipeTypes.PRINTING)
        // Machine - Heat
        registration.addRecipes(RagiumJeiRecipeTypes.MELTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PYROLYZING)
        registration.addRecipes(RagiumJeiRecipeTypes.REFINING)
        // Machine - Cool
        registration.addRecipes(RagiumJeiRecipeTypes.FREEZING)
        // Machine - Chemical
        registration.addRecipes(RagiumJeiRecipeTypes.CANNING)
        registration.addRecipes(RagiumJeiRecipeTypes.MIXING)
        registration.addRecipes(RagiumJeiRecipeTypes.WASHING)
        // Device
        registration.addRecipes(RagiumJeiRecipeTypes.ENCHANTING)
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(RagiumBlocks.AUTO_CHISEL, RecipeTypes.STONECUTTING)
        registration.addRecipeCatalyst(RagiumBlocks.CRUSHER, getRecipeType(HCJeiRecipeTypes.CRUSHING))
        registration.addRecipeCatalyst(RagiumBlocks.ELECTRIC_FURNACE, RecipeTypes.SMELTING)

        registration.addRecipeCatalysts(
            // Machine - Basic
            RagiumJeiRecipeTypes.ALLOYING,
            RagiumJeiRecipeTypes.COMPRESSING,
            RagiumJeiRecipeTypes.CUTTING,
            RagiumJeiRecipeTypes.PRESSING,
            RagiumJeiRecipeTypes.PRINTING,
            // Machine - Heat
            RagiumJeiRecipeTypes.MELTING,
            RagiumJeiRecipeTypes.PYROLYZING,
            RagiumJeiRecipeTypes.REFINING,
            // Machine - Cool
            RagiumJeiRecipeTypes.FREEZING,
            // Machine - Chemical
            RagiumJeiRecipeTypes.CANNING,
            RagiumJeiRecipeTypes.MIXING,
            RagiumJeiRecipeTypes.WASHING,
            // Device
            RagiumJeiRecipeTypes.ENCHANTING,
            RagiumJeiRecipeTypes.PLANTING,
        )
    }
}
