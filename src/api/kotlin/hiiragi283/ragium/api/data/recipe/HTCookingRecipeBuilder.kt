package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.ItemLike
import java.util.function.IntUnaryOperator
import kotlin.math.max

class HTCookingRecipeBuilder private constructor(
    private val factory: AbstractCookingRecipe.Factory<AbstractCookingRecipe>,
    private val timeModifier: IntUnaryOperator,
    private val result: ItemStack,
) : HTIngredientRecipeBuilder<HTCookingRecipeBuilder, AbstractCookingRecipe> {
    companion object {
        @JvmStatic
        fun smelting(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder =
            HTCookingRecipeBuilder(::SmeltingRecipe, IntUnaryOperator.identity(), ItemStack(item, count))

        @JvmStatic
        fun blasting(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder =
            HTCookingRecipeBuilder(::BlastingRecipe, { it / 2 }, ItemStack(item, count))

        @JvmStatic
        fun smoking(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder =
            HTCookingRecipeBuilder(::SmokingRecipe, { it / 2 }, ItemStack(item, count))
    }

    private var group: String? = null
    private lateinit var ingredient: Ingredient
    private var time: Int = 200
    private var exp: Float = 0f

    override fun addIngredient(ingredient: Ingredient): HTCookingRecipeBuilder = apply {
        check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
        this.ingredient = ingredient
    }

    fun setTime(time: Int): HTCookingRecipeBuilder = apply {
        this.time = max(0, time)
    }

    fun setExp(exp: Float): HTCookingRecipeBuilder = apply {
        this.exp = max(0f, exp)
    }

    override fun group(groupName: String?): HTCookingRecipeBuilder = apply {
        this.group = groupName
    }

    override fun getPrimalId(): ResourceLocation = result.itemHolder.idOrThrow

    override fun getPrefix(recipe: AbstractCookingRecipe): String = when (recipe) {
        is SmeltingRecipe -> "smelting/"
        is BlastingRecipe -> "blasting/"
        is SmokingRecipe -> "smoking/"
        else -> throw IllegalStateException("Unsupported recipe class: ${recipe.javaClass.canonicalName}")
    }

    override fun createRecipe(): AbstractCookingRecipe = factory.create(
        group ?: "",
        CookingBookCategory.MISC,
        ingredient,
        result,
        exp,
        timeModifier.applyAsInt(time),
    )
}
