package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.ICustomIngredient

class HTShapelessRecipeBuilder(private val output: ItemStack, private val category: CraftingBookCategory) :
    HTRecipeBuilder<ShapelessRecipe> {
    constructor(item: ItemLike, count: Int = 1, category: CraftingBookCategory = CraftingBookCategory.MISC) : this(
        ItemStack(item, count),
        category,
    )

    private val ingredients: NonNullList<Ingredient> = NonNullList.create()

    fun requiresFor(count: Int, ingredient: Ingredient): HTShapelessRecipeBuilder = apply {
        repeat(count) { requires(ingredient) }
    }

    fun requires(prefix: HTTagPrefix, key: HTMaterialKey): HTShapelessRecipeBuilder = requires(prefix.createTag(key))

    fun requires(tagKey: TagKey<Item>): HTShapelessRecipeBuilder = requires(Ingredient.of(tagKey))

    fun requires(item: ItemLike): HTShapelessRecipeBuilder = requires(Ingredient.of(item))

    fun requires(ingredient: ICustomIngredient): HTShapelessRecipeBuilder = requires(ingredient.toVanilla())

    fun requires(ingredient: Ingredient): HTShapelessRecipeBuilder = apply {
        ingredients.add(ingredient)
    }

    //    RecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    private var groupName: String? = null

    override fun group(groupName: String?): HTShapelessRecipeBuilder = apply {
        this.groupName = groupName
    }

    override fun getResult(): Item = output.item

    override fun getIdPrefix(): String = "shapeless"

    override fun createRecipe(): ShapelessRecipe = ShapelessRecipe(
        groupName ?: "",
        category,
        output,
        ingredients,
    )
}
