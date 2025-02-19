package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTSolidifierRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTSolidifierRecipeBuilder : HTMachineRecipeBuilderBase<HTSolidifierRecipeBuilder, HTSolidifierRecipe>() {
    private var group: String? = null
    private lateinit var input: SizedFluidIngredient
    private lateinit var output: HTItemOutput
    private var catalyst: Ingredient? = null

    override fun itemInput(ingredient: HTItemIngredient): HTSolidifierRecipeBuilder = throw UnsupportedOperationException()

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTSolidifierRecipeBuilder = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = SizedFluidIngredient(ingredient, amount)
    }

    override fun itemOutput(output: HTItemOutput): HTSolidifierRecipeBuilder = apply {
        check(!::output.isInitialized) { "Output is already initialized" }
        this.output = output
    }

    override fun fluidOutput(stack: FluidStack): HTSolidifierRecipeBuilder = throw UnsupportedOperationException()

    fun catalyst(item: ItemLike): HTSolidifierRecipeBuilder = catalyst(Ingredient.of(item))

    fun catalyst(tagKey: TagKey<Item>): HTSolidifierRecipeBuilder = catalyst(Ingredient.of(tagKey))

    fun catalyst(catalyst: Ingredient): HTSolidifierRecipeBuilder = apply {
        check(!catalyst.isEmpty) { "Empty ingredient is not allowed for catalyst" }
        this.catalyst = catalyst
    }

    override val prefix: String = "solidifier"

    override fun getPrimalId(): ResourceLocation = output.id

    override fun createRecipe(): HTSolidifierRecipe = HTSolidifierRecipe(group ?: "", input, Optional.ofNullable(catalyst), output)

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
