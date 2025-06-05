package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
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

sealed class HTExtractorBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state),
    HTItemSlotHandler {
    private val inputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.INPUT_SLOT, this)
    private val outputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.OUTPUT_SLOT, this)
    protected abstract val outputTank: HTFluidTank?

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        inputSlot.writeNbt(nbt, registryOps)
        outputSlot.writeNbt(nbt, registryOps)
        outputTank?.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        inputSlot.readNbt(nbt, registryOps)
        outputSlot.readNbt(nbt, registryOps)
        outputTank?.readNbt(nbt, registryOps)
    }

    override fun loadEnchantment(newEnchantments: ItemEnchantments) {
        super.loadEnchantment(newEnchantments)
        inputSlot.onUpdateEnchantment(newEnchantments)
        outputSlot.onUpdateEnchantment(newEnchantments)
        outputTank?.onUpdateEnchantment(newEnchantments)
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

    private val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.EXTRACTING.get())

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
            outputTank?.let { addOutput(0, it) }
        }
        val recipe: HTMachineRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(6400, true) != 6400) return TriState.FALSE
        // レシピを実行する
        recipe.process(input)
        network.extractEnergy(6400, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    //    Item    //

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        1 -> outputSlot
        else -> null
    }

    override fun getSlots(): Int = 2

    //    Basic    //

    class Basic(pos: BlockPos, state: BlockState) : HTExtractorBlockEntity(RagiumBlockEntityTypes.EXTRACTOR, pos, state) {
        override val outputTank: HTFluidTank? = null
    }

    //    Advanced    //

    class Advanced(pos: BlockPos, state: BlockState) :
        HTExtractorBlockEntity(RagiumBlockEntityTypes.ADVANCED_EXTRACTOR, pos, state),
        HTFluidTankHandler {
        override val outputTank: HTFluidTank = HTFluidTank.create(RagiumConstantValues.OUTPUT_TANK, this)

        //    Fluid    //

        override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.OUTPUT

        override fun getFluidTank(tank: Int): HTFluidTank? = outputTank

        override fun getTanks(): Int = 1
    }
}
