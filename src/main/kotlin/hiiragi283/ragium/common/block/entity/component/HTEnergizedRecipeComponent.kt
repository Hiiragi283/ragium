package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTEnergizedRecipeComponent<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(
    owner: HTProcessorBlockEntity.Energized,
) : HTRecipeComponent.Basic<INPUT, RECIPE>(
        owner,
        HTProcessingProgressHandler.create(owner::updateAndGetProgress) { owner.battery.consume() },
    ) {
    abstract class Cached<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(
        private val cache: HTRecipeCache<INPUT, RECIPE>,
        owner: HTProcessorBlockEntity.Energized,
    ) : HTEnergizedRecipeComponent<INPUT, RECIPE>(owner) {
        constructor(lookup: HTRecipeLookup<INPUT, RECIPE, *>, owner: HTProcessorBlockEntity.Energized) : this(lookup.createCache(), owner)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
