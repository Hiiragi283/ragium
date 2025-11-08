package hiiragi283.ragium.impl.data.recipe

import com.enderio.machines.common.blocks.alloy.AlloySmeltingRecipe
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.SizedIngredient
import kotlin.math.max

/**
 * @see com.enderio.machines.data.recipes.AlloyRecipeProvider
 */
class HTAlloySmeltingRecipeBuilder(stack: ImmutableItemStack) :
    HTStackRecipeBuilder<HTAlloySmeltingRecipeBuilder>("alloy_smelting/", stack) {
    companion object {
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTAlloySmeltingRecipeBuilder =
            HTAlloySmeltingRecipeBuilder(ImmutableItemStack.of(item, count))
    }

    val ingredients: MutableList<SizedIngredient> = mutableListOf()
    var energy: Int = -1
    var exp: Float = -1f

    fun addIngredient(item: ItemLike, count: Int = 1): HTAlloySmeltingRecipeBuilder = addIngredient(Ingredient.of(item), count)

    fun addIngredient(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTAlloySmeltingRecipeBuilder =
        addIngredient(prefix.itemTagKey(material), count)

    fun addIngredient(tagKey: TagKey<Item>, count: Int = 1): HTAlloySmeltingRecipeBuilder = addIngredient(Ingredient.of(tagKey), count)

    fun addIngredient(ingredient: Ingredient, count: Int = 1): HTAlloySmeltingRecipeBuilder =
        addIngredient(SizedIngredient(ingredient, count))

    fun addIngredient(ingredient: SizedIngredient): HTAlloySmeltingRecipeBuilder = apply {
        check(ingredients.size < 3)
        ingredients.add(ingredient)
    }

    fun setEnergy(energy: Int): HTAlloySmeltingRecipeBuilder = apply {
        this.energy = max(0, energy)
    }

    fun setExp(exp: Float): HTAlloySmeltingRecipeBuilder = apply {
        this.exp = max(0f, exp)
    }

    override fun createRecipe(output: ItemStack): AlloySmeltingRecipe = AlloySmeltingRecipe(
        ingredients,
        output,
        energy,
        exp,
    )
}
