package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTGrowthChamberRecipe
import hiiragi283.ragium.api.recipe.base.HTFluidOutput
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import net.minecraft.core.HolderGetter
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

class HTGrowthChamberRecipeBuilder(lookup: HolderGetter<Item>) :
    HTMachineRecipeBuilderBase<HTGrowthChamberRecipeBuilder, HTGrowthChamberRecipe>(lookup) {
    private var group: String? = null
    private lateinit var seed: Ingredient
    private lateinit var soil: Ingredient
    private lateinit var crop: HTItemOutput
    private var waterAmount: Int = 100

    override fun itemInput(ingredient: HTItemIngredient): HTGrowthChamberRecipeBuilder = apply {
        if (::seed.isInitialized) {
            soil = ingredient.ingredient
        } else {
            seed = ingredient.ingredient
        }
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTGrowthChamberRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(output: HTItemOutput): HTGrowthChamberRecipeBuilder = apply {
        this.crop = output
    }

    fun water(amount: Int): HTGrowthChamberRecipeBuilder = apply {
        this.waterAmount = amount
    }

    override fun fluidOutput(output: HTFluidOutput): HTGrowthChamberRecipeBuilder = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = crop.id

    override val prefix: String = "growth"

    override fun createRecipe(): HTGrowthChamberRecipe = HTGrowthChamberRecipe(
        group ?: "",
        seed,
        soil,
        waterAmount,
        crop,
    )

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
