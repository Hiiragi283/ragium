package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipes
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import hiiragi283.ragium.integration.jei.category.HTCentrifugingRecipeCategory
import hiiragi283.ragium.integration.jei.category.HTSingleItemRecipeCategory
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager

@JeiPlugin
class RagiumJEIPlugin : IModPlugin {
    override fun getPluginUid(): ResourceLocation = RagiumAPI.id("plugin")

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val jeiHelper: IJeiHelpers = registration.jeiHelpers
        val guiHelper: IGuiHelper = jeiHelper.guiHelper

        val categories = listOf(
            HTCentrifugingRecipeCategory(guiHelper),
            HTSingleItemRecipeCategory(guiHelper, RagiumJEIRecipeTypes.CRUSHING, Items.CRAFTING_TABLE),
            HTSingleItemRecipeCategory(guiHelper, RagiumJEIRecipeTypes.EXTRACTING, Items.CRAFTING_TABLE),
        )
        for (category in categories) {
            registration.addRecipeCategories(category)
        }
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val recipeManager: RecipeManager = Minecraft.getInstance().level?.recipeManager ?: return

        fun <I : RecipeInput, R : Recipe<I>> register(recipeType: RecipeType<RecipeHolder<R>>, recipe: HTDeferredRecipeType<I, R>) {
            recipe.reloadCache(recipeManager)
            registration.addRecipes(
                recipeType,
                recipe.getAllRecipes(),
            )
        }

        register(RagiumJEIRecipeTypes.CENTRIFUGING, RagiumRecipes.CENTRIFUGING)
        register(RagiumJEIRecipeTypes.CRUSHING, RagiumRecipes.CRUSHING)

        register(RagiumJEIRecipeTypes.EXTRACTING, RagiumRecipes.EXTRACTING)
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(Items.CRAFTING_TABLE, RagiumJEIRecipeTypes.CENTRIFUGING)
        registration.addRecipeCatalyst(Items.CRAFTING_TABLE, RagiumJEIRecipeTypes.CRUSHING)
        registration.addRecipeCatalyst(Items.CRAFTING_TABLE, RagiumJEIRecipeTypes.EXTRACTING)
    }
}
