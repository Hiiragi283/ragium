package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTRecipeInput
import hiiragi283.ragium.common.recipe.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTManualGrinderBlockEntity(
    pos: BlockPos,
    state: BlockState,
) : BlockEntity(RagiumBlockEntityTypes.MANUAL_GRINDER, pos, state), HTDelegatedInventory {

    override val parent: HTSidedInventory = HTSidedStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSides.SIDE)
        .set(1, HTStorageIO.OUTPUT, HTStorageSides.DOWN)
        .buildInventory()
    private val matchGetter: RecipeManager.MatchGetter<HTRecipeInput, HTMachineRecipe> =
        RecipeManager.createCachedMatchGetter(HTMachineType.GRINDER)

    fun process() {
        val world: World = world ?: return
        val recipe: HTMachineRecipe = matchGetter.getFirstMatch(
            HTRecipeInput { add(getStack(0)) },
            world
        ).map { it.value }.getOrNull() ?: return
        val output: ItemStack = recipe.getResult(world.registryManager)
        if (canAcceptOutput(output)) {
            parent.modifyStack(1) { stackIn: ItemStack ->
                when {
                    stackIn.isEmpty -> output
                    ItemStack.areItemsAndComponentsEqual(stackIn, output) -> stackIn.apply { count += output.count }
                    else -> stackIn
                }
            }
            parent.getStack(0).decrement(recipe.inputs[0].count)
        }
    }

    private fun canAcceptOutput(output: ItemStack): Boolean {
        val stackIn: ItemStack = parent.getStack(1)
        return when {
            stackIn.isEmpty -> true
            ItemStack.areItemsAndComponentsEqual(stackIn, output) -> true
            else -> false
        }
    }

    override fun markDirty() {
        super<BlockEntity>.markDirty()
    }

}