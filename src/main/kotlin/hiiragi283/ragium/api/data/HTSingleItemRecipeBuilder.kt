package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTSingleItemRecipe
import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

class HTSingleItemRecipeBuilder<T : HTSingleItemRecipe>(
    override val prefix: String,
    private val factory: (String, SizedIngredient, ItemStack) -> T,
) : HTMachineRecipeBuilderBase<HTSingleItemRecipeBuilder<T>, T>() {
    companion object {
        @JvmStatic
        fun compressor(): HTSingleItemRecipeBuilder<HTCompressorRecipe> = HTSingleItemRecipeBuilder("compressor", ::HTCompressorRecipe)
    }

    private var group: String? = null
    private lateinit var input: SizedIngredient
    private lateinit var output: ItemStack
    private var catalyst: Ingredient? = null

    override fun itemInput(ingredient: Ingredient, count: Int): HTSingleItemRecipeBuilder<T> = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTSingleItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun itemOutput(stack: ItemStack): HTSingleItemRecipeBuilder<T> = apply {
        this.output = stack
    }

    override fun fluidOutput(stack: FluidStack): HTSingleItemRecipeBuilder<T> = throw UnsupportedOperationException()

    fun catalyst(item: ItemLike): HTSingleItemRecipeBuilder<T> = catalyst(Ingredient.of(item))

    fun catalyst(tagKey: TagKey<Item>): HTSingleItemRecipeBuilder<T> = catalyst(Ingredient.of(tagKey))

    fun catalyst(catalyst: Ingredient): HTSingleItemRecipeBuilder<T> = apply {
        this.catalyst = catalyst
    }

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    override fun createRecipe(): T = factory(group ?: "", input, this.output)

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }

    override fun getResult(): Item = output.item
}
