package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTRecipeType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.common.recipe.HTSimpleItemRecipe
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTSimpleItemProcessBlockEntity(
    val recipeType: HTRecipeType<HTMachineInput, out HTSimpleItemRecipe>,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTMachineBlockEntity(type, pos, state),
    HTItemSlotHandler {
    protected val inputSlot: HTItemSlot = HTItemSlot.builder(this).build("item_input")
    protected val outputSlot: HTItemSlot = HTItemSlot.builder(this).build("item_output")

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

    companion object {
        @JvmStatic
        fun clientTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTSimpleItemProcessBlockEntity,
        ) {
            blockEntity.totalTick++
        }

        @JvmStatic
        fun serverTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTSimpleItemProcessBlockEntity,
        ) {
            blockEntity.totalTick++
            blockEntity.processRecipe(level, pos, state)
        }
    }

    protected val recipeCache: HTRecipeCache<HTMachineInput, out HTSimpleItemRecipe> =
        HTRecipeCache.reloadable(recipeType)

    private var checkRecipe: Boolean = false

    private fun processRecipe(level: Level, pos: BlockPos, state: BlockState) {
        // 200 tick毎に一度実行する
        if (totalTick % 200 != 0) return
        // インプットに一致するレシピを探索する
        val input: HTMachineInput = HTMachineInput
            .builder()
            .addInput(0, inputSlot)
            .addOutput(0, outputSlot)
            .build()
        val recipe: HTSimpleItemRecipe = recipeCache.getFirstRecipe(input, level) ?: return skipTicking()
        // 処理が行えるか判定する
        if (!recipe.canProcess(input)) return skipTicking()
        // エネルギーを消費できるか判定する
        val network: IEnergyStorage = RagiumAPI.getInstance().getEnergyNetworkManager().getNetwork(level) ?: return skipTicking()
        if (network.extractEnergy(1600, true) != 1600) return skipTicking()
        // レシピを実行する
        recipe.process(input)
        network.extractEnergy(1600, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS)
    }

    override fun setChanged() {
        super.setChanged()
        checkRecipe = true
    }

    protected fun skipTicking() {
        checkRecipe = false
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
