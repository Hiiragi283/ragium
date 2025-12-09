package hiiragi283.ragium.api.recipe.multi

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
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

    override fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? = null

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = result.getStackOrNull(provider)

    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val bool1: Boolean = input.testFluid(0, left)
        val bool2: Boolean = right.map(
            { ingredient: HTItemIngredient -> input.testItem(0, ingredient) },
            { ingredient: HTFluidIngredient -> input.testFluid(1, ingredient) },
        )
        val bool3: Boolean = input.testCatalyst(1, bottom)
        return bool1 && bool2 && bool3
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumPlatform.INSTANCE.getRockGeneratingRecipeSerializer()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ROCK_GENERATING.get()
}
