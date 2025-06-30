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
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemStackHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.inventory.HTMelterMenu
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandler

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.MELTER, pos, state),
    HTFluidTankHandler {
    override val inventory: HTItemHandler = HTItemStackHandler(1, this::setChanged)
    private val outputTank: HTFluidTank = HTFluidTank.create(RagiumConstantValues.OUTPUT_TANK, this)
    override val energyUsage: Int get() = RagiumConfig.COMMON.advancedMachineEnergyUsage.get()

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        super.writeNbt(writer)
        outputTank.writeNbt(writer)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        super.readNbt(reader)
        outputTank.readNbt(reader)
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
        val input: HTUniversalRecipeInput = HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0))
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
        inventory.consumeStackInSlot(0, recipe.ingredient.count())
        // エネルギーを減らす
        network.extractEnergy(requiredEnergy, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    override fun getItemHandler(direction: Direction?): IItemHandler? = HTFilteredItemHandler(inventory, HTItemFilter.INSERT_ONLY)
    //    Fluid    //

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.OUTPUT

    override fun getFluidTank(tank: Int): HTFluidTank? = outputTank

    override fun getTanks(): Int = 1

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTMelterMenu = HTMelterMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
