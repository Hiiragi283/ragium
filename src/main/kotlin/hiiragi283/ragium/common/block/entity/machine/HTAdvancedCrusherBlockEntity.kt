package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTAdvancedCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.ADVANCED_CRUSHER, pos, state),
    HTItemSlotHandler {
    private val inputSlot: HTItemSlot = HTItemSlot.create("item_input", this)
    private val outputSlot: HTItemSlot = HTItemSlot.create("item_output", this)
    private val outputSlot1: HTItemSlot = HTItemSlot.create("item_output1", this)

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, registryOps)
        inputSlot.writeNbt(nbt, registryOps)
        outputSlot.writeNbt(nbt, registryOps)
        outputSlot1.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.readNbt(nbt, registryOps)
        inputSlot.readNbt(nbt, registryOps)
        outputSlot.readNbt(nbt, registryOps)
        outputSlot1.readNbt(nbt, registryOps)
    }

    override fun loadEnchantment(newEnchantments: ItemEnchantments) {
        super.loadEnchantment(newEnchantments)
        inputSlot.onUpdateEnchantment(newEnchantments)
        outputSlot.onUpdateEnchantment(newEnchantments)
        outputSlot1.onUpdateEnchantment(newEnchantments)
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
        outputSlot.dropStack(level, pos)
        outputSlot1.dropStack(level, pos)
    }

    //    Ticking    //

    private val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.CRUSHING.get())

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 200 tick毎に一度実行する
        if (totalTick % 200 != 0) return TriState.DEFAULT
        // インプットに一致するレシピを探索する
        val input: HTMachineInput = HTMachineInput.create {
            addInput(0, inputSlot)
            addOutput(0, outputSlot)
            addOutput(1, outputSlot1)
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

    //    Item    //

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        2 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        1 -> outputSlot
        2 -> outputSlot1
        else -> null
    }

    override fun getSlots(): Int = 3
}
