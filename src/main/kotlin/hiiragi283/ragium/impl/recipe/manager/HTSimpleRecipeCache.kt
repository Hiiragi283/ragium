package hiiragi283.ragium.impl.recipe.manager

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.recipeAccess
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTSimpleRecipeCache<I : RecipeInput, R : Recipe<I>>(val recipeType: RecipeType<R>) :
    HTRecipeCache<I, R>,
    HTValueSerializable {
    private var lastRecipe: ResourceLocation? = null

    private fun <R : Recipe<*>> updateCache(holder: HTRecipeHolder<R>?): HTRecipeHolder<R>? = holder.apply {
        lastRecipe = this?.id
    }

    override fun getFirstHolder(input: I, level: Level): HTRecipeHolder<R>? = level.recipeAccess
        .getRecipeFor(recipeType, input, level, lastRecipe)
        ?.let(::updateCache)

    override fun deserialize(input: HTValueInput) {
        lastRecipe = input.read(RagiumConst.LAST_RECIPE, VanillaBiCodecs.RL)
    }

    override fun serialize(output: HTValueOutput) {
        output.store(RagiumConst.LAST_RECIPE, VanillaBiCodecs.RL, lastRecipe)
    }
}
