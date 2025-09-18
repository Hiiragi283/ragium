package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.toNonNullList
import hiiragi283.ragium.api.extension.wrapOptional
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult

/**
 * @see [vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder]
 */
class HTCuttingBoardRecipeBuilder(output: ChanceResult) : HTIngredientRecipeBuilder.Prefixed<HTCuttingBoardRecipeBuilder>("cutting") {
    constructor(stack: ItemStack, chance: Float = 1f) : this(ChanceResult(stack, chance))

    constructor(item: ItemLike, count: Int = 1, chance: Float = 1f) : this(ItemStack(item, count), chance)

    private val results: MutableList<ChanceResult> = mutableListOf(output)
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

    override fun getPrimalId(): ResourceLocation = results[0].stack.itemHolder.idOrThrow

    private var group: String? = null

    override fun group(groupName: String?): HTCuttingBoardRecipeBuilder = apply {
        this.group = groupName
    }

    override fun createRecipe(): CuttingBoardRecipe = CuttingBoardRecipe(
        group ?: "",
        ingredients[0],
        ingredients[1],
        results.toNonNullList(),
        sound.wrapOptional(),
    )
}
