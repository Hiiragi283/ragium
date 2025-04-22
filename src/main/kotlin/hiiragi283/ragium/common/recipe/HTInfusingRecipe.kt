package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

/**
 * アイテムと液体を別のアイテムに変換するレシピ
 */
class HTInfusingRecipe(
    private val itemIngredient: SizedIngredient,
    private val fluidIngredient: SizedFluidIngredient,
    private val output: HTItemOutput,
) : HTMachineRecipe(),
    HTDefinitionRecipe<HTMachineInput> {
    override fun matches(input: HTMachineInput): Boolean {
        val bool1: Boolean = itemIngredient.test(input.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = fluidIngredient.test(input.getFluidStack(HTStorageIO.INPUT, 0))
        return bool1 && bool2
    }

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(itemIngredient),
            listOf(fluidIngredient),
            listOf(output),
            listOf(),
        ),
    )

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.INFUSING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.INFUSING.get()
}
