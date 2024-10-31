package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.energy.HTEnergyType
import hiiragi283.ragium.api.extension.energyNetwork
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

open class HTGeneratorMachineEntity(type: HTMachineConvertible, tier: HTMachineTier) :
    HTMachineEntity<HTMachineType.Generator>(type.asGenerator(), tier),
    SidedStorageBlockEntity {
    override val parent: HTSimpleInventory = HTStorageBuilder(5)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        generateEnergy(world, pos, tier)
    }

    private fun generateEnergy(world: World, pos: BlockPos, tier: HTMachineTier) {
        if (!machineType.isGenerator()) return
        useTransaction { transaction: Transaction ->
            // Try to consumer fluid
            FluidStorage.SIDED
                .find(world, pos, world.getBlockState(pos), world.getBlockEntity(pos), null)
                ?.let { storage: Storage<FluidVariant> ->
                    StorageUtil
                        .extractAny(storage, FluidConstants.BUCKET, transaction)
                        ?.let { resourceAmount: ResourceAmount<FluidVariant> ->
                            if (resourceAmount.amount == FluidConstants.BUCKET &&
                                generateEnergy(
                                    world,
                                    tier,
                                    transaction,
                                )
                            ) {
                                transaction.commit()
                                return
                            }
                        }
                }
            // check condition
            if (machineType.getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE)(world, pos) &&
                generateEnergy(
                    world,
                    tier,
                    transaction,
                )
            ) {
                transaction.commit()
            } else {
                transaction.abort()
            }
        }
    }

    private fun generateEnergy(world: World, tier: HTMachineTier, transaction: Transaction): Boolean =
        world.energyNetwork?.let { network: HTEnergyNetwork ->
            network.insert(HTEnergyType.ELECTRICITY, tier.recipeCost, transaction) > 0
        } ?: false

    private val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.create(HTMachineType.Size.SIMPLE)

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()

    final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSimpleMachineScreenHandler(
            syncId,
            playerInventory,
            packet,
            createContext(),
        )
}
