package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.block.entity.processing.HTProcessorBlockEntity
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyBattery
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTProcessingRecipeComponent<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(
    override val owner: HTProcessorBlockEntity,
) : HTRecipeComponent<INPUT, RECIPE>(owner) {
    private val battery: HTMachineEnergyBattery.Processor get() = owner.battery

    final override fun getMaxProgress(recipe: RECIPE): Int = owner.updateAndGetProgress(recipe.time)

    final override fun getProgress(level: ServerLevel, pos: BlockPos): Int = battery.consume()

    abstract class Cached<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(
        private val cache: HTRecipeCache<INPUT, RECIPE>,
        owner: HTProcessorBlockEntity,
    ) : HTProcessingRecipeComponent<INPUT, RECIPE>(owner) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            owner: HTProcessorBlockEntity,
        ) : this(HTFinderRecipeCache(finder), owner)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
