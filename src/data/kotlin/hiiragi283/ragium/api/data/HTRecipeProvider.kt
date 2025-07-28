package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTDefinitionRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.extension.itemLookup
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.IConditionBuilder

abstract class HTRecipeProvider : IConditionBuilder {
    protected lateinit var provider: HolderLookup.Provider
        private set
    protected lateinit var lookup: HolderGetter<Item>
        private set
    protected lateinit var output: RecipeOutput
        private set

    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        provider = holderLookup
        lookup = provider.itemLookup()
        this.output = object : RecipeOutput {
            override fun accept(
                id: ResourceLocation,
                recipe: Recipe<*>,
                advancement: AdvancementHolder?,
                vararg conditions: ICondition,
            ) {
                val fixedId: ResourceLocation = when (id.namespace) {
                    RagiumAPI.MOD_ID -> id
                    RagiumConstantValues.COMMON -> RagiumAPI.id(id.path)
                    RagiumConstantValues.MINECRAFT -> RagiumAPI.id(id.path)
                    else -> {
                        val path: List<String> = id.path.split("/", limit = 2)
                        RagiumAPI.id(path[0] + "/${id.namespace}/" + path[1])
                    }
                }
                output.accept(fixedId, recipe, advancement, *conditions)
            }

            override fun advancement(): Advancement.Builder = output.advancement()
        }
        buildRecipeInternal()
    }

    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider, modid: String) {
        buildRecipes(output.withConditions(modLoaded(modid)), holderLookup)
    }

    protected abstract fun buildRecipeInternal()

    //    Extensions    //

    fun save(recipeId: ResourceLocation, recipe: Recipe<*>, vararg conditions: ICondition) {
        output.accept(recipeId, recipe, null, *conditions)
    }

    fun createAlloying(): HTDefinitionRecipeBuilder<HTAlloyingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.ALLOYING, RagiumRecipeFactories::alloying)

    fun createCrushing(): HTDefinitionRecipeBuilder<HTCrushingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.CRUSHING, RagiumRecipeFactories::crushing)

    fun createExtracting(): HTDefinitionRecipeBuilder<HTExtractingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.EXTRACTING, RagiumRecipeFactories::extracting)

    fun createMelting(): HTDefinitionRecipeBuilder<HTMeltingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.MELTING, RagiumRecipeFactories::melting)

    fun createPressing(): HTDefinitionRecipeBuilder<HTPressingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.PRESSING, RagiumRecipeFactories::pressing)

    fun createRefining(): HTDefinitionRecipeBuilder<HTRefiningRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.REFINING, RagiumRecipeFactories::refining)

    fun createSolidifying(): HTDefinitionRecipeBuilder<HTSolidifyingRecipe> =
        HTDefinitionRecipeBuilder(RagiumConstantValues.SOLIDIFYING, RagiumRecipeFactories::solidifying)

    fun createNetheriteUpgrade(output: ItemLike, input: ItemLike): HTSmithingRecipeBuilder = HTSmithingRecipeBuilder(output)
        .addIngredient(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
        .addIngredient(input)
        .addIngredient(Tags.Items.INGOTS_NETHERITE)
}
