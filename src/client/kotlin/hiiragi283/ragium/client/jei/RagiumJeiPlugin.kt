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
import hiiragi283.ragium.client.jei.category.HTFreezingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemAndItemRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemOrFluidRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMeltingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMixingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTWashingRecipeCategory
import hiiragi283.ragium.client.jei.category.RagiumDuplicatingRecipeCategory
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

        @JvmStatic
        lateinit var planting: HTItemToChancedRecipeCategory
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
        lateinit var pyrolyzing: HTItemOrFluidRecipeCategory
            private set

        @JvmStatic
        lateinit var refining: HTItemOrFluidRecipeCategory
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
            RagiumItems.BLUEPRINT.get(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(RagiumDataComponents.BLUEPRINT_NUMBER) },
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
            planting,
            pressing,
            printing,
            // Machine - Advanced
            HTFreezingRecipeCategory(guiHelper),
            HTMeltingRecipeCategory(guiHelper),
            pyrolyzing,
            refining,
            // Machine - Elite
            canning,
            HTMixingRecipeCategory(guiHelper),
            HTWashingRecipeCategory(guiHelper),
            // Machine - Ultimate
            enchanting,
            RagiumDuplicatingRecipeCategory(guiHelper),
            // Device
        )
    }

    private fun initItemToItem(guiHelper: IGuiHelper, manager: IIngredientManager) {
        compressing = HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.COMPRESSING)

        compressing.addExtension(HTBasicItemToItemRecipeCategoryExtension())
    }

    private fun initItemToChanced(guiHelper: IGuiHelper, manager: IIngredientManager) {
        cutting = HTItemToChancedRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CUTTING)
        planting = HTItemToChancedRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PLANTING)

        cutting.addExtension(HTBasicItemToChancedRecipeCategoryExtension())
        planting.addExtension(HTBasicItemToChancedRecipeCategoryExtension())
    }

    private fun initItemAndItem(guiHelper: IGuiHelper, manager: IIngredientManager) {
        pressing = HTItemAndItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PRESSING)
        printing = HTItemAndItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PRINTING)

        pressing.addExtension(HTBasicItemAndItemRecipeCategoryExtension())

        printing.addExtension(HTPrintingRecipeCategoryExtension)
        printing.addExtension(HTBookCopyingRecipeCategoryExtension)
    }

    private fun initItemOrFluid(guiHelper: IGuiHelper, manager: IIngredientManager) {
        pyrolyzing = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PYROLYZING)
        refining = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.REFINING)
        canning = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CANNING)

        pyrolyzing.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
        refining.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
        canning.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())

        HTDrainingRecipeCategoryExtension<HTBucketDrainingRecipe>(manager, 1000, HTBucketDrainingRecipe::isFilledBucket)
            .let(canning::addExtension)
        HTBucketFillingRecipeCategoryExtension(manager)
            .let(canning::addExtension)

        HTDrainingRecipeCategoryExtension<HTPotionDrainingRecipe>(manager, 250, HTPotionDrainingRecipe::isPotion)
            .let(canning::addExtension)
        HTPotionFillingRecipeCategoryExtension(manager)
            .let(canning::addExtension)
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Machine - Basic
        registration.addRecipes(RagiumJeiRecipeTypes.ALLOYING)
        registration.addRecipes(RagiumJeiRecipeTypes.COMPRESSING)
        registration.addRecipes(RagiumJeiRecipeTypes.CUTTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PLANTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PRESSING)
        registration.addRecipes(RagiumJeiRecipeTypes.PRINTING)
        // Machine - Advanced
        registration.addRecipes(RagiumJeiRecipeTypes.FREEZING)
        registration.addRecipes(RagiumJeiRecipeTypes.MELTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PYROLYZING)
        registration.addRecipes(RagiumJeiRecipeTypes.REFINING)
        // Machine - Elite
        registration.addRecipes(RagiumJeiRecipeTypes.CANNING)
        registration.addRecipes(RagiumJeiRecipeTypes.MIXING)
        registration.addRecipes(RagiumJeiRecipeTypes.WASHING)
        // Machine - Ultimate
        registration.addRecipes(RagiumJeiRecipeTypes.DUPLICATING)
        registration.addRecipes(RagiumJeiRecipeTypes.ENCHANTING)
        // Device
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(RagiumBlocks.AUTO_CHISEL, RecipeTypes.STONECUTTING)
        registration.addRecipeCatalyst(RagiumBlocks.BREWERY, getRecipeType(HCJeiRecipeTypes.BREWING))
        registration.addRecipeCatalyst(RagiumBlocks.CRUSHER, getRecipeType(HCJeiRecipeTypes.CRUSHING))
        registration.addRecipeCatalyst(RagiumBlocks.ELECTRIC_FURNACE, RecipeTypes.SMELTING)

        registration.addRecipeCatalysts(
            // Machine - Basic
            RagiumJeiRecipeTypes.ALLOYING,
            RagiumJeiRecipeTypes.COMPRESSING,
            RagiumJeiRecipeTypes.CUTTING,
            RagiumJeiRecipeTypes.PLANTING,
            RagiumJeiRecipeTypes.PRESSING,
            RagiumJeiRecipeTypes.PRINTING,
            // Machine - Advanced
            RagiumJeiRecipeTypes.FREEZING,
            RagiumJeiRecipeTypes.MELTING,
            RagiumJeiRecipeTypes.PYROLYZING,
            RagiumJeiRecipeTypes.REFINING,
            // Machine - Elite
            RagiumJeiRecipeTypes.CANNING,
            RagiumJeiRecipeTypes.MIXING,
            RagiumJeiRecipeTypes.WASHING,
            // Machine - Ultimate
            RagiumJeiRecipeTypes.ENCHANTING,
            RagiumJeiRecipeTypes.DUPLICATING,
            // Device
        )
    }
}
