package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.integration.jei.category.*
import hiiragi283.ragium.integration.jei.entry.HTDeviceOutputEntry
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
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod

@Suppress("unused")
@JeiPlugin
class RagiumJEIPlugin : IModPlugin {
    override fun getPluginUid(): ResourceLocation = RagiumAPI.id("plugin")

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val jeiHelper: IJeiHelpers = registration.jeiHelpers
        val guiHelper: IGuiHelper = jeiHelper.guiHelper

        val categories: List<HTRecipeCategory<*>> = listOf(
            HTCentrifugingRecipeCategory(guiHelper),
            HTDeviceOutputCategory(guiHelper),
            HTFluidProcessRecipeCategory(guiHelper, RagiumJEIRecipeTypes.REFINING, Items.BARRIER),
            HTInfusingRecipeCategory(guiHelper),
            HTItemProcessRecipeCategory(guiHelper, RagiumJEIRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER),
            HTItemProcessRecipeCategory(guiHelper, RagiumJEIRecipeTypes.EXTRACTING, RagiumBlocks.EXTRACTOR),
        )
        for (category: HTRecipeCategory<*> in categories) {
            registration.addRecipeCategories(category)
        }
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val recipeManager: RecipeManager = Minecraft.getInstance().level?.recipeManager ?: return

        fun register(recipeType: RecipeType<RecipeHolder<HTMachineRecipe>>, recipe: HTMachineRecipeType) {
            recipe.reloadCache(recipeManager)
            registration.addRecipes(
                recipeType,
                recipe.getAllRecipes(),
            )
        }

        register(RagiumJEIRecipeTypes.CENTRIFUGING, RagiumRecipes.CENTRIFUGING)
        register(RagiumJEIRecipeTypes.CRUSHING, RagiumRecipes.CRUSHING)
        register(RagiumJEIRecipeTypes.EXTRACTING, RagiumRecipes.EXTRACTING)
        register(RagiumJEIRecipeTypes.INFUSING, RagiumRecipes.INFUSING)
        register(RagiumJEIRecipeTypes.REFINING, RagiumRecipes.REFINING)

        registration.addRecipes(
            RagiumJEIRecipeTypes.DEVICE,
            buildList {
                // Water Well
                add(
                    HTDeviceOutputEntry(
                        RagiumAPI.id("water_well"),
                        Ingredient.of(RagiumBlocks.WATER_WELL),
                        HTFluidOutput.of(Fluids.WATER, 100),
                    ),
                )
                // Lava Well
                add(
                    HTDeviceOutputEntry(
                        RagiumAPI.id("lava_well"),
                        Ingredient.of(RagiumBlocks.LAVA_WELL),
                        HTFluidOutput.of(Fluids.LAVA, 10),
                    ),
                )
                // Milk Drain
                add(
                    HTDeviceOutputEntry(
                        RagiumAPI.id("milk_drain"),
                        Ingredient.of(RagiumBlocks.MILK_DRAIN),
                        HTFluidOutput.of(NeoForgeMod.MILK, 1000),
                    ),
                )
            },
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(RagiumBlocks.CRUSHER, RagiumJEIRecipeTypes.CRUSHING)
        registration.addRecipeCatalyst(RagiumBlocks.EXTRACTOR, RagiumJEIRecipeTypes.EXTRACTING)

        registration.addRecipeCatalyst(Items.BARRIER, RagiumJEIRecipeTypes.CENTRIFUGING)
        registration.addRecipeCatalyst(Items.BARRIER, RagiumJEIRecipeTypes.INFUSING)
        registration.addRecipeCatalyst(Items.BARRIER, RagiumJEIRecipeTypes.REFINING)
    }
}
