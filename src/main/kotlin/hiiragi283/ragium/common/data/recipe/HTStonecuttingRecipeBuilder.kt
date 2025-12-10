package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.ItemLike

class HTStonecuttingRecipeBuilder(stack: ImmutableItemStack) :
    HTStackRecipeBuilder.Single<HTStonecuttingRecipeBuilder>("stonecutting", stack) {
    companion object {
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTStonecuttingRecipeBuilder =
            HTStonecuttingRecipeBuilder(ImmutableItemStack.of(item, count))
    }

    private var group: String? = null

    fun setGroup(group: String?): HTStonecuttingRecipeBuilder = apply {
        this.group = group
    }

    override fun createRecipe(output: ItemStack): StonecutterRecipe = StonecutterRecipe(
        group ?: "",
        ingredient,
        output,
    )
}
