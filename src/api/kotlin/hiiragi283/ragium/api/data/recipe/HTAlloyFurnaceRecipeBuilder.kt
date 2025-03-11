package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTAlloyFurnaceRecipe
import hiiragi283.ragium.api.recipe.base.HTFluidOutput
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import net.minecraft.core.HolderGetter
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

class HTAlloyFurnaceRecipeBuilder(lookup: HolderGetter<Item>) :
    HTMachineRecipeBuilder<HTAlloyFurnaceRecipeBuilder, HTAlloyFurnaceRecipe>(lookup) {
    private var group: String? = null
    private lateinit var firstInput: HTItemIngredient
    private lateinit var secondInput: HTItemIngredient
    private lateinit var output: HTItemOutput

    override fun itemInput(ingredient: HTItemIngredient): HTAlloyFurnaceRecipeBuilder = apply {
        if (!::firstInput.isInitialized) {
            firstInput = ingredient
            return@apply
        }
        if (!::secondInput.isInitialized) {
            secondInput = ingredient
            return@apply
        }
        throw IllegalStateException("Inputs are already initialized")
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTAlloyFurnaceRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(output: HTItemOutput): HTAlloyFurnaceRecipeBuilder = apply {
        check(!::output.isInitialized) { "Output is already initialized" }
        this.output = output
    }

    override fun fluidOutput(output: HTFluidOutput): HTAlloyFurnaceRecipeBuilder = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = output.id

    override fun createRecipe(): HTAlloyFurnaceRecipe = HTAlloyFurnaceRecipe(
        group ?: "",
        firstInput,
        secondInput,
        output,
    )

    override fun group(groupName: String?): HTAlloyFurnaceRecipeBuilder = apply {
        this.group = groupName
    }
}
