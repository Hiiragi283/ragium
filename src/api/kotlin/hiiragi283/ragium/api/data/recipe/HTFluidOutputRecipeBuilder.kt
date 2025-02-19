package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.api.recipe.HTInfuserRecipe
import hiiragi283.ragium.api.recipe.HTMixerRecipe
import hiiragi283.ragium.api.recipe.HTRefineryRecipe
import hiiragi283.ragium.api.recipe.base.HTFluidOutput
import hiiragi283.ragium.api.recipe.base.HTFluidOutputRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.Optional

class HTFluidOutputRecipeBuilder<T : HTFluidOutputRecipe>(
    override val prefix: String,
    private val factory: (String, List<HTItemIngredient>, List<SizedFluidIngredient>, List<HTItemOutput>, List<HTFluidOutput>) -> T,
) : HTMachineRecipeBuilderBase<HTFluidOutputRecipeBuilder<T>, T>() {
    companion object {
        @JvmStatic
        fun extractor(): HTFluidOutputRecipeBuilder<HTExtractorRecipe> = HTFluidOutputRecipeBuilder(
            "extractor",
        ) {
            group: String,
            itemInputs: List<HTItemIngredient>,
            fluidInputs: List<SizedFluidIngredient>,
            itemOutputs: List<HTItemOutput>,
            fluidOutputs: List<HTFluidOutput>,
            ->
            HTExtractorRecipe(
                group,
                itemInputs[0],
                itemOutputs,
                fluidOutputs,
            )
        }

        @JvmStatic
        fun infuser(): HTFluidOutputRecipeBuilder<HTInfuserRecipe> = HTFluidOutputRecipeBuilder(
            "infuser",
        ) {
            group: String,
            itemInputs: List<HTItemIngredient>,
            fluidInputs: List<SizedFluidIngredient>,
            itemOutputs: List<HTItemOutput>,
            fluidOutputs: List<HTFluidOutput>,
            ->
            HTInfuserRecipe(
                group,
                itemInputs[0],
                fluidInputs[0],
                itemOutputs,
                fluidOutputs,
            )
        }

        @JvmStatic
        fun mixer(): HTFluidOutputRecipeBuilder<HTMixerRecipe> = HTFluidOutputRecipeBuilder(
            "mixer",
        ) {
            group: String,
            itemInputs: List<HTItemIngredient>,
            fluidInputs: List<SizedFluidIngredient>,
            itemOutputs: List<HTItemOutput>,
            fluidOutputs: List<HTFluidOutput>,
            ->
            HTMixerRecipe(
                group,
                fluidInputs[0],
                fluidInputs[1],
                Optional.ofNullable(itemInputs.getOrNull(0)),
                itemOutputs,
                fluidOutputs,
            )
        }

        @JvmStatic
        fun refinery(): HTFluidOutputRecipeBuilder<HTRefineryRecipe> = HTFluidOutputRecipeBuilder(
            "refinery",
        ) {
            group: String,
            itemInputs: List<HTItemIngredient>,
            fluidInputs: List<SizedFluidIngredient>,
            itemOutputs: List<HTItemOutput>,
            fluidOutputs: List<HTFluidOutput>,
            ->
            HTRefineryRecipe(
                group,
                fluidInputs[0],
                itemOutputs,
                fluidOutputs,
            )
        }
    }

    private var group: String? = null
    private val itemInputs: MutableList<HTItemIngredient> = mutableListOf()
    private val fluidInputs: MutableList<SizedFluidIngredient> = mutableListOf()
    private val itemOutputs: MutableList<HTItemOutput> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidOutput> = mutableListOf()

    override fun itemInput(ingredient: HTItemIngredient): HTFluidOutputRecipeBuilder<T> = apply {
        itemInputs.add(ingredient)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTFluidOutputRecipeBuilder<T> = apply {
        fluidInputs.add(SizedFluidIngredient(ingredient, amount))
    }

    override fun itemOutput(output: HTItemOutput): HTFluidOutputRecipeBuilder<T> = apply {
        itemOutputs.add(output)
    }

    override fun fluidOutput(stack: FluidStack): HTFluidOutputRecipeBuilder<T> = apply {
        fluidOutputs.add(HTFluidOutput.of(stack))
    }

    override fun getPrimalId(): ResourceLocation = itemOutputs.firstOrNull()?.id
        ?: fluidOutputs.firstOrNull()?.id
        ?: error("Either item or fluid output required!")

    override fun createRecipe(): T = factory(
        group ?: "",
        itemInputs,
        fluidInputs,
        itemOutputs,
        fluidOutputs,
    )

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
