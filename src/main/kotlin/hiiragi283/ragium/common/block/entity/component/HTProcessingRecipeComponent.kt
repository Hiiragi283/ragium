package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.common.block.entity.HTBlockEntity
import net.minecraft.world.item.crafting.RecipeInput
import java.util.function.ToIntFunction

abstract class HTProcessingRecipeComponent<INPUT : RecipeInput, RECIPE : Any>(
    owner: HTBlockEntity,
    private val timeGetter: ToIntFunction<RECIPE>,
) : HTRecipeComponent<INPUT, RECIPE>(owner) {
    final override fun getMaxProgress(recipe: RECIPE): Int = getTime(recipe).let(::modifyTime)

    protected open fun modifyTime(time: Int): Int = time

    fun getTime(recipe: RECIPE): Int = timeGetter.applyAsInt(recipe)
}
