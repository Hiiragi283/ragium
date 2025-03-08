package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTAssemblerRecipe
import hiiragi283.ragium.api.recipe.base.HTFluidOutput
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import net.minecraft.core.HolderGetter
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.Optional

class HTAssemblerRecipeBuilder(lookup: HolderGetter<Item>) :
    HTMachineRecipeBuilder<HTAssemblerRecipeBuilder, HTAssemblerRecipe>(lookup) {
    private var group: String? = null
    private lateinit var firstInput: HTItemIngredient
    private lateinit var secondInput: HTItemIngredient
    private var thirdInput: HTItemIngredient? = null
    private lateinit var output: HTItemOutput

    override fun itemInput(ingredient: HTItemIngredient): HTAssemblerRecipeBuilder = apply {
        if (!::firstInput.isInitialized) {
            firstInput = ingredient
            return@apply
        }
        if (!::secondInput.isInitialized) {
            secondInput = ingredient
            return@apply
        }
        thirdInput = ingredient
    }

    override fun fluidInput(ingredient: FluidIngredient, amount: Int): HTAssemblerRecipeBuilder = throw UnsupportedOperationException()

    override fun itemOutput(output: HTItemOutput): HTAssemblerRecipeBuilder = apply {
        check(!::output.isInitialized) { "Output is already initialized" }
        this.output = output
    }

    override fun fluidOutput(output: HTFluidOutput): HTAssemblerRecipeBuilder = throw UnsupportedOperationException()

    override fun getPrimalId(): ResourceLocation = output.id

    override val prefix: String = "assembler"

    override fun createRecipe(): HTAssemblerRecipe = HTAssemblerRecipe(
        group ?: "",
        firstInput,
        secondInput,
        Optional.ofNullable(thirdInput),
        output,
    )

    override fun group(groupName: String?): HTAssemblerRecipeBuilder = apply {
        this.group = groupName
    }
}
