package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHelper
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlotHelper
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.inventory.HTMelterMenu
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
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

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.MELTER, pos, state),
    HTItemSlotHandler,
    HTFluidTankHandler {
    private val inputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.INPUT_SLOT, this)
    private val outputTank: HTFluidTank = HTFluidTank.create(RagiumConstantValues.OUTPUT_TANK, this)

    override val energyUsage: Int get() = RagiumConfig.COMMON.advancedMachineEnergyUsage.get()

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        inputSlot.writeNbt(writer)
        outputTank.writeNbt(writer)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        inputSlot.readNbt(reader)
        outputTank.readNbt(reader)
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
    }

    //    Ticking    //

    private val recipeCache: HTRecipeCache<HTUniversalRecipeInput, HTMeltingRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.MELTING.get())

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 200 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
        // インプットに一致するレシピを探索する
        val input: HTUniversalRecipeInput = HTUniversalRecipeInput.fromSlots(listOf(inputSlot))
        val recipe: HTMeltingRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(requiredEnergy, true) != requiredEnergy) return TriState.DEFAULT
        // アウトプットに搬出できるか判定する
        if (!HTFluidTankHelper.canInsertFluid(outputTank, recipe.output.get())) {
            return TriState.FALSE
        }
        // 実際にアウトプットに搬出する
        HTFluidTankHelper.insertFluid(outputTank, recipe.output.get(), false)
        // インプットを減らす
        HTItemSlotHelper.consumeItem(inputSlot, recipe.ingredient.count(), null)
        // エネルギーを減らす
        network.extractEnergy(requiredEnergy, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    //    Fluid    //

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.OUTPUT

    override fun getFluidTank(tank: Int): HTFluidTank? = outputTank

    override fun getTanks(): Int = 1

    //    Item    //

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = HTStorageIO.INPUT

    override fun getItemSlot(slot: Int): HTItemSlot? = inputSlot

    override fun getSlots(): Int = 1

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTMelterMenu = HTMelterMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(
            listOf(inputSlot),
            listOf(),
        ),
    )
}
