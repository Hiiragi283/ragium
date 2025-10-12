package hiiragi283.ragium.impl.recipe.manager

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeFinder
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

class HTSimpleRecipeCache<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val recipeGetter: HTRecipeFinder<INPUT, RECIPE>) :
    HTRecipeCache<INPUT, RECIPE>,
    HTValueSerializable {
    private var lastRecipe: ResourceLocation? = null

    override fun getFirstHolder(input: INPUT, level: Level): RecipeHolder<RECIPE>? =
        recipeGetter.getRecipeFor(level.recipeManager, input, level, lastRecipe).also { holder ->
            lastRecipe = holder?.id
        }

    override fun deserialize(input: HTValueInput) {
        lastRecipe = input.read(RagiumConst.LAST_RECIPE, VanillaBiCodecs.RL)
    }

    override fun serialize(output: HTValueOutput) {
        output.store(RagiumConst.LAST_RECIPE, VanillaBiCodecs.RL, lastRecipe)
    }
}
