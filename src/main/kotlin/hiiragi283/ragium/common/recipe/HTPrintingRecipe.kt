package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.ragium.support.recipe.base.HTBasicAssemblingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTPrintingRecipe(
    val ingredient: HTItemIngredient,
    val press: Ingredient,
    result: HTItemResult,
    progressData: HTProgressData,
) : HTBasicAssemblingRecipe(result, progressData) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTPrintingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTPrintingRecipe::ingredient),
                    HTCodecs.INGREDIENT.fieldOf("press").forGetter(HTPrintingRecipe::press),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTPrintingRecipe::result),
                    HTProgressData.CODEC.forGetter { it.progressData },
                ).apply(instance, ::HTPrintingRecipe)
        }
    }

    override fun test(first: ItemStack, second: ItemStack): Boolean = ingredient.test(first) && press.test(second)

    override fun getMatchingStacks(first: ItemStack, second: ItemStack): Pair<ItemStack, ItemStack> = ingredient.getMatchingStack(first) to ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PRINTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ASSEMBLING

    override fun isIncomplete(): Boolean = ingredient.isIncomplete() || press.hasNoItems() || result.isIncomplete()
}
