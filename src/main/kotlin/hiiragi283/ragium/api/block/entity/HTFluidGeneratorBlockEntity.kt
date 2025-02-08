package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.function.Supplier

abstract class HTFluidGeneratorBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    machineKey: HTMachineKey,
) : HTMachineBlockEntity(type, pos, state, machineKey) {
    private val tank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.ofFluid(listOf(tank))

    abstract fun isFluidValid(stack: FluidStack): Boolean

    abstract fun getFuelAmount(stack: FluidStack): Int

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.generate(3200)

    override fun process(level: ServerLevel, pos: BlockPos) {
        val stackIn: FluidStack = tank.fluid
        val amount: Int = getFuelAmount(stackIn)
        if (amount <= 0) throw HTMachineException.FindFuel(false)
        if (tank.drain(amount, IFluidHandler.FluidAction.SIMULATE).amount == amount) {
            tank.drain(amount, IFluidHandler.FluidAction.EXECUTE)
            return
        }
        throw throw HTMachineException.ConsumeFuel(false)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = tank.interactWithFluidStorage(player, HTStorageIO.GENERIC)

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        tank.updateCapacity(this)
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getFluidHandler(direction: Direction?): IFluidHandler = HTStorageIO.INPUT.wrapFluidHandler(tank)
}
