package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.HTBasicAssemblingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTAssemblingRecipe(
    val primary: HTItemIngredient,
    val secondary: HTItemIngredient,
    result: HTItemResult,
    progressData: HTProgressData,
) : HTBasicAssemblingRecipe(result, progressData) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTAssemblingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC
                        .listOf(2, 2)
                        .fieldOf(HTConst.INGREDIENT)
                        .forGetter { listOf(it.primary, it.secondary) },
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAssemblingRecipe::result),
                    HTProgressData.CODEC.forGetter { it.progressData },
                ).apply(instance, ::HTAssemblingRecipe)
        }
    }

    constructor(ingredients: List<HTItemIngredient>, result: HTItemResult, progressData: HTProgressData) : this(
        ingredients[0],
        ingredients[1],
        result,
        progressData,
    )

    override fun test(first: ItemStack, second: ItemStack): Boolean = primary.test(first) && secondary.test(second)

    override fun getRequiredAmount(first: ItemStack, second: ItemStack): Pair<Int, Int> = primary.getRequiredAmount(first) to secondary.getRequiredAmount(second)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ASSEMBLING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ASSEMBLING
}
