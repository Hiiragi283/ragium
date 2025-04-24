package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class HTSolidifyingRecipe(
    private val ingredient: SizedFluidIngredient,
    private val catalyst: Ingredient,
    private val itemOutput: HTItemOutput,
) : HTMachineRecipe(),
    HTDefinitionRecipe<HTMachineInput> {
    override fun matches(input: HTMachineInput): Boolean {
        val bool1: Boolean = ingredient.test(input.getFluidStack(HTStorageIO.INPUT, 0))
        val catalystStack: ItemStack = input.getItemStack(HTStorageIO.INPUT, 0)
        val bool2: Boolean = when {
            catalyst.isEmpty -> catalystStack.isEmpty
            else -> catalyst.test(catalystStack)
        }
        return bool1 && bool2
    }

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SOLIDIFYING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SOLIDIFYING.get()

    override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(),
            listOf(ingredient),
            catalyst,
            listOf(itemOutput),
            listOf(),
        ),
    )
}
