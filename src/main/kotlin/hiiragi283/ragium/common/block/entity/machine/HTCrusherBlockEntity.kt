package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlotHelper
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.inventory.HTCrusherMenu
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.CRUSHER, pos, state),
    HTItemSlotHandler,
    MenuProvider {
    private val inputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.INPUT_SLOT, this)
    private val outputSlots: List<HTItemSlot> =
        HTItemSlotHelper.createSlotList(4, RagiumConstantValues.OUTPUT_SLOT, this)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        inputSlot.writeNbt(writer)
        for (slot: HTItemSlot in outputSlots) {
            slot.writeNbt(writer)
        }
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        inputSlot.readNbt(reader)
        for (slot: HTItemSlot in outputSlots) {
            slot.readNbt(reader)
        }
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        inputSlot.dropStack(level, pos)
        for (slot: HTItemSlot in outputSlots) {
            slot.dropStack(level, pos)
        }
    }

    //    Ticking    //

    private val recipeCache: HTRecipeCache<SingleRecipeInput, HTCrushingRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.CRUSHING.get())

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 200 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
        // インプットに一致するレシピを探索する
        val input = SingleRecipeInput(inputSlot.stack)
        val recipe: HTCrushingRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(6400, true) != 6400) return TriState.FALSE
        // アウトプットに搬出できるか判定する
        for (output: HTItemOutput in recipe.outputs) {
            if (!HTItemSlotHelper.canInsertItem(outputSlots, output.get())) {
                return TriState.FALSE
            }
        }
        // 実際にアウトプットに搬出する
        for (output: HTItemOutput in recipe.outputs) {
            HTItemSlotHelper.insertItem(outputSlots, output.getChancedStack(level.random), false)
        }
        // インプットを減らす
        HTItemSlotHelper.consumeItem(inputSlot, recipe.ingredient.count(), null)
        // エネルギーを減らす
        network.extractEnergy(6400, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    //    Item    //

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        in (1..4) -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        in (1..4) -> outputSlots[slot - 1]
        else -> null
    }

    override fun getSlots(): Int = 5

    //    Menu    //

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (!level.isClientSide) {
            player.openMenu(this, pos)
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTCrusherMenu(containerId, playerInventory, blockPos, upgrades, inputSlot, outputSlots, containerData)

    override fun getDisplayName(): Component = blockState.block.name
}
