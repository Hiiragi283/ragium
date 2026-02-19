package hiiragi283.ragium.client.jei

import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.jei.category.HTAlloyingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTDistillingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemOrFluidRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemToChancedRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemToItemRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMixingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTPressingRecipeCategory
import hiiragi283.ragium.common.recipe.HTCanningRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder

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
            HTAlloyingRecipeCategory(guiHelper),
            HTItemToItemRecipeCategory.bending(guiHelper),
            HTItemToItemRecipeCategory.compressing(guiHelper),
            HTItemToChancedRecipeCategory.crushing(guiHelper),
            HTItemToChancedRecipeCategory.cutting(guiHelper),
            HTItemToItemRecipeCategory.lathing(guiHelper),
            HTPressingRecipeCategory(guiHelper),
            HTItemToItemRecipeCategory.wiring(guiHelper),
            // Machine - Heat
            HTDistillingRecipeCategory(guiHelper),
            HTItemOrFluidRecipeCategory.melting(guiHelper),
            HTItemOrFluidRecipeCategory.pyrolyzing(guiHelper),
            // Machine - Cool
            HTItemOrFluidRecipeCategory.freezing(guiHelper),
            // Machine - Chemical
            HTItemOrFluidRecipeCategory.canning(guiHelper),
            HTMixingRecipeCategory(guiHelper),
            // Device
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Machine - Basic
        registration.addRecipes(RagiumJeiRecipeTypes.ALLOYING)
        registration.addRecipes(RagiumJeiRecipeTypes.BENDING)
        registration.addRecipes(RagiumJeiRecipeTypes.COMPRESSING)
        registration.addRecipes(RagiumJeiRecipeTypes.CRUSHING)
        registration.addRecipes(RagiumJeiRecipeTypes.CUTTING)
        registration.addRecipes(RagiumJeiRecipeTypes.LATHING)
        registration.addRecipes(RagiumJeiRecipeTypes.PRESSING)
        registration.addRecipes(RagiumJeiRecipeTypes.WIRING)
        // Machine - Heat
        registration.addRecipes(RagiumJeiRecipeTypes.DISTILLING)
        registration.addRecipes(RagiumJeiRecipeTypes.MELTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PYROLYZING)
        // Machine - Cool
        registration.addRecipes(RagiumJeiRecipeTypes.FREEZING)
        // Machine - Chemical
        registration.addRecipes(RagiumJeiRecipeTypes.CANNING)
        registration.addRecipes(
            getRecipeType(RagiumJeiRecipeTypes.CANNING),
            HTVanillaRecipeTypes.BREWING.getAllRecipes().map { holder: RecipeHolder<HCBrewingRecipe> ->
                val recipe: HCBrewingRecipe = holder.value
                RecipeHolder(
                    holder.id,
                    HTCanningRecipe(Ior.Both(recipe.ingredient, recipe.potionFrom), Ior.Right(recipe.potionTo), recipe.parameters),
                )
            },
        )
        registration.addRecipes(RagiumJeiRecipeTypes.MIXING)
        // Device
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalysts(
            // Machine - Basic
            RagiumJeiRecipeTypes.ALLOYING,
            RagiumJeiRecipeTypes.BENDING,
            RagiumJeiRecipeTypes.COMPRESSING,
            RagiumJeiRecipeTypes.CRUSHING,
            RagiumJeiRecipeTypes.CUTTING,
            RagiumJeiRecipeTypes.LATHING,
            RagiumJeiRecipeTypes.PRESSING,
            RagiumJeiRecipeTypes.WIRING,
            // Machine - Heat
            RagiumJeiRecipeTypes.DISTILLING,
            RagiumJeiRecipeTypes.MELTING,
            RagiumJeiRecipeTypes.PYROLYZING,
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
