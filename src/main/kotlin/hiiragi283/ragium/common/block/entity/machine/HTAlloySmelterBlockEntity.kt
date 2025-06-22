package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlotHelper
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.inventory.HTAlloySmelterMenu
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state),
    HTItemSlotHandler {
    private val inputSlots: List<HTItemSlot> = HTItemSlotHelper.createSlotList(2, RagiumConstantValues.INPUT_SLOT, this)
    private val outputSlots: List<HTItemSlot> = HTItemSlotHelper.createSlotList(4, RagiumConstantValues.OUTPUT_SLOT, this)
    private val allSlots: List<HTItemSlot> = buildList {
        addAll(inputSlots)
        addAll(outputSlots)
    }

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        for (slot: HTItemSlot in allSlots) {
            slot.writeNbt(writer)
        }
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        for (slot: HTItemSlot in allSlots) {
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
        for (slot: HTItemSlot in allSlots) {
            slot.dropStack(level, pos)
        }
    }

    //    Ticking    //

    private val recipeCache: HTRecipeCache<HTUniversalRecipeInput, HTAlloyingRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.ALLOYING.get())

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 200 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
        // インプットに一致するレシピを探索する
        val input: HTUniversalRecipeInput = HTUniversalRecipeInput.fromSlots(inputSlots)
        val recipe: HTAlloyingRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(6400, true) != 6400) return TriState.FALSE
        // アウトプットに搬出できるか判定する
        for (output: HTItemOutput in recipe.outputs) {
            if (!HTItemSlotHelper.canInsertItem(outputSlots, output.get())) {
                return TriState.FALSE
            }
        }
        // インプットから正確な個数を引けるか判定する
        if (!consumeItem(input, recipe, 0, 1)) {
            if (!consumeItem(input, recipe, 1, 0)) {
                return TriState.FALSE
            }
        }
        // 実際にアウトプットに搬出する
        for (output: HTItemOutput in recipe.outputs) {
            HTItemSlotHelper.insertItem(outputSlots, output.getChancedStack(level.random), false)
        }
        // エネルギーを減らす
        network.extractEnergy(6400, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    private fun consumeItem(
        input: HTUniversalRecipeInput,
        recipe: HTAlloyingRecipe,
        first: Int,
        second: Int,
    ): Boolean = if (recipe.ingredients[0].test(input.getItem(first)) && recipe.ingredients[1].test(input.getItem(second))) {
        HTItemSlotHelper.consumeItem(inputSlots[first], recipe.ingredients[0].count(), null)
        HTItemSlotHelper.consumeItem(inputSlots[second], recipe.ingredients[1].count(), null)
        true
    } else {
        false
    }

    //    Item    //

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        in (0..1) -> HTStorageIO.INPUT
        in (2..5) -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        in (0..1) -> inputSlots[slot]
        in (2..5) -> outputSlots[slot - 2]
        else -> null
    }

    override fun getSlots(): Int = 6

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTAlloySmelterMenu = HTAlloySmelterMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(
            inputSlots,
            outputSlots,
            listOf(),
        ),
    )
}
