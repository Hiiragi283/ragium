package hiiragi283.ragium.impl.data.recipe

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe
import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.util.wrapOptional
import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult
import kotlin.math.max

sealed class HTCookingBoardRecipeBuilder<BUILDER : HTCookingBoardRecipeBuilder<BUILDER>>(
    prefix: String,
    stack: ImmutableItemStack,
    chance: Float,
) : HTStackRecipeBuilder<BUILDER>(prefix, stack),
    HTIngredientRecipeBuilder<BUILDER> {
    companion object {
        @JvmStatic
        fun chopping(stack: ItemStack): Chopping = Chopping(ImmutableItemStack.of(stack))

        @JvmStatic
        fun chopping(item: ItemLike, count: Int = 1): Chopping = chopping(ItemStack(item, count))

        @JvmStatic
        fun cutting(stack: ItemStack, chance: Float = 1f): Cutting = Cutting(ImmutableItemStack.of(stack), chance)

        @JvmStatic
        fun cutting(item: ItemLike, count: Int = 1, chance: Float = 1f): Cutting = cutting(ItemStack(item, count), chance)
    }

    protected val results: MutableList<Pair<ImmutableItemStack, Float>> = mutableListOf(stack to chance)
    protected val ingredients: MutableList<Ingredient> = mutableListOf()

    //    Chopping    //

    class Chopping(stack: ImmutableItemStack) : HTCookingBoardRecipeBuilder<Chopping>("chopping", stack, 1f) {
        private val modelId: ResourceLocation = stack.getId()
        private var cutCount = 3

        fun setCutCount(cutCount: Int): Chopping = apply {
            this.cutCount = max(cutCount, 1)
        }

        override fun addIngredient(ingredient: Ingredient): Chopping = apply {
            check(ingredients.size <= 1) { "Ingredient has already been initialized!" }
            ingredients.add(ingredient)
        }

        override fun createRecipe(output: ItemStack): Recipe<*> = ChoppingBoardRecipe(
            ingredients[0],
            output,
            cutCount,
            modelId,
        )
    }

    //    Cutting    //

    class Cutting(stack: ImmutableItemStack, chance: Float) : HTCookingBoardRecipeBuilder<Cutting>("cutting", stack, chance) {
        private var group: String? = null
        private var sound: SoundEvent? = null

        fun addResult(item: ItemLike, count: Int = 1, chance: Float = 1f): Cutting = addResult(ItemStack(item, count), chance)

        fun addResult(stack: ItemStack, chance: Float = 1f): Cutting = apply {
            this.results.add(ImmutableItemStack.of(stack) to chance)
        }

        fun setSound(sound: SoundEvent): Cutting = apply {
            this.sound = sound
        }

        override fun group(groupName: String?): Cutting = apply {
            this.group = groupName
        }

        override fun addIngredient(ingredient: Ingredient): Cutting = apply {
            check(ingredients.size <= 2) { "Ingredient has already been initialized!" }
            ingredients.add(ingredient)
        }

        override fun createRecipe(output: ItemStack): Recipe<*> = CuttingBoardRecipe(
            group ?: "",
            ingredients[0],
            ingredients[1],
            results
                .map { (stack: ImmutableItemStack, chance: Float) -> ChanceResult(stack.stack, chance) }
                .let(NonNullList<ChanceResult>::copyOf),
            sound.wrapOptional(),
        )
    }
}
