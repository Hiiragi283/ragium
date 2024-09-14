package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.recipe.HTRecipeInput
import hiiragi283.ragium.common.recipe.HTRecipeResult
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import hiiragi283.ragium.common.shape.HTMultiMachineShape
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.RecipeEntry
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTMachineBlockEntity(
    pos: BlockPos,
    state: BlockState,
) : BlockEntity(RagiumBlockEntityTypes.MACHINE, pos, state), HTDelegatedInventory, NamedScreenHandlerFactory {

    companion object {
        @JvmField
        val TICKER: BlockEntityTicker<HTMachineBlockEntity> =
            BlockEntityTicker { world: World, pos: BlockPos, _: BlockState, blockEntity: HTMachineBlockEntity ->
                if (blockEntity.ticks >= 200) {
                    blockEntity.ticks = 0
                    val input = HTRecipeInput {
                        add(blockEntity.getStack(0))
                        add(blockEntity.getStack(1))
                        add(blockEntity.getStack(2))
                    }
                    val recipe: HTMachineRecipe = world.recipeManager.getFirstMatch(blockEntity.type, input, world)
                        .map(RecipeEntry<HTMachineRecipe>::value)
                        .getOrNull() ?: return@BlockEntityTicker
                    if (!canAcceptOutputs(blockEntity, recipe)) return@BlockEntityTicker
                    if (!recipe.type.tier.canProcess(world, pos)) {
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
        private fun canAcceptOutputs(tile: HTMachineBlockEntity, recipe: HTMachineRecipe): Boolean {
            recipe.outputs.forEachIndexed { index: Int, result: HTRecipeResult ->
                val stackIn: ItemStack = tile.getStack(index + 3)
                if (!result.canAccept(stackIn)) {
                    return false
                }
            }
            return true
        }

        @JvmStatic
        private fun decrementInput(tile: HTMachineBlockEntity, slot: Int, recipe: HTMachineRecipe) {
            val delCount: Int = recipe.inputs.getOrNull(slot)?.count ?: return
            tile.getStack(slot).count -= delCount
        }

        @JvmStatic
        private fun modifyOutput(tile: HTMachineBlockEntity, slot: Int, recipe: HTMachineRecipe) {
            tile.parent.modifyStack(slot + 4) { stackIn: ItemStack ->
                recipe.outputs.getOrNull(slot)?.modifyStack(stackIn) ?: stackIn
            }
        }
    }

    constructor(pos: BlockPos, state: BlockState, type: HTMachineType) : this(pos, state) {
        this.type = type
    }

    var type: HTMachineType = HTMachineType.Single.GRINDER
        private set
    var ticks: Int = 0
        private set
    var isActive: Boolean = false
        private set
    val multiShape: HTMultiMachineShape? = (type as? HTMachineType.Multi)?.multiShape
    var showPreview: Boolean = false

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        parent.writeNbt(nbt, registryLookup)
        nbt.putString("MachineType", type.asString())
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        parent.readNbt(nbt, registryLookup)
        val typeName: String = nbt.getString("MachineType")
        type = HTMachineType.getEntries().firstOrNull { it.asString() == typeName } ?: HTMachineType.Single.GRINDER
    }

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory = HTSidedStorageBuilder(7)
        .set(0, HTStorageIO.INPUT, HTStorageSides.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSides.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSides.ANY)
        .set(3, HTStorageIO.INTERNAL, HTStorageSides.NONE)
        .set(4, HTStorageIO.OUTPUT, HTStorageSides.ANY)
        .set(5, HTStorageIO.OUTPUT, HTStorageSides.ANY)
        .set(6, HTStorageIO.OUTPUT, HTStorageSides.ANY)
        .buildInventory()

    override fun markDirty() {
        super<BlockEntity>.markDirty()
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTMachineScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))

    override fun getDisplayName(): Text = type.text

}