package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTAssemblerRecipe
import hiiragi283.ragium.api.recipe.HTBlastFurnaceRecipe
import hiiragi283.ragium.api.recipe.HTMultiItemRecipe
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
import java.util.*

class HTMultiItemRecipeBuilder<T : HTMultiItemRecipe>(
    override val prefix: String,
    private val factory: (String, SizedIngredient, SizedIngredient, Optional<SizedIngredient>, ItemStack) -> T,
) : HTMachineRecipeBuilderBase<HTMultiItemRecipeBuilder<T>>() {
    companion object {
        @JvmStatic
        fun assembler(): HTMultiItemRecipeBuilder<HTAssemblerRecipe> = HTMultiItemRecipeBuilder("assembler", ::HTAssemblerRecipe)

        @JvmStatic
        fun blastFurnace(): HTMultiItemRecipeBuilder<HTBlastFurnaceRecipe> =
            HTMultiItemRecipeBuilder("blast_furnace", ::HTBlastFurnaceRecipe)
    }

    private var group: String? = null
    private lateinit var firstInput: SizedIngredient
    private lateinit var secondInput: SizedIngredient
    private var thirdInput: SizedIngredient? = null
    private lateinit var output: ItemStack

    override fun itemInput(ingredient: Ingredient, count: Int): HTMultiItemRecipeBuilder<T> = apply {
        if (!::firstInput.isInitialized) {
            this.firstInput = SizedIngredient(ingredient, count)
            return@apply
        }
        if (!::secondInput.isInitialized) {
            this.secondInput = SizedIngredient(ingredient, count)
            return@apply
        }
        this.thirdInput = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTMultiItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun itemOutput(stack: ItemStack): HTMultiItemRecipeBuilder<T> = apply {
        this.output = stack
    }

    override fun fluidOutput(stack: FluidStack): HTMultiItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    override fun saveInternal(output: RecipeOutput, id: ResourceLocation) {
        output.accept(
            id,
            factory(group ?: "", firstInput, secondInput, Optional.ofNullable(thirdInput), this.output),
            null,
        )
    }

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }

    override fun getResult(): Item = output.item
}
