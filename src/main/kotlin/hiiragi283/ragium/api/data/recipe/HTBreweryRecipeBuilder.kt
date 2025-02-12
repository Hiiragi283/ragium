package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTBreweryRecipe
import hiiragi283.ragium.api.recipe.base.HTItemResult
import net.minecraft.core.Holder
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.*

class HTBreweryRecipeBuilder : HTMachineRecipeBuilderBase<HTBreweryRecipeBuilder, HTBreweryRecipe>() {
    private var group: String? = null
    private lateinit var firstInput: SizedIngredient
    private lateinit var secondInput: SizedIngredient
    private var thirdInput: SizedIngredient? = null
    private lateinit var potion: Holder<Potion>

    override fun itemInput(ingredient: Ingredient, count: Int): HTBreweryRecipeBuilder = apply {
        if (!::firstInput.isInitialized) {
            this.firstInput = SizedIngredient(ingredient, count)
            return@apply
        }
        if (!::secondInput.isInitialized) {
            this.secondInput = SizedIngredient(ingredient, count)
            return@apply
        }
        check(thirdInput == null) { "Input is already initialized" }
        this.thirdInput = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTBreweryRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(result: HTItemResult): HTBreweryRecipeBuilder = throw UnsupportedOperationException()

    override fun fluidOutput(stack: FluidStack): HTBreweryRecipeBuilder = throw UnsupportedOperationException()

    fun potionOutput(potion: Holder<Potion>): HTBreweryRecipeBuilder = apply {
        this.potion = potion
    }

    override fun getPrimalId(): ResourceLocation = potion.idOrThrow

    override val prefix: String = "brewery"

    override fun createRecipe(): HTBreweryRecipe = HTBreweryRecipe(
        group ?: "",
        firstInput,
        secondInput,
        Optional.ofNullable(thirdInput),
        potion,
    )

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
