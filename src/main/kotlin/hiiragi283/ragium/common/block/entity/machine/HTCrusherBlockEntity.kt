package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

sealed class HTCrusherBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state),
    HTItemSlotHandler {
    protected val inputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.INPUT_SLOT, this)
    protected val outputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.OUTPUT_SLOT, this)
    protected abstract val outputSlot1: HTItemSlot?

    final override fun writeNbt(writer: HTNbtCodec.Writer) {
        inputSlot.writeNbt(writer)
        outputSlot.writeNbt(writer)
        outputSlot1?.writeNbt(writer)
    }

    final override fun readNbt(reader: HTNbtCodec.Reader) {
        inputSlot.readNbt(reader)
        outputSlot.readNbt(reader)
        outputSlot1?.readNbt(reader)
    }

    final override fun loadEnchantment(newEnchantments: ItemEnchantments) {
        super.loadEnchantment(newEnchantments)
        inputSlot.onUpdateEnchantment(newEnchantments)
        outputSlot.onUpdateEnchantment(newEnchantments)
        outputSlot1?.onUpdateEnchantment(newEnchantments)
    }

    final override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        inputSlot.dropStack(level, pos)
        outputSlot.dropStack(level, pos)
        outputSlot1?.dropStack(level, pos)
    }

    //    Item    //

    final override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        2 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    final override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        1 -> outputSlot
        2 -> outputSlot1
        else -> null
    }

    final override fun getSlots(): Int = 3

    //    Ticking    //

    protected val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.CRUSHING.get())

    final override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 200 tickごとに実行する
        if (!canProcess(200)) return TriState.DEFAULT
        // インプットに一致するレシピを探索する
        val input: HTMachineInput = HTMachineInput.create(pos) {
            addInput(0, inputSlot)
            addOutput(0, outputSlot)
            outputSlot1?.let { addOutput(1, it) }
        }
        val recipe: HTMachineRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(6400, true) != 6400) return TriState.FALSE
        // レシピを実行する
        recipe.process(input)
        network.extractEnergy(6400, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    //    Basic    //

    class Basic(pos: BlockPos, state: BlockState) : HTCrusherBlockEntity(RagiumBlockEntityTypes.CRUSHER, pos, state) {
        override val outputSlot1: HTItemSlot? = null
    }

    //    Advanced    //

    class Advanced(pos: BlockPos, state: BlockState) : HTCrusherBlockEntity(RagiumBlockEntityTypes.ADVANCED_CRUSHER, pos, state) {
        override val outputSlot1: HTItemSlot = HTItemSlot.create(RagiumConstantValues.OUTPUT_SLOT + 1, this)
    }
}
