package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTGrowthChamberRecipe
import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

class HTGrowthChamberRecipeBuilder : HTMachineRecipeBuilderBase<HTGrowthChamberRecipeBuilder, HTGrowthChamberRecipe>() {
    private var group: String? = null
    private lateinit var seed: Ingredient
    private lateinit var soil: Ingredient
    private lateinit var crop: ItemStack
    private var waterAmount: Int = 100

    override fun itemInput(ingredient: Ingredient, count: Int): HTGrowthChamberRecipeBuilder = apply {
        if (::seed.isInitialized) {
            soil = ingredient
        } else {
            seed = ingredient
        }
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTGrowthChamberRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(stack: ItemStack): HTGrowthChamberRecipeBuilder = apply {
        this.crop = stack
    }

    fun water(amount: Int): HTGrowthChamberRecipeBuilder = apply {
        this.waterAmount = amount
    }

    override fun fluidOutput(stack: FluidStack): HTGrowthChamberRecipeBuilder = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = crop.itemHolder.idOrThrow

    override val prefix: String = "growth"

    override fun createRecipe(): HTGrowthChamberRecipe = HTGrowthChamberRecipe(
        group ?: "",
        seed,
        soil,
        waterAmount,
        crop,
    )

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }

    override fun getResult(): Item = crop.item
}
