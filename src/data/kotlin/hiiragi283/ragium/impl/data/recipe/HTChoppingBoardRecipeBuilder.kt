package hiiragi283.ragium.impl.data.recipe

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import kotlin.math.max

class HTChoppingBoardRecipeBuilder(stack: ImmutableItemStack) :
    HTStackRecipeBuilder.Single<HTChoppingBoardRecipeBuilder>("chopping", stack) {
    companion object {
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTChoppingBoardRecipeBuilder =
            HTChoppingBoardRecipeBuilder(ImmutableItemStack.of(item, count))
    }

    private val modelId: ResourceLocation = stack.getId()
    private var cutCount = 3

    fun setCutCount(cutCount: Int): HTChoppingBoardRecipeBuilder = apply {
        this.cutCount = max(cutCount, 1)
    }

    override fun createRecipe(output: ItemStack): ChoppingBoardRecipe = ChoppingBoardRecipe(ingredient, output, cutCount, modelId)
}
