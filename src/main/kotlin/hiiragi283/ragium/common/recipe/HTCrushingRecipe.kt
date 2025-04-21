package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.setup.RagiumRecipes
import net.neoforged.neoforge.common.crafting.SizedIngredient

/**
 * アイテムを別のアイテムに変換するレシピ
 */
class HTCrushingRecipe(private val ingredient: SizedIngredient, private val output: HTItemOutput) :
    HTMachineRecipe(RagiumRecipes.CRUSHING) {
    override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getItemStack(HTStorageIO.INPUT, 0))

    override fun canProcess(input: HTMachineInput): Boolean {
        // Item output
        val outputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.OUTPUT, 0) ?: return false
        if (!outputSlot.canInsert(output.get())) return false
        // Item input
        val inputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.INPUT, 0) ?: return false
        return inputSlot.canExtract(ingredient.count())
    }

    override fun process(input: HTMachineInput) {
        // Item output
        input.getSlot(HTStorageIO.OUTPUT, 0).insert(output.get(), false)
        // Item input
        input.getSlot(HTStorageIO.INPUT, 0).extract(ingredient.count(), false)
    }

    override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(ingredient),
            listOf(),
            listOf(output),
            listOf(),
        ),
    )
}
