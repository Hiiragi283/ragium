package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.HTBaseBlockEntity
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTRecipeResult
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeEntry
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

abstract class HTMachineBlockEntity<T : HTMachineType>(val machineType: T, pos: BlockPos, state: BlockState) :
    HTBaseBlockEntity(machineType.blockEntityType, pos, state),
    HTDelegatedInventory,
    NamedScreenHandlerFactory,
    PropertyDelegateHolder {
    var isActive: Boolean = false
        protected set

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = when (isActive) {
        true -> 15
        false -> 0
    }

    override val tickRate: Int = 200

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        val input =
            HTMachineRecipe.Input(
                getStack(0),
                getStack(1),
                getStack(2),
                getStack(3),
            )
        val recipe: HTMachineRecipe =
            world.recipeManager
                .getFirstMatch(machineType, input, world)
                .map(RecipeEntry<HTMachineRecipe>::value)
                .getOrNull() ?: return
        if (!canAcceptOutputs(recipe)) return
        if (!canProcessRecipe(world, pos, recipe)) {
            isActive = false
            return
        }
        isActive = true
        decrementInput(0, recipe)
        decrementInput(1, recipe)
        decrementInput(2, recipe)
        modifyOutput(0, recipe)
        modifyOutput(1, recipe)
        modifyOutput(2, recipe)
    }

    private fun canAcceptOutputs(recipe: HTMachineRecipe): Boolean {
        recipe.outputs.forEachIndexed { index: Int, result: HTRecipeResult ->
            val stackIn: ItemStack = getStack(index + 3)
            if (!result.canAccept(stackIn)) {
                return false
            }
        }
        return true
    }

    private fun decrementInput(slot: Int, recipe: HTMachineRecipe) {
        val delCount: Int = recipe.getInput(slot)?.count ?: return
        getStack(slot).count -= delCount
    }

    private fun modifyOutput(slot: Int, recipe: HTMachineRecipe) {
        parent.modifyStack(slot + 4) { stackIn: ItemStack ->
            recipe.getOutput(slot)?.modifyStack(stackIn) ?: stackIn
        }
    }

    protected abstract fun canProcessRecipe(world: World, pos: BlockPos, recipe: HTMachineRecipe): Boolean

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory =
        HTSidedStorageBuilder(7)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
            .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
            .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
            .buildSided()

    override fun markDirty() {
        super<HTBaseBlockEntity>.markDirty()
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTMachineScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))

    override fun getDisplayName(): Text = machineType.text

    //    PropertyDelegateHolder    //

    override fun getPropertyDelegate(): PropertyDelegate = object : PropertyDelegate {
        override fun get(index: Int): Int = when (index) {
            0 -> ticks
            1 -> 200
            else -> throw IndexOutOfBoundsException(index)
        }

        override fun set(index: Int, value: Int) {
        }

        override fun size(): Int = 2
    }
}
