package hiiragi283.ragium.common.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.common.init.RagiumRecipes
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

/**
 * アイテムまたは液体を複数の出力に加工するレシピ
 *
 * 少なくとも一つ以上のアイテムか液体の出力を指定する必要があります。
 */
class HTCentrifugingRecipe(
    val ingredient: Either<SizedIngredient, SizedFluidIngredient>,
    val itemOutputs: List<HTItemOutput>,
    val fluidOutputs: List<HTFluidOutput>,
) : HTMachineRecipe(RagiumRecipes.CENTRIFUGING) {
    override fun matches(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> = when {
        itemOutputs.isEmpty() && fluidOutputs.isEmpty() -> DataResult.error { "Either item or fluid output required!" }
        else -> DataResult.success(
            HTRecipeDefinition(
                ingredient.left().stream().toList(),
                ingredient.right().stream().toList(),
                itemOutputs,
                fluidOutputs,
            ),
        )
    }
}
