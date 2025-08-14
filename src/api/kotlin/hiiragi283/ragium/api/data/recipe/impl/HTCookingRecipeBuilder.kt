package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.ItemLike
import kotlin.math.max

abstract class HTCookingRecipeBuilder(protected val result: ItemStack) : HTIngredientRecipeBuilder<HTCookingRecipeBuilder> {
    companion object {
        @JvmStatic
        fun smelting(item: ItemLike, count: Int = 1): HTCookingRecipeBuilder = Smelting(ItemStack(item, count))

        @JvmStatic
        fun blasting(item: ItemLike, count: Int = 1, onlyBlasting: Boolean = false): HTCookingRecipeBuilder =
            Blasting(ItemStack(item, count), onlyBlasting)

        @JvmStatic
        fun smoking(item: ItemLike, count: Int = 1, onlySmoking: Boolean = false): HTCookingRecipeBuilder =
            Smoking(ItemStack(item, count), onlySmoking)
    }

    protected var group: String? = null
    protected lateinit var ingredient: Ingredient
    protected var time: Int = 200
    protected var exp: Float = 0f

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

    override fun getPrimalId(): ResourceLocation = result.itemHolder.idOrThrow

    override fun group(groupName: String?): HTCookingRecipeBuilder = apply {
        this.group = groupName
    }

    //    Smelting    //

    private open class Smelting(result: ItemStack) : HTCookingRecipeBuilder(result) {
        override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
            recipeOutput.accept(
                id.withPrefix("smelting/"),
                SmeltingRecipe(
                    group ?: "",
                    CookingBookCategory.MISC,
                    ingredient,
                    result,
                    exp,
                    time,
                ),
                null,
            )
        }
    }

    //    Blasting    //

    private class Blasting(result: ItemStack, private val onlyBlasting: Boolean) : Smelting(result) {
        override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
            val fixedTime: Int = if (onlyBlasting) time else time / 2
            recipeOutput.accept(
                id.withPrefix("blasting/"),
                BlastingRecipe(
                    group ?: "",
                    CookingBookCategory.MISC,
                    ingredient,
                    result,
                    exp,
                    fixedTime,
                ),
                null,
            )

            if (!onlyBlasting) {
                super.save(recipeOutput, id)
            }
        }
    }

    //    Smoking    //

    private class Smoking(result: ItemStack, private val onlySmoking: Boolean) : Smelting(result) {
        override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
            val fixedTime: Int = if (onlySmoking) time else time / 2
            recipeOutput.accept(
                id.withPrefix("smoking/"),
                SmokingRecipe(
                    group ?: "",
                    CookingBookCategory.MISC,
                    ingredient,
                    result,
                    exp,
                    fixedTime,
                ),
                null,
            )

            if (!onlySmoking) {
                super.save(recipeOutput, id)
            }
        }
    }
}
