package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.HTBaseBlockEntity
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTMachineRecipeInput
import hiiragi283.ragium.common.recipe.HTRecipeResult
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.RecipeEntry
import net.minecraft.registry.RegistryWrapper
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
    companion object {
        @JvmField
        val TICKER: BlockEntityTicker<HTMachineBlockEntity<*>> =
            BlockEntityTicker { world: World, pos: BlockPos, _: BlockState, blockEntity: HTMachineBlockEntity<*> ->
                if (blockEntity.ticks >= 200) {
                    blockEntity.ticks = 0
                    val input =
                        HTMachineRecipeInput(
                            blockEntity.getStack(0),
                            blockEntity.getStack(1),
                            blockEntity.getStack(2),
                            blockEntity.getStack(3),
                        )
                    val recipe: HTMachineRecipe =
                        world.recipeManager
                            .getFirstMatch(blockEntity.machineType, input, world)
                            .map(RecipeEntry<HTMachineRecipe>::value)
                            .getOrNull() ?: return@BlockEntityTicker
                    if (!canAcceptOutputs(blockEntity, recipe)) return@BlockEntityTicker
                    if (!blockEntity.canProcessRecipe(world, pos, recipe)) {
                        blockEntity.isActive = false
                        return@BlockEntityTicker
                    }
                    blockEntity.isActive = true
                    decrementInput(blockEntity, 0, recipe)
                    decrementInput(blockEntity, 1, recipe)
                    decrementInput(blockEntity, 2, recipe)
                    modifyOutput(blockEntity, 0, recipe)
                    modifyOutput(blockEntity, 1, recipe)
                    modifyOutput(blockEntity, 2, recipe)
                } else {
                    blockEntity.ticks++
                }
            }

        @JvmStatic
        private fun canAcceptOutputs(tile: HTMachineBlockEntity<*>, recipe: HTMachineRecipe): Boolean {
            recipe.outputs.forEachIndexed { index: Int, result: HTRecipeResult ->
                val stackIn: ItemStack = tile.getStack(index + 3)
                if (!result.canAccept(stackIn)) {
                    return false
                }
            }
            return true
        }

        @JvmStatic
        private fun decrementInput(tile: HTMachineBlockEntity<*>, slot: Int, recipe: HTMachineRecipe) {
            val delCount: Int = recipe.inputs.getOrNull(slot)?.count ?: return
            tile.getStack(slot).count -= delCount
        }

        @JvmStatic
        private fun modifyOutput(tile: HTMachineBlockEntity<*>, slot: Int, recipe: HTMachineRecipe) {
            tile.parent.modifyStack(slot + 4) { stackIn: ItemStack ->
                recipe.outputs.getOrNull(slot)?.modifyStack(stackIn) ?: stackIn
            }
        }
    }

    var ticks: Int = 0
        protected set
    var isActive: Boolean = false
        protected set

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        parent.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        parent.readNbt(nbt, registryLookup)
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = when (isActive) {
        true -> 15
        false -> 0
    }

    protected abstract fun canProcessRecipe(world: World, pos: BlockPos, recipe: HTMachineRecipe): Boolean

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory =
        HTSidedStorageBuilder(7)
            .set(0, HTStorageIO.INPUT, HTStorageSides.ANY)
            .set(1, HTStorageIO.INPUT, HTStorageSides.ANY)
            .set(2, HTStorageIO.INPUT, HTStorageSides.ANY)
            .set(3, HTStorageIO.INTERNAL, HTStorageSides.NONE)
            .set(4, HTStorageIO.OUTPUT, HTStorageSides.ANY)
            .set(5, HTStorageIO.OUTPUT, HTStorageSides.ANY)
            .set(6, HTStorageIO.OUTPUT, HTStorageSides.ANY)
            .buildInventory()

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
