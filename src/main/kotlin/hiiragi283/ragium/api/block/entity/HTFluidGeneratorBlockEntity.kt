package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.function.Supplier

abstract class HTFluidGeneratorBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    machineKey: HTMachineKey,
) : HTMachineBlockEntity(type, pos, state, machineKey) {
    private val tank: HTMachineFluidTank =
        object : HTMachineFluidTank(FluidType.BUCKET_VOLUME * 8, this@HTFluidGeneratorBlockEntity::setChanged) {
            override fun isFluidValid(stack: FluidStack): Boolean = this@HTFluidGeneratorBlockEntity.isFluidValid(stack)
        }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tank.writeToNBT(registries, tag)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        tank.readFromNBT(registries, tag)
    }

    abstract fun isFluidValid(stack: FluidStack): Boolean

    abstract fun getFuelAmount(stack: FluidStack): Int

    override fun process(level: ServerLevel, pos: BlockPos) {
        if (tank.isEmpty) throw HTMachineException.EmptyFluid(false)
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

    override fun interactWithFluidStorage(player: Player): Boolean =
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, tank)

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        tank.capacity = getEnchantmentLevel(Enchantments.UNBREAKING) * 8 * FluidType.BUCKET_VOLUME
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getFluidHandler(direction: Direction?): IFluidHandler = HTStorageIO.INPUT.wrapFluidHandler(tank)
}
