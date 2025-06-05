package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.data.recipe.HTDefinitionRecipeBuilder
import hiiragi283.ragium.api.extension.itemLookup
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.recipe.HTBeehiveRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTInfusingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition

abstract class HTRecipeProvider {
    protected lateinit var provider: HolderLookup.Provider
        private set
    protected lateinit var lookup: HolderGetter<Item>
        private set
    protected lateinit var output: RecipeOutput
        private set

    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        provider = holderLookup
        lookup = provider.itemLookup()
        this.output = output
        buildRecipeInternal()
    }

    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider, modid: String) {
        buildRecipes(output.withConditions(ModLoadedCondition(modid)), holderLookup)
    }

    protected abstract fun buildRecipeInternal()

    //    Extensions    //

    fun save(recipeId: ResourceLocation, recipe: Recipe<*>, vararg conditions: ICondition) {
        output.accept(recipeId, recipe, null, *conditions)
    }

    fun createBeehive(): HTDefinitionRecipeBuilder<HTBeehiveRecipe> = HTDefinitionRecipeBuilder("bee_hive", RagiumRecipeFactories::beehive)

    fun createCrushing(): HTDefinitionRecipeBuilder<HTCrushingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.CRUSHING, RagiumRecipeFactories::crushing)

    fun createExtracting(): HTDefinitionRecipeBuilder<HTExtractingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.EXTRACTING, RagiumRecipeFactories::extracting)

    fun createInfusing(): HTDefinitionRecipeBuilder<HTInfusingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.INFUSING, RagiumRecipeFactories::infusing)

    fun createRefining(): HTDefinitionRecipeBuilder<HTRefiningRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.REFINING, RagiumRecipeFactories::refining)

    fun createSolidifying(): HTDefinitionRecipeBuilder<HTSolidifyingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.SOLIDIFYING, RagiumRecipeFactories::solidifying)
}
