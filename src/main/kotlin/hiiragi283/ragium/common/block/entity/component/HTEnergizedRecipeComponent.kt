package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyBattery
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel

abstract class HTEnergizedRecipeComponent<RECIPE : HTViewProcessingRecipe>(val owner: HTProcessorBlockEntity.Energized) :
    HTRecipeComponent<HTViewRecipeInput, RECIPE>(owner) {
    private val battery: HTMachineEnergyBattery.Processor get() = owner.battery

    final override fun getMaxProgress(recipe: RECIPE): Int = getTime(recipe).let(owner::updateAndGetProgress)

    open fun getTime(recipe: RECIPE): Int = recipe.time

    final override fun getProgress(level: ServerLevel, pos: BlockPos): Int = battery.consume()

    abstract class Cached<RECIPE : HTViewProcessingRecipe>(
        private val cache: HTRecipeCache<HTViewRecipeInput, RECIPE>,
        owner: HTProcessorBlockEntity.Energized,
    ) : HTEnergizedRecipeComponent<RECIPE>(owner) {
        constructor(
            finder: HTRecipeFinder<HTViewRecipeInput, RECIPE>,
            owner: HTProcessorBlockEntity.Energized,
        ) : this(HTFinderRecipeCache(finder), owner)

        final override fun getMatchedRecipe(input: HTViewRecipeInput, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
