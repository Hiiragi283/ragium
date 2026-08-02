package hiiragi283.ragium.support.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.support.data.recipe.HTDoubleItemToItemRecipeBuilder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

open class HTBasicDoubleItemToItemRecipe(
    val primary: HTItemIngredient,
    val secondary: HTItemIngredient,
    val consumeSecondary: Boolean,
    val result: HTItemResult,
    final override val progressData: HTProgressData,
) : HTDoubleItemToItemRecipe,
    HTProgressRecipe.Simple<RecipeInput> {
    companion object {
        @JvmStatic
        fun <RECIPE : HTBasicDoubleItemToItemRecipe> codec(factory: HTDoubleItemToItemRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.fieldOf(RagiumConst.PRIMARY).forGetter(HTBasicDoubleItemToItemRecipe::primary),
                HTItemIngredient.CODEC.fieldOf(RagiumConst.SECONDARY).forGetter(HTBasicDoubleItemToItemRecipe::secondary),
                HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicDoubleItemToItemRecipe::result),
                HTProgressData.CODEC.forGetter(HTBasicDoubleItemToItemRecipe::progressData),
            ).apply(instance, factory::create)
        }
    }

    final override fun test(first: ItemStack, second: ItemStack): Boolean = primary.test(first) && secondary.test(second)

    override fun getMatchingStacks(first: ItemStack, second: ItemStack): Pair<ItemStack, ItemStack> = Pair(
        primary.getMatchingStack(first),
        when (consumeSecondary) {
            true -> secondary.getMatchingStack(second)
            false -> ItemStack.EMPTY
        },
    )

    override fun isIncomplete(): Boolean = primary.isIncomplete() || secondary.isIncomplete() || result.isIncomplete()

    override fun assemble(firstInput: ItemStack, secondInput: ItemStack): ItemStack = result.createOrEmpty()
}
