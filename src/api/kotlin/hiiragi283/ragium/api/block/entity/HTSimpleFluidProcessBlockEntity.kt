package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTSimpleFluidRecipe
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTRecipeType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
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

abstract class HTSimpleFluidProcessBlockEntity(
    val recipeType: HTRecipeType<HTMachineInput, out HTSimpleFluidRecipe>,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTMachineBlockEntity(type, pos, state),
    HTFluidSlotHandler {
    protected val inputTank: HTFluidTank = HTFluidTank.builder(this).build("fluid_input")
    protected val outputTank: HTFluidTank = HTFluidTank.builder(this).build("fluid_output")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, registryOps)
        inputTank.writeNbt(nbt, registryOps)
        outputTank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.readNbt(nbt, registryOps)
        inputTank.readNbt(nbt, registryOps)
        outputTank.readNbt(nbt, registryOps)
    }

    override fun loadEnchantment(newEnchantments: ItemEnchantments) {
        super.loadEnchantment(newEnchantments)
        inputTank.onUpdateEnchantment(newEnchantments)
        outputTank.onUpdateEnchantment(newEnchantments)
    }

    //    Ticking    //

    companion object {
        @JvmStatic
        fun clientTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTSimpleFluidProcessBlockEntity,
        ) {
            blockEntity.totalTick++
        }

        @JvmStatic
        fun serverTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTSimpleFluidProcessBlockEntity,
        ) {
            blockEntity.totalTick++
            blockEntity.processRecipe(level, pos, state)
        }
    }

    protected val recipeCache: HTRecipeCache<HTMachineInput, out HTSimpleFluidRecipe> =
        HTRecipeCache.reloadable(recipeType)

    private var checkRecipe: Boolean = false

    private fun processRecipe(level: Level, pos: BlockPos, state: BlockState) {
        // 200 tick毎に一度実行する
        if (totalTick % 200 != 0) return
        // インプットに一致するレシピを探索する
        val input: HTMachineInput = HTMachineInput
            .builder()
            .addInput(0, inputTank)
            .addOutput(0, outputTank)
            .build()
        val recipe: HTSimpleFluidRecipe = recipeCache.getFirstRecipe(input, level) ?: return skipTicking()
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

    //    Fluid    //

    final override fun getFluidIoFromSlot(tank: Int): HTStorageIO = when (tank) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    final override fun getFluidTank(tank: Int): HTFluidTank? = when (tank) {
        0 -> inputTank
        1 -> outputTank
        else -> null
    }

    final override fun getTanks(): Int = 2
}
