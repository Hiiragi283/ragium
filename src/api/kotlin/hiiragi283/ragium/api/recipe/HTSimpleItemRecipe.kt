package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.neoforged.neoforge.common.crafting.SizedIngredient

/**
 * アイテムを別のアイテムに変換するレシピ
 */
abstract class HTSimpleItemRecipe(
    recipeType: HTMachineRecipeType,
    private val ingredient: SizedIngredient,
    private val output: HTItemOutput,
) : HTMachineRecipe(recipeType) {
    final override fun matches(input: HTMachineInput): Boolean = ingredient.test(input.getItemStack(HTStorageIO.INPUT, 0))

    final override fun canProcess(input: HTMachineInput): Boolean {
        // Item output
        val outputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.OUTPUT, 0) ?: return false
        if (!outputSlot.canInsert(output.get())) return false
        // Item input
        val inputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.INPUT, 0) ?: return false
        return inputSlot.canExtract(ingredient.count())
    }

    final override fun process(input: HTMachineInput) {
        // Item output
        input.getSlot(HTStorageIO.OUTPUT, 0).insert(output.get(), false)
        // Item input
        input.getSlot(HTStorageIO.INPUT, 0).extract(ingredient.count(), false)
    }

    final override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(ingredient),
            listOf(),
            listOf(output),
            listOf(),
        ),
    )
}
