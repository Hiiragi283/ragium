package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.*

class HTExtractorRecipeBuilder : HTMachineRecipeBuilderBase<HTExtractorRecipeBuilder>() {
    private var group: String? = null
    private lateinit var input: SizedIngredient
    private var itemOutput: ItemStack? = null
    private var fluidOutput: FluidStack? = null

    override fun itemInput(ingredient: Ingredient, count: Int): HTExtractorRecipeBuilder = apply {
        check(!::input.isInitialized) { "Input is already initialized" }
        input = SizedIngredient(ingredient, count)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTExtractorRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(stack: ItemStack): HTExtractorRecipeBuilder = apply {
        this.itemOutput = stack
    }

    override fun fluidOutput(stack: FluidStack): HTExtractorRecipeBuilder = apply {
        this.fluidOutput = stack
    }

    override fun getPrimalId(): ResourceLocation = itemOutput?.itemHolder?.idOrThrow
        ?: fluidOutput?.fluidHolder?.idOrThrow
        ?: error("Either item or fluid output required!")

    override val prefix: String = "extractor"

    override fun saveInternal(output: RecipeOutput, id: ResourceLocation) {
        output.accept(
            id,
            HTExtractorRecipe(
                group ?: "",
                input,
                Optional.ofNullable(itemOutput),
                Optional.ofNullable(fluidOutput),
            ),
            null,
        )
    }

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = this

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }

    override fun getResult(): Item = itemOutput?.item ?: Items.AIR
}
