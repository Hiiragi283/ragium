package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.registries.datamaps.DataMapType

abstract class HTFluidDynamoBlockEntity(
    private val fuelMap: DataMapType<Fluid, HTFluidFuelData>,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTDynamoBlockEntity(type, pos, state),
    HTFluidInteractable {
    final override val inventory: HTItemHandler = HTItemStackHandler.EMPTY
    protected val tank = HTFluidTank(RagiumAPI.getConfig().getDefaultTankCapacity(), this::setChanged)

    protected fun getFuelData(stack: FluidStack): HTFluidFuelData? = stack.fluidHolder.getData(fuelMap)

    protected var lastFuelTime = 0

    override fun serverTickPre(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        val fuelTime: Int = if (usedEnergy == 0) {
            val fuelData: HTFluidFuelData = getFuelData(tank.fluid) ?: return TriState.FALSE
            val result: Int = fuelData.time
            tank.drain(fuelData.amount, IFluidHandler.FluidAction.EXECUTE)
            result
        } else {
            lastFuelTime
        }
        if (fuelTime <= 0) return TriState.FALSE
        // 燃焼時間を確認する
        if (this.lastFuelTime != fuelTime) {
            this.lastFuelTime = fuelTime
            this.requiredEnergy = energyUsage * fuelTime
        }

        // エネルギーを生産する
        if (!doProgress(network)) return TriState.DEFAULT
        return TriState.TRUE
    }

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        result: TriState,
    ) {
        // サウンドを流す
        level.playSound(null, pos, sound, SoundSource.BLOCKS)
    }

    protected abstract val sound: SoundEvent

    final override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(
        tank,
        object : HTFluidFilter {
            override fun canFill(tank: IFluidTank, stack: FluidStack): Boolean = getFuelData(stack) != null

            override fun canDrain(tank: IFluidTank, stack: FluidStack): Boolean = false

            override fun canDrain(tank: IFluidTank, maxDrain: Int): Boolean = false
        },
    )

    final override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, tank)

    final override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null
}
