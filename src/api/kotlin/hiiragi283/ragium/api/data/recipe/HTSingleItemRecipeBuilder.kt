package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.HTLaserAssemblyRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import net.minecraft.core.HolderGetter
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.*

class HTSingleItemRecipeBuilder<T : HTSingleItemRecipe>(
    override val prefix: String,
    private val factory: (String, HTItemIngredient, Optional<Ingredient>, HTItemOutput) -> T,
    lookup: HolderGetter<Item>,
) : HTMachineRecipeBuilderBase<HTSingleItemRecipeBuilder<T>, T>(lookup) {
    companion object {
        @JvmStatic
        fun compressor(lookup: HolderGetter<Item>): HTSingleItemRecipeBuilder<HTCompressorRecipe> =
            HTSingleItemRecipeBuilder("compressor", ::HTCompressorRecipe, lookup)

        @JvmStatic
        fun grinder(lookup: HolderGetter<Item>): HTSingleItemRecipeBuilder<HTGrinderRecipe> =
            HTSingleItemRecipeBuilder("grinder", ::HTGrinderRecipe, lookup)

        @JvmStatic
        fun laser(lookup: HolderGetter<Item>): HTSingleItemRecipeBuilder<HTLaserAssemblyRecipe> =
            HTSingleItemRecipeBuilder("laser", ::HTLaserAssemblyRecipe, lookup)
    }

    private var group: String? = null
    private lateinit var input: HTItemIngredient
    private lateinit var output: HTItemOutput
    private var catalyst: Ingredient? = null

    override fun itemInput(ingredient: HTItemIngredient): HTSingleItemRecipeBuilder<T> = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = ingredient
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTSingleItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun itemOutput(output: HTItemOutput): HTSingleItemRecipeBuilder<T> = apply {
        check(!::output.isInitialized) { "Output is already initialized" }
        this.output = output
    }

    override fun fluidOutput(stack: FluidStack): HTSingleItemRecipeBuilder<T> = throw UnsupportedOperationException()

    fun catalyst(item: ItemLike): HTSingleItemRecipeBuilder<T> = catalyst(Ingredient.of(item))

    fun catalyst(tagKey: TagKey<Item>): HTSingleItemRecipeBuilder<T> = catalyst(Ingredient.of(tagKey))

    fun catalyst(catalyst: Ingredient): HTSingleItemRecipeBuilder<T> = apply {
        check(!catalyst.isEmpty) { "Empty ingredient is not allowed for catalyst" }
        this.catalyst = catalyst
    }

    override fun getPrimalId(): ResourceLocation = output.id

    override fun createRecipe(): T = factory(group ?: "", input, Optional.ofNullable(catalyst), output)

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
