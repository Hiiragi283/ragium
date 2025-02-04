package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTSingleItemRecipe
import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

class HTSingleItemRecipeBuilder<T : HTSingleItemRecipe>(private val factory: (String, SizedIngredient, ItemStack) -> T) :
    HTMachineRecipeBuilderBase<HTSingleItemRecipeBuilder<T>>() {
    companion object {
        @JvmStatic
        fun compressor(): HTSingleItemRecipeBuilder<HTCompressorRecipe> = HTSingleItemRecipeBuilder(::HTCompressorRecipe)
    }

    private var group: String? = null
    private lateinit var input: SizedIngredient
    private lateinit var itemOutput: ItemStack

    override fun itemInput(ingredient: Ingredient, count: Int): HTSingleItemRecipeBuilder<T> = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTSingleItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun itemOutput(stack: ItemStack): HTSingleItemRecipeBuilder<T> = apply {
        this.itemOutput = stack
    }

    override fun fluidOutput(stack: FluidStack): HTSingleItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = itemOutput.itemHolder.idOrThrow

    override val prefix: String = "extractor"

    override fun saveInternal(output: RecipeOutput, id: ResourceLocation) {
        output.accept(
            id,
            factory(group ?: "", input, itemOutput),
            null,
        )
    }

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }

    override fun getResult(): Item = itemOutput.item
}
