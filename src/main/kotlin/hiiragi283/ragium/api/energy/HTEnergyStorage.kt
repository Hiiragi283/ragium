package hiiragi283.ragium.api.energy

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage

object HTEnergyStorage {
    @JvmField
    val SIDED: BlockApiLookup<Storage<HTEnergyType>, Direction?> = BlockApiLookup.get(
        RagiumAPI.id("sided_energy_storage"),
        Storage.asClass(),
        Direction::class.java,
    )

    @JvmField
    val ITEM: ItemApiLookup<Storage<HTEnergyType>, ContainerItemContext> = ItemApiLookup.get(
        RagiumAPI.id("energy_storage"),
        Storage.asClass(),
        ContainerItemContext::class.java,
    )

    init {
        /*SIDED.registerFallback { world: World, pos: BlockPos, state: BlockState, blockEntity: BlockEntity?, direction: Direction? ->
            EnergyStorage.SIDED.find(world, pos, state, blockEntity, direction)?.let { storage: EnergyStorage ->
                object : SingleSlotStorage<HTEnergyType> {
                    override fun extract(
                        resource: HTEnergyType,
                        maxAmount: Long,
                        transaction: TransactionContext
                    ): Long = when {
                        resource.isOf(HTEnergyType.ELECTRICITY) ->
                            storage.extract(maxAmount, transaction)

                        else -> 0
                    }

                    override fun insert(
                        resource: HTEnergyType,
                        maxAmount: Long,
                        transaction: TransactionContext?
                    ): Long = when {
                        resource.isOf(HTEnergyType.ELECTRICITY) ->
                            storage.insert(maxAmount, transaction)

                        else -> 0
                    }

                    override fun isResourceBlank(): Boolean = false

                    override fun getResource(): HTEnergyType = HTEnergyType.ELECTRICITY

                    override fun getAmount(): Long = storage.amount

                    override fun getCapacity(): Long = storage.capacity
                }
            }
        }*/

        EnergyStorage.SIDED.registerFallback {
                world: World,
                pos: BlockPos,
                state: BlockState,
                blockEntity: BlockEntity?,
                direction: Direction?,
            ->
            SIDED.find(world, pos, state, blockEntity, direction)?.let { storage: Storage<HTEnergyType> ->
                object : EnergyStorage {
                    override fun insert(maxAmount: Long, transaction: TransactionContext): Long =
                        storage.insert(HTEnergyType.ELECTRICITY, maxAmount, transaction)

                    override fun extract(maxAmount: Long, transaction: TransactionContext): Long =
                        storage.extract(HTEnergyType.ELECTRICITY, maxAmount, transaction)

                    override fun getAmount(): Long = storage.iterator().asSequence().sumOf { it.amount }

                    override fun getCapacity(): Long = storage.iterator().asSequence().sumOf { it.capacity }
                }
            }
        }
    }
}
