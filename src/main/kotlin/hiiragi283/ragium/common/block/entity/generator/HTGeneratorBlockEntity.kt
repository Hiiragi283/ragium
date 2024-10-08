package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.inventory.*
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.screen.HTGeneratorScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTGeneratorBlockEntity :
    HTMachineBlockEntityBase,
    SidedStorageBlockEntity {
    constructor(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : super(type, pos, state)

    constructor(
        type: BlockEntityType<*>,
        pos: BlockPos,
        state: BlockState,
        machineType: HTMachineType,
        tier: HTMachineTier,
    ) : super(
        type,
        pos,
        state,
        machineType,
        tier,
    )

    override fun validateMachineType(machineType: HTMachineType) {
        check(machineType.isGenerator())
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        machineType.generateEnergy(world, pos, tier)
    }

    //    HTDelegatedInventory    //

    override val parent: HTSimpleInventory = HTSidedStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTGeneratorScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))

    //    SidedStorageBlockEntity    //

    private val fluidStorage: SingleFluidStorage = object : SingleFluidStorage() {
        override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET * 8

        override fun canInsert(variant: FluidVariant): Boolean =
            machineType[HTMachinePropertyKeys.FUEL_TAG]?.let(variant.registryEntry::isIn) ?: false

        override fun canExtract(variant: FluidVariant): Boolean =
            machineType[HTMachinePropertyKeys.FUEL_TAG]?.let(variant.registryEntry::isIn) ?: false
    }

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = fluidStorage

    //    Simple    //

    class Simple : HTGeneratorBlockEntity {
        @Deprecated("")
        constructor(pos: BlockPos, state: BlockState) :
            super(RagiumBlockEntityTypes.GENERATOR_MACHINE, pos, state)

        constructor(pos: BlockPos, state: BlockState, machineType: HTMachineType, tier: HTMachineTier) :
            super(RagiumBlockEntityTypes.GENERATOR_MACHINE, pos, state, machineType, tier)
    }
}
