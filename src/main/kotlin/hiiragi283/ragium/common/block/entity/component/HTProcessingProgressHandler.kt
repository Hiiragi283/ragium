package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import java.util.function.IntSupplier
import java.util.function.IntUnaryOperator
import java.util.function.ToIntFunction

class HTProcessingProgressHandler<RECIPE : Any>(
    private val timeGetter: ToIntFunction<RECIPE>,
    private val maxProgressModifier: IntUnaryOperator,
    private val progressGetter: IntSupplier,
) : HTRecipeHandler.ProgressHandler<RECIPE> {
    companion object {
        @JvmStatic
        fun <RECIPE : HTProcessingRecipe<*>> create(
            maxProgressModifier: IntUnaryOperator,
            progressGetter: IntSupplier,
        ): HTProcessingProgressHandler<RECIPE> =
            HTProcessingProgressHandler(HTProcessingRecipe<*>::time, maxProgressModifier, progressGetter)
    }

    override fun getMaxProgress(recipe: RECIPE): Int = recipe.let(timeGetter::applyAsInt).let(maxProgressModifier::applyAsInt)

    override fun getProgress(level: ServerLevel, pos: BlockPos): Int = progressGetter.asInt
}
