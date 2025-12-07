package hiiragi283.ragium.api.recipe.multi

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.*

class HTRockGeneratingRecipe(
    val left: HTFluidIngredient,
    val right: Either<HTItemIngredient, HTFluidIngredient>,
    val bottom: Optional<HTItemIngredient>,
    val result: HTItemResult,
) : HTComplexRecipe {
    override fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int = when (index) {
        1 -> right.left().map { ingredient: HTItemIngredient -> ingredient.getRequiredAmount(stack) }
        else -> Optional.empty()
    }.orElse(0)

    override fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int = when (index) {
        0 -> left.getRequiredAmount(stack)
        1 -> right.right().map { ingredient: HTFluidIngredient -> ingredient.getRequiredAmount(stack) }.orElse(0)
        else -> 0
    }

    override fun assembleFluid(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? = null

    override fun test(input: HTMultiRecipeInput): Boolean {
        val bool1: Boolean = left.test(input.getFluid(0))
        val bool2: Boolean = right.map(
            { ingredient: HTItemIngredient -> ingredient.test(input.getItem(0)) },
            { ingredient: HTFluidIngredient -> ingredient.test(input.getFluid(1)) },
        )
        val bool3: Boolean = bottom.isEmpty || bottom.get().test(input.getItem(1))
        return bool1 && bool2 && bool3
    }

    override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, result)

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = left.hasNoMatchingStacks()
        val bool2: Boolean = right.map(HTItemIngredient::hasNoMatchingStacks, HTFluidIngredient::hasNoMatchingStacks)
        val bool3: Boolean = bottom.isPresent && bottom.get().hasNoMatchingStacks()
        return bool1 || bool2 || bool3
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumPlatform.INSTANCE.getRockGeneratingRecipeSerializer()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ROCK_GENERATING.get()
}
