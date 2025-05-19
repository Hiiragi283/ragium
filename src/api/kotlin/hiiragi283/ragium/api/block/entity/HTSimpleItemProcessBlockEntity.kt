package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTSimpleItemProcessBlockEntity(
    recipeType: RecipeType<out HTMachineRecipe>,
    private val sound: SoundEvent,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTMachineBlockEntity(type, pos, state),
    HTItemSlotHandler {
    protected val inputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.INPUT_SLOT, this)
    protected val outputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.OUTPUT_SLOT, this)

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, registryOps)
        inputSlot.writeNbt(nbt, registryOps)
        outputSlot.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.readNbt(nbt, registryOps)
        inputSlot.readNbt(nbt, registryOps)
        outputSlot.readNbt(nbt, registryOps)
    }

    override fun loadEnchantment(newEnchantments: ItemEnchantments) {
        super.loadEnchantment(newEnchantments)
        inputSlot.onUpdateEnchantment(newEnchantments)
        outputSlot.onUpdateEnchantment(newEnchantments)
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
    }

    //    Ticking    //

    protected val recipeCache: HTRecipeCache<HTMachineInput, out HTMachineRecipe> = HTRecipeCache.simple(recipeType)

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 200 tickごとに実行する
        if (!canProcess(200)) return TriState.DEFAULT
        // インプットに一致するレシピを探索する
        val input: HTMachineInput = HTMachineInput.create {
            addInput(0, inputSlot)
            addOutput(0, outputSlot)
        }
        val recipe: HTMachineRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(1600, true) != 1600) return TriState.FALSE
        // レシピを実行する
        recipe.process(input)
        network.extractEnergy(1600, false)
        // サウンドを流す
        level.playSound(null, pos, sound, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    //    Item    //

    final override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    final override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        1 -> outputSlot
        else -> null
    }

    final override fun getSlots(): Int = 2
}
