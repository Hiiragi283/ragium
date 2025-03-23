package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTSimpleFluidProcessBlockEntity(
    val recipeType: HTMachineRecipeType,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTMachineBlockEntity(type, pos, state),
    HTFluidTankHandler {
    protected val inputTank: HTFluidTank = HTFluidTank.create("fluid_input", this)
    protected val outputTank: HTFluidTank = HTFluidTank.create("fluid_output", this)

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

    protected val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> = HTRecipeCache.reloadable(recipeType)

    override fun onServerTick(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 200 tick毎に一度実行する
        if (totalTick % 200 != 0) return TriState.DEFAULT
        // インプットに一致するレシピを探索する
        val input: HTMachineInput = HTMachineInput.create {
            addInput(0, inputTank)
            addOutput(0, outputTank)
        }
        val recipe: HTMachineRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // 処理が行えるか判定する
        if (!recipe.canProcess(input)) return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(1600, true) != 1600) return TriState.FALSE
        // レシピを実行する
        recipe.process(input)
        network.extractEnergy(1600, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS)
        return TriState.TRUE
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
