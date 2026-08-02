package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTAlloyingRecipe(
    val primary: HTItemIngredient,
    val secondary: HTItemIngredient,
    val tertiary: HTItemIngredient?,
    val result: HTItemResult,
    override val progressData: HTProgressData,
) : HTRecipePredicates.TripleItem,
    HTRecipeFactories.TripleItem<ItemStack>,
    HTProgressRecipe.Simple<RecipeInput>,
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTAlloyingRecipe> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.listOf(2, 3).fieldOf(HTConst.INGREDIENT).forGetter { listOfNotNull(it.primary, it.secondary, it.tertiary) },
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAlloyingRecipe::result),
                    HTProgressData.CODEC.forGetter(HTAlloyingRecipe::progressData),
                ).apply(instance, ::HTAlloyingRecipe)
        }
    }

    constructor(ingredients: List<HTItemIngredient>, result: HTItemResult, progressData: HTProgressData) : this(
        ingredients[0],
        ingredients[1],
        ingredients.getOrNull(2),
        result,
        progressData,
    )

    override fun test(first: ItemStack, second: ItemStack, third: ItemStack): Boolean {
        if (!primary.test(first) || !secondary.test(second)) return false
        return tertiary?.test(third) ?: third.isEmpty
    }

    override fun getMatchingStacks(first: ItemStack, second: ItemStack, third: ItemStack): Triple<ItemStack, ItemStack, ItemStack> = Triple(
        primary.getMatchingStack(first),
        secondary.getMatchingStack(second),
        tertiary?.getMatchingStack(third) ?: ItemStack.EMPTY,
    )

    override fun assemble(firstInput: ItemStack, secondInput: ItemStack, thirdInput: ItemStack): ItemStack = result.createOrEmpty()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING

    override fun isIncomplete(): Boolean = primary.isIncomplete() || secondary.isIncomplete() || (tertiary?.isIncomplete() ?: false) || result.isIncomplete()
}
