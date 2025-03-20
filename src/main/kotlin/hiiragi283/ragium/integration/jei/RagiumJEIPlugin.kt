package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTRecipeType
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.integration.jei.category.HTCentrifugingRecipeCategory
import hiiragi283.ragium.integration.jei.category.HTItemProcessRecipeCategory
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
            HTItemProcessRecipeCategory(guiHelper, RagiumJEIRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER),
            HTItemProcessRecipeCategory(guiHelper, RagiumJEIRecipeTypes.EXTRACTING, RagiumBlocks.EXTRACTOR),
            HTItemProcessRecipeCategory(guiHelper, RagiumJEIRecipeTypes.FERMENTING, RagiumBlocks.EXTRACTOR),
        )
        for (category in categories) {
            registration.addRecipeCategories(category)
        }
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val recipeManager: RecipeManager = Minecraft.getInstance().level?.recipeManager ?: return

        fun <I : RecipeInput, R : Recipe<I>> register(recipeType: RecipeType<RecipeHolder<R>>, recipe: HTRecipeType<I, R>) {
            recipe.reloadCache(recipeManager)
            registration.addRecipes(
                recipeType,
                recipe.getAllRecipes(),
            )
        }

        register(RagiumJEIRecipeTypes.CENTRIFUGING, RagiumRecipes.CENTRIFUGING)
        register(RagiumJEIRecipeTypes.CRUSHING, RagiumRecipes.CRUSHING)
        register(RagiumJEIRecipeTypes.EXTRACTING, RagiumRecipes.EXTRACTING)
        register(RagiumJEIRecipeTypes.FERMENTING, RagiumRecipes.FERMENTING)
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(RagiumBlocks.CRUSHER, RagiumJEIRecipeTypes.CENTRIFUGING)
        registration.addRecipeCatalyst(RagiumBlocks.CRUSHER, RagiumJEIRecipeTypes.CRUSHING)
        registration.addRecipeCatalyst(RagiumBlocks.EXTRACTOR, RagiumJEIRecipeTypes.EXTRACTING)
        registration.addRecipeCatalyst(RagiumBlocks.EXTRACTOR, RagiumJEIRecipeTypes.FERMENTING)
    }
}
