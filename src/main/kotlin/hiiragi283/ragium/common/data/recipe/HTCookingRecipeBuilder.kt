package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.ItemLike
import java.util.function.IntUnaryOperator
import kotlin.math.max

class HTCookingRecipeBuilder(
    prefix: String,
    private val factory: AbstractCookingRecipe.Factory<*>,
    private val timeOperator: IntUnaryOperator,
    stack: ImmutableItemStack,
) : HTStackRecipeBuilder.Single<HTCookingRecipeBuilder>(prefix, stack) {
    companion object {
        @JvmStatic
        fun smelting(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            "smelting",
            ::SmeltingRecipe,
            IntUnaryOperator.identity(),
            ImmutableItemStack.of(item, count),
        )

        @JvmStatic
        fun blasting(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            "blasting",
            ::BlastingRecipe,
            { it / 2 },
            ImmutableItemStack.of(item, count),
        )

        @JvmStatic
        fun smoking(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            "smoking",
            ::SmokingRecipe,
            { it / 2 },
            ImmutableItemStack.of(item, count),
        )

        @JvmStatic
        fun smeltingAndBlasting(item: ItemLike, count: Int = 1, builderAction: HTCookingRecipeBuilder.() -> Unit) {
            smelting(item, count).apply(builderAction)
            blasting(item, count).apply(builderAction)
        }

        @JvmStatic
        fun smeltingAndSmoking(item: ItemLike, count: Int = 1, builderAction: HTCookingRecipeBuilder.() -> Unit) {
            smelting(item, count).apply(builderAction)
            smoking(item, count).apply(builderAction)
        }
    }

    private var group: String? = null
    private var time: Int = 200
    private var exp: Float = 0f

    fun setTime(time: Int): HTCookingRecipeBuilder = apply {
        this.time = max(0, time)
    }

    fun setExp(exp: Float): HTCookingRecipeBuilder = apply {
        this.exp = max(0f, exp)
    }

    fun setGroup(group: String?): HTCookingRecipeBuilder = apply {
        this.group = group
    }

    override fun createRecipe(output: ItemStack): AbstractCookingRecipe =
        factory.create(group ?: "", CookingBookCategory.MISC, ingredient, output, exp, timeOperator.applyAsInt(time))
}
