package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.impl.recipe.HTSawmillRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.ItemLike

class HTSingleItemRecipeBuilder<RECIPE : SingleItemRecipe>(
    prefix: String,
    private val factory: SingleItemRecipe.Factory<RECIPE>,
    stack: ImmutableItemStack,
) : HTStackRecipeBuilder.Single<HTSingleItemRecipeBuilder<RECIPE>>(prefix, stack) {
    companion object {
        @JvmStatic
        fun sawmill(item: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder<HTSawmillRecipe> =
            HTSingleItemRecipeBuilder(RagiumConst.SAWMILL, ::HTSawmillRecipe, ImmutableItemStack.of(item, count))

        @JvmStatic
        fun stonecutter(item: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder<StonecutterRecipe> =
            HTSingleItemRecipeBuilder("stonecutting", ::StonecutterRecipe, ImmutableItemStack.of(item, count))
    }

    private var group: String? = null

    override fun group(groupName: String?): HTSingleItemRecipeBuilder<RECIPE> = apply {
        this.group = groupName
    }

    override fun createRecipe(output: ItemStack): RECIPE = factory.create(
        group ?: "",
        ingredient,
        output,
    )
}
