package hiiragi283.ragium.api.recipe.processor

import hiiragi283.ragium.api.extension.getStackOrEmpty
import hiiragi283.ragium.api.extension.iterable
import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTRecipeCache
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SmithingRecipe
import net.minecraft.recipe.input.SmithingRecipeInput
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTSmithingRecipeProcessor(private val inventory: Inventory, private val inputIndex: IntArray, private val outputIndex: Int) :
    HTRecipeProcessor {
    private val matchGetter: HTRecipeCache<SmithingRecipeInput, SmithingRecipe> = HTRecipeCache(RecipeType.SMITHING)

    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): Boolean = runCatching {
        val input: SmithingRecipeInput = inputIndex
            .map(inventory::getStackOrEmpty)
            .let {
                SmithingRecipeInput(
                    it.getOrNull(0) ?: ItemStack.EMPTY,
                    it.getOrNull(1) ?: ItemStack.EMPTY,
                    it.getOrNull(2) ?: ItemStack.EMPTY,
                )
            }
        val recipe: SmithingRecipe =
            matchGetter.getFirstMatch(input, world).getOrNull()?.value ?: return@runCatching false
        val resultStack: ItemStack = recipe.craft(input, world.registryManager).copy()
        val output = HTItemResult(resultStack)
        if (!output.canMerge(inventory.getStack(outputIndex))) return@runCatching false
        inventory.modifyStack(outputIndex, output::merge)
        input.iterable().forEach { stackIn: ItemStack -> stackIn.decrement(1) }
        return@runCatching true
    }.getOrDefault(false)
}
