package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTImplodingRecipe(val ingredient: HTItemIngredient, val result: HTItemResult, override val progressData: HTProgressData) :
    HTDoubleItemToItemRecipe,
    HTProgressRecipe.Simple<RecipeInput>,
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTImplodingRecipe> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTImplodingRecipe::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTImplodingRecipe::result),
                    HTProgressData.CODEC.forGetter(HTImplodingRecipe::progressData),
                ).apply(instance, ::HTImplodingRecipe)
        }

        @JvmField
        val EXPLOSIVE_AMOUNTS: Map<TagKey<Item>, Int> = mapOf(
            RagiumTags.Items.EXPLOSIVES.elite to 1,
            RagiumTags.Items.EXPLOSIVES.advanced to 2,
            RagiumTags.Items.EXPLOSIVES.basic to 4,
        )
    }

    override fun test(first: ItemStack, second: ItemStack): Boolean = ingredient.test(first) && testExplosive(second)

    private fun testExplosive(stack: ItemStack): Boolean {
        for ((tagKey: TagKey<Item>, amount: Int) in EXPLOSIVE_AMOUNTS) {
            if (stack.`is`(tagKey) && stack.count >= amount) return true
        }
        return false
    }

    override fun getMatchingStacks(first: ItemStack, second: ItemStack): Pair<ItemStack, ItemStack> = Pair(
        ingredient.getMatchingStack(first),
        second.copyWithCount(
            EXPLOSIVE_AMOUNTS.entries
                .firstOrNull { (tagKey: TagKey<Item>, amount: Int) -> second.`is`(tagKey) && second.count >= amount }
                ?.value
                ?: 0,
        ),
    )

    override fun apply(first: ItemStack, second: ItemStack): ItemStack = result.createOrEmpty()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.IMPLODING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.IMPLODING

    override fun isIncomplete(): Boolean = ingredient.isIncomplete() || result.isIncomplete()
}
