package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyBattery
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import java.util.function.ToIntFunction

abstract class HTEnergizedRecipeComponent<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
    val owner: HTProcessorBlockEntity.Energized,
    timeGetter: ToIntFunction<RECIPE>,
) : HTProcessingRecipeComponent<INPUT, RECIPE>(owner, timeGetter) {
    private val battery: HTMachineEnergyBattery.Processor get() = owner.battery

    final override fun modifyTime(time: Int): Int = owner.updateAndGetProgress(time)

    final override fun getProgress(level: ServerLevel, pos: BlockPos): Int = battery.consume()

    abstract class Processing<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(owner: HTProcessorBlockEntity.Energized) :
        HTEnergizedRecipeComponent<INPUT, RECIPE>(owner, HTProcessingRecipe<*>::time)

    abstract class ProcessingCached<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(
        private val cache: HTRecipeCache<INPUT, RECIPE>,
        owner: HTProcessorBlockEntity.Energized,
    ) : Processing<INPUT, RECIPE>(owner) {
        constructor(lookup: HTRecipeLookup<INPUT, RECIPE>, owner: HTProcessorBlockEntity.Energized) : this(lookup.createCache(), owner)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
