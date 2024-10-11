package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.inventory.HTSidedStorageBuilder
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.common.screen.HTGeneratorScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

open class HTGeneratorMachineEntity(type: HTMachineConvertible, tier: HTMachineTier) :
    HTMachineEntity(type, tier),
    SidedStorageBlockEntity {
    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        machineType.generateEnergy(world, pos, tier)
    }

    override val parent: HTSimpleInventory = HTSidedStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTGeneratorScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(parentBE.world, parentBE.pos))

    //    SidedStorageBlockEntity    //

    private val fluidStorage: SingleFluidStorage = object : SingleFluidStorage() {
        override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * 8

        override fun canInsert(variant: FluidVariant): Boolean =
            machineType[HTMachinePropertyKeys.FUEL_TAG]?.let(variant.registryEntry::isIn) ?: false

        override fun canExtract(variant: FluidVariant): Boolean =
            machineType[HTMachinePropertyKeys.FUEL_TAG]?.let(variant.registryEntry::isIn) ?: false
    }

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = fluidStorage
}
