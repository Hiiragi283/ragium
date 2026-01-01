package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.block.entity.processing.HTProcessorBlockEntity
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyBattery
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel

abstract class HTProcessingRecipeComponent<RECIPE : HTProcessingRecipe>(override val owner: HTProcessorBlockEntity) :
    HTRecipeComponent<RECIPE>(owner) {
    private val battery: HTMachineEnergyBattery.Processor get() = owner.battery

    final override fun getMaxProgress(recipe: RECIPE): Int = owner.updateAndGetProgress(recipe.time)

    final override fun getProgress(level: ServerLevel, pos: BlockPos): Int = battery.consume()

    abstract class Cached<RECIPE : HTProcessingRecipe>(
        private val cache: HTRecipeCache<HTRecipeInput, RECIPE>,
        owner: HTProcessorBlockEntity,
    ) : HTProcessingRecipeComponent<RECIPE>(owner) {
        constructor(
            finder: HTRecipeFinder<HTRecipeInput, RECIPE>,
            owner: HTProcessorBlockEntity,
        ) : this(HTFinderRecipeCache(finder), owner)

        final override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
