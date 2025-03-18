package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTCrushingRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

class HTCrushingRecipeBuilder(val result: ItemStack) : HTRecipeBuilder<HTCrushingRecipe> {
    constructor(result: ItemLike, count: Int = 1) : this(ItemStack(result, count))

    private var group: String? = null
    private lateinit var ingredient: Ingredient

    fun setIngredient(prefix: HTTagPrefix, key: HTMaterialKey): HTCrushingRecipeBuilder = setIngredient(prefix.createIngredient(key))

    fun setIngredient(tagKey: TagKey<Item>): HTCrushingRecipeBuilder = setIngredient(Ingredient.of(tagKey))

    fun setIngredient(item: ItemLike): HTCrushingRecipeBuilder = setIngredient(Ingredient.of(item))

    fun setIngredient(ingredient: Ingredient): HTCrushingRecipeBuilder = apply {
        check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
        this.ingredient = ingredient
    }

    override fun group(groupName: String?): HTCrushingRecipeBuilder = apply {
        this.group = groupName
    }

    override fun getPrimalId(): ResourceLocation = result.itemHolder.idOrThrow

    override fun getIdPrefix(): String = "crushing"

    override fun createRecipe(): HTCrushingRecipe = HTCrushingRecipe(
        group ?: "",
        ingredient,
        result,
    )
}
