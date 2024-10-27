package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

open class HTGeneratorMachineEntity(type: HTMachineConvertible, tier: HTMachineTier) :
    HTMachineEntity(type, tier),
    SidedStorageBlockEntity {
    override val parent: HTSimpleInventory = HTStorageBuilder(5)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        machineType.generateEnergy(world, pos, tier)
    }

    private val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.Simple()

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()

    final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSimpleMachineScreenHandler(
            syncId,
            playerInventory,
            packet,
            createContext(),
        )
}
