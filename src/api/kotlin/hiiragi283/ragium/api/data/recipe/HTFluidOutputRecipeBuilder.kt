package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.recipe.base.HTFluidOutput
import hiiragi283.ragium.api.recipe.base.HTFluidOutputRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import net.minecraft.core.HolderGetter
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTFluidOutputRecipeBuilder<T : HTFluidOutputRecipe>(
    lookup: HolderGetter<Item>,
    override val prefix: String,
    private val factory: (String, List<HTItemIngredient>, List<SizedFluidIngredient>, List<HTItemOutput>, List<HTFluidOutput>) -> T,
) : HTMachineRecipeBuilder<HTFluidOutputRecipeBuilder<T>, T>(lookup) {
    companion object {
        @JvmStatic
        fun crusher(lookup: HolderGetter<Item>): HTFluidOutputRecipeBuilder<HTCrusherRecipe> =
            HTFluidOutputRecipeBuilder(lookup, "crusher") {
                group: String,
                itemInputs: List<HTItemIngredient>,
                fluidInputs: List<SizedFluidIngredient>,
                itemOutputs: List<HTItemOutput>,
                fluidOutputs: List<HTFluidOutput>,
                ->
                checkRange(itemInputs, 1..1)
                checkEmpty(fluidInputs)
                checkEmpty(fluidOutputs)
                HTCrusherRecipe(
                    group,
                    itemInputs[0],
                    itemOutputs,
                )
            }

        @JvmStatic
        fun extractor(lookup: HolderGetter<Item>): HTFluidOutputRecipeBuilder<HTExtractorRecipe> =
            HTFluidOutputRecipeBuilder(lookup, "extractor") {
                group: String,
                itemInputs: List<HTItemIngredient>,
                fluidInputs: List<SizedFluidIngredient>,
                itemOutputs: List<HTItemOutput>,
                fluidOutputs: List<HTFluidOutput>,
                ->
                checkRange(itemInputs, 1..1)
                checkEmpty(fluidInputs)
                HTExtractorRecipe(
                    group,
                    itemInputs[0],
                    itemOutputs,
                    fluidOutputs,
                )
            }

        @JvmStatic
        fun infuser(lookup: HolderGetter<Item>): HTFluidOutputRecipeBuilder<HTInfuserRecipe> =
            HTFluidOutputRecipeBuilder(lookup, "infuser") {
                group: String,
                itemInputs: List<HTItemIngredient>,
                fluidInputs: List<SizedFluidIngredient>,
                itemOutputs: List<HTItemOutput>,
                fluidOutputs: List<HTFluidOutput>,
                ->
                checkRange(itemInputs, 1..1)
                checkRange(fluidInputs, 1..1)
                HTInfuserRecipe(
                    group,
                    itemInputs[0],
                    fluidInputs[0],
                    itemOutputs,
                    fluidOutputs,
                )
            }

        @JvmStatic
        fun mixer(lookup: HolderGetter<Item>): HTFluidOutputRecipeBuilder<HTMixerRecipe> = HTFluidOutputRecipeBuilder(lookup, "mixer") {
            group: String,
            itemInputs: List<HTItemIngredient>,
            fluidInputs: List<SizedFluidIngredient>,
            itemOutputs: List<HTItemOutput>,
            fluidOutputs: List<HTFluidOutput>,
            ->
            checkRange(itemInputs, 0..1)
            checkRange(fluidInputs, 1..2)
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
        fun refinery(lookup: HolderGetter<Item>): HTFluidOutputRecipeBuilder<HTRefineryRecipe> =
            HTFluidOutputRecipeBuilder(lookup, "refinery") {
                group: String,
                itemInputs: List<HTItemIngredient>,
                fluidInputs: List<SizedFluidIngredient>,
                itemOutputs: List<HTItemOutput>,
                fluidOutputs: List<HTFluidOutput>,
                ->
                checkRange(itemInputs, 0..0)
                checkRange(fluidInputs, 1..1)
                HTRefineryRecipe(
                    group,
                    fluidInputs[0],
                    itemOutputs,
                    fluidOutputs,
                )
            }

        @JvmStatic
        private fun checkEmpty(list: List<*>) {
            check(list.isEmpty()) { "Given list must be empty!" }
        }

        @JvmStatic
        private fun checkRange(list: List<*>, range: IntRange) {
            val size: Int = list.size
            check(size in range) { "Invalid list size: $size for $range" }
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

    override fun fluidOutput(output: HTFluidOutput): HTFluidOutputRecipeBuilder<T> = apply {
        fluidOutputs.add(output)
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
