package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.util.wrapOptional
import net.minecraft.core.NonNullList
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult

/**
 * @see [vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder]
 */
class HTCuttingBoardRecipeBuilder(stack: ImmutableItemStack, chance: Float) :
    HTStackRecipeBuilder<HTCuttingBoardRecipeBuilder>("cutting", stack),
    HTIngredientRecipeBuilder<HTCuttingBoardRecipeBuilder> {
    companion object {
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1, chance: Float = 1f): HTCuttingBoardRecipeBuilder =
            HTCuttingBoardRecipeBuilder(ImmutableItemStack.of(item, count), chance)
    }

    private val results: MutableList<ChanceResult> = mutableListOf(ChanceResult(stack.stack, chance))
    private val ingredients: MutableList<Ingredient> = mutableListOf()
    private var sound: SoundEvent? = null

    override fun addIngredient(ingredient: Ingredient): HTCuttingBoardRecipeBuilder = apply {
        check(ingredients.size <= 2) { "Ingredient has already been initialized!" }
        ingredients.add(ingredient)
    }

    fun addResult(item: ItemLike, count: Int = 1, chance: Float = 1f): HTCuttingBoardRecipeBuilder =
        addResult(ItemStack(item, count), chance)

    fun addResult(stack: ItemStack, chance: Float = 1f): HTCuttingBoardRecipeBuilder = apply {
        this.results.add(ChanceResult(stack, chance))
    }

    fun setSound(sound: SoundEvent): HTCuttingBoardRecipeBuilder = apply {
        this.sound = sound
    }

    private var group: String? = null

    override fun group(groupName: String?): HTCuttingBoardRecipeBuilder = apply {
        this.group = groupName
    }

    override fun createRecipe(output: ItemStack): CuttingBoardRecipe = CuttingBoardRecipe(
        group ?: "",
        ingredients[0],
        ingredients[1],
        results.let(NonNullList<ChanceResult>::copyOf),
        sound.wrapOptional(),
    )
}
