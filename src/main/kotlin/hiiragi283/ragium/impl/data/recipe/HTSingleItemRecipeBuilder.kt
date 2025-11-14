package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.ItemLike

class HTSingleItemRecipeBuilder(prefix: String, private val factory: SingleItemRecipe.Factory<*>, stack: ImmutableItemStack) :
    HTStackRecipeBuilder.Single<HTSingleItemRecipeBuilder>(prefix, stack) {
    companion object {
        @JvmStatic
        fun stonecutter(item: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder("stonecutting", ::StonecutterRecipe, ImmutableItemStack.of(item, count))
    }

    private var group: String? = null

    override fun group(groupName: String?): HTSingleItemRecipeBuilder = apply {
        this.group = groupName
    }

    override fun createRecipe(output: ItemStack): SingleItemRecipe = factory.create(
        group ?: "",
        ingredient,
        output,
    )
}
