package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTAssemblerRecipe
import hiiragi283.ragium.api.recipe.HTBlastFurnaceRecipe
import hiiragi283.ragium.api.recipe.base.HTFluidOutput
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import net.minecraft.core.HolderGetter
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTMultiItemRecipeBuilder<T : HTMultiItemRecipe>(
    override val prefix: String,
    private val factory: (String, List<HTItemIngredient>, Optional<SizedFluidIngredient>, HTItemOutput) -> T,
    lookup: HolderGetter<Item>,
) : HTMachineRecipeBuilderBase<HTMultiItemRecipeBuilder<T>, T>(lookup) {
    companion object {
        @JvmStatic
        fun assembler(lookup: HolderGetter<Item>): HTMultiItemRecipeBuilder<HTAssemblerRecipe> =
            HTMultiItemRecipeBuilder("assembler", ::HTAssemblerRecipe, lookup)

        @JvmStatic
        fun blastFurnace(lookup: HolderGetter<Item>): HTMultiItemRecipeBuilder<HTBlastFurnaceRecipe> =
            HTMultiItemRecipeBuilder("blast_furnace", ::HTBlastFurnaceRecipe, lookup)
    }

    private var group: String? = null
    private val itemInputs: MutableList<HTItemIngredient> = mutableListOf()
    private var fluidInput: SizedFluidIngredient? = null
    private lateinit var output: HTItemOutput

    override fun itemInput(ingredient: HTItemIngredient): HTMultiItemRecipeBuilder<T> = apply {
        itemInputs.add(ingredient)
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTMultiItemRecipeBuilder<T> = apply {
        fluidInput = SizedFluidIngredient(ingredient, amount)
    }

    override fun itemOutput(output: HTItemOutput): HTMultiItemRecipeBuilder<T> = apply {
        check(!::output.isInitialized) { "Output is already initialized" }
        this.output = output
    }

    override fun fluidOutput(output: HTFluidOutput): HTMultiItemRecipeBuilder<T> = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = output.id

    override fun createRecipe(): T = factory(group ?: "", itemInputs, Optional.ofNullable(fluidInput), this.output)

    override fun group(groupName: String?): RecipeBuilder = apply {
        this.group = groupName
    }
}
