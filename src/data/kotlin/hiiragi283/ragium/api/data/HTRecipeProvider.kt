package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.extension.itemLookup
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition

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

    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider, mod: IntegrationMods) {
        buildRecipes(output.withConditions(mod.condition), holderLookup)
    }

    protected abstract fun buildRecipeInternal()

    //    Extensions    //

    fun save(recipeId: ResourceLocation, recipe: Recipe<*>, vararg conditions: ICondition) {
        output.accept(recipeId, recipe, null, *conditions)
    }
}
