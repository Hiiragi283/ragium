package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTRefineryRecipe
import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTRefineryRecipeBuilder : HTMachineRecipeBuilderBase<HTRefineryRecipeBuilder, HTRefineryRecipe>() {
    private var group: String? = null
    private lateinit var input: SizedFluidIngredient
    private var itemOutput: ItemStack? = null
    private var fluidOutput: FluidStack? = null

    override fun itemInput(ingredient: Ingredient, count: Int): HTRefineryRecipeBuilder = throw UnsupportedOperationException()

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTRefineryRecipeBuilder = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = SizedFluidIngredient(ingredient, amount)
    }

    override fun itemOutput(stack: ItemStack): HTRefineryRecipeBuilder = apply {
        check(itemOutput == null) { "Output is already initialized" }
        this.itemOutput = stack
    }

    override fun fluidOutput(stack: FluidStack): HTRefineryRecipeBuilder = apply {
        check(fluidOutput == null) { "Output is already initialized" }
        this.fluidOutput = stack
    }

    override fun getPrimalId(): ResourceLocation = itemOutput?.itemHolder?.idOrThrow
        ?: fluidOutput?.fluidHolder?.idOrThrow
        ?: error("Either item or fluid output required!")

    override val prefix: String = "refinery"

    override fun createRecipe(): HTRefineryRecipe = HTRefineryRecipe(
        group ?: "",
        input,
        Optional.ofNullable(itemOutput),
        Optional.ofNullable(fluidOutput),
    )

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }

    override fun getResult(): Item = itemOutput?.item ?: Items.AIR
}
