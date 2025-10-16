package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.storage.item.ImmutableItemStack
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

class HTCookingRecipeBuilder<RECIPE : AbstractCookingRecipe>(
    prefix: String,
    private val factory: AbstractCookingRecipe.Factory<RECIPE>,
    private val timeOperator: IntUnaryOperator,
    stack: ImmutableItemStack,
) : HTStackRecipeBuilder<HTCookingRecipeBuilder<RECIPE>>(prefix, stack),
    HTIngredientRecipeBuilder<HTCookingRecipeBuilder<RECIPE>> {
    companion object {
        @JvmStatic
        fun smelting(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder<SmeltingRecipe> = HTCookingRecipeBuilder(
            "smelting",
            ::SmeltingRecipe,
            IntUnaryOperator.identity(),
            ImmutableItemStack.of(item, count),
        )

        @JvmStatic
        fun blasting(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder<BlastingRecipe> = HTCookingRecipeBuilder(
            "blasting",
            ::BlastingRecipe,
            { it / 2 },
            ImmutableItemStack.of(item, count),
        )

        @JvmStatic
        fun smoking(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder<SmokingRecipe> = HTCookingRecipeBuilder(
            "smoking",
            ::SmokingRecipe,
            { it / 2 },
            ImmutableItemStack.of(item, count),
        )

        @JvmStatic
        fun smeltingAndBlasting(item: ItemLike, count: Int = 1, builderAction: HTCookingRecipeBuilder<*>.() -> Unit) {
            smelting(item, count).apply(builderAction)
            blasting(item, count).apply(builderAction)
        }

        @JvmStatic
        fun smeltingAndSmoking(item: ItemLike, count: Int = 1, builderAction: HTCookingRecipeBuilder<*>.() -> Unit) {
            smelting(item, count).apply(builderAction)
            smoking(item, count).apply(builderAction)
        }
    }

    private var group: String? = null
    private lateinit var ingredient: Ingredient
    private var time: Int = 200
    private var exp: Float = 0f

    override fun addIngredient(ingredient: Ingredient): HTCookingRecipeBuilder<RECIPE> = apply {
        check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
        this.ingredient = ingredient
    }

    fun setTime(time: Int): HTCookingRecipeBuilder<RECIPE> = apply {
        this.time = max(0, time)
    }

    fun setExp(exp: Float): HTCookingRecipeBuilder<RECIPE> = apply {
        this.exp = max(0f, exp)
    }

    override fun group(groupName: String?): HTCookingRecipeBuilder<RECIPE> = apply {
        this.group = groupName
    }

    override fun createRecipe(output: ItemStack): RECIPE =
        factory.create(group ?: "", CookingBookCategory.MISC, ingredient, output, exp, timeOperator.applyAsInt(time))
}
