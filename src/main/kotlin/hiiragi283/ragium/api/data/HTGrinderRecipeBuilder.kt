package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTChancedItemStack
import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.*

class HTGrinderRecipeBuilder : HTMachineRecipeBuilderBase<HTGrinderRecipeBuilder, HTGrinderRecipe>() {
    private var group: String? = null
    private lateinit var input: SizedIngredient
    private lateinit var output: ItemStack
    private var secondOutput: ItemStack? = null
    private var chance: Float = 1f

    override fun itemInput(ingredient: Ingredient, count: Int): HTGrinderRecipeBuilder = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTGrinderRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(stack: ItemStack): HTGrinderRecipeBuilder = apply {
        if (::output.isInitialized) {
            secondOutput = stack
        } else {
            this.output = stack
        }
    }

    fun setChance(chance: Float): HTGrinderRecipeBuilder = apply {
        check(chance in (0f..1f)) { "Chance must be between 0 and 1" }
        this.chance = chance
    }

    override fun fluidOutput(stack: FluidStack): HTGrinderRecipeBuilder = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    override val prefix: String = "grinder"

    override fun createRecipe(): HTGrinderRecipe {
        val chanced: HTChancedItemStack? = secondOutput?.let { HTChancedItemStack(it, chance) }
        return HTGrinderRecipe(
            group ?: "",
            input,
            this.output,
            Optional.ofNullable(chanced),
        )
    }

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }

    override fun getResult(): Item = output.item
}
