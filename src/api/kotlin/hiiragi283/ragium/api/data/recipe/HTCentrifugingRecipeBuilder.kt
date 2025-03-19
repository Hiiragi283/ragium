package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTCentrifugingRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

class HTCentrifugingRecipeBuilder(private val result: ItemStack) :
    HTIngredientRecipeBuilder<HTCentrifugingRecipeBuilder, HTCentrifugingRecipe> {
    private var group: String? = null
    private lateinit var ingredient: Ingredient
    private val subProducts: MutableList<ItemStack> = mutableListOf()

    constructor(item: ItemLike, count: Int = 1) : this(ItemStack(item, count))

    override fun addIngredient(ingredient: Ingredient): HTCentrifugingRecipeBuilder = apply {
        check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
        this.ingredient = ingredient
    }

    fun addSubProduct(item: ItemLike, count: Int = 1): HTCentrifugingRecipeBuilder = addSubProduct(ItemStack(item, count))

    fun addSubProduct(stack: ItemStack): HTCentrifugingRecipeBuilder = apply {
        if (!stack.isEmpty) subProducts.add(stack)
    }

    override fun group(groupName: String?): HTCentrifugingRecipeBuilder = apply {
        this.group = groupName
    }

    override fun getPrimalId(): ResourceLocation = result.itemHolder.idOrThrow

    override val prefix: String = "centrifuging"

    override fun createRecipe(): HTCentrifugingRecipe = HTCentrifugingRecipe(
        group ?: "",
        ingredient,
        buildList {
            add(result)
            addAll(subProducts)
        },
    )
}
