package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient

/**
 * アイテムを別のアイテムか液体に変換するレシピ
 */
class HTExtractingRecipe(
    private val ingredient: SizedIngredient,
    private val itemOutput: HTItemOutput?,
    private val fluidOutput: HTFluidOutput?,
) : HTMachineRecipe(),
    HTDefinitionRecipe<HTMachineInput> {
    override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getItemStack(HTStorageIO.INPUT, 0))

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> {
        if (itemOutput == null && fluidOutput == null) {
            return DataResult.error { "Either one fluid or item output required!" }
        }
        return DataResult.success(
            HTRecipeDefinition(
                listOf(ingredient),
                listOf(),
                listOfNotNull(itemOutput),
                listOfNotNull(fluidOutput),
            ),
        )
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()
}
