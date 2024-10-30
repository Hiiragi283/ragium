package hiiragi283.ragium.api.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.longRangeCodec
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import kotlin.math.min

class HTSingleFluidStorage(private val capacity: Long) :
    SnapshotParticipant<ResourceAmount<FluidVariant>>(),
    SingleSlotStorage<FluidVariant> {
    companion object {
        @JvmField
        val CODEC: Codec<HTSingleFluidStorage> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    longRangeCodec(0, Long.MAX_VALUE).fieldOf("capacity").forGetter(HTSingleFluidStorage::capacity),
                    longRangeCodec(0, Long.MAX_VALUE).fieldOf("amount").forGetter(HTSingleFluidStorage::amount),
                    FluidVariant.CODEC.fieldOf("variant").forGetter(HTSingleFluidStorage::variant),
                ).apply(instance, ::HTSingleFluidStorage)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTSingleFluidStorage> = PacketCodec.tuple(
            PacketCodecs.VAR_LONG,
            HTSingleFluidStorage::capacity,
            PacketCodecs.VAR_LONG,
            HTSingleFluidStorage::amount,
            FluidVariant.PACKET_CODEC,
            HTSingleFluidStorage::variant,
            ::HTSingleFluidStorage,
        )
    }

    @JvmField
    var amount: Long = 0

    @JvmField
    var variant: FluidVariant = FluidVariant.blank()

    constructor(capacity: Long, amount: Long, variant: FluidVariant) : this(capacity) {
        this.amount = amount
        this.variant = variant
    }

    //    Storage    //

    override fun insert(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount)
        if (variant.isBlank || resource == variant) {
            val inserted: Long = min(maxAmount, capacity - amount)
            if (inserted > 0) {
                updateSnapshots(transaction)
                if (variant.isBlank) {
                    variant = resource
                    amount = inserted
                } else {
                    amount += inserted
                }
                return inserted
            }
        }
        return 0
    }

    override fun extract(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount)
        if (resource == variant) {
            val extracted: Long = min(maxAmount, amount)
            if (extracted > 0) {
                updateSnapshots(transaction)
                amount -= extracted
                if (amount <= 0) {
                    variant = FluidVariant.blank()
                }
                return extracted
            }
        }
        return 0
    }

    override fun isResourceBlank(): Boolean = resource.isBlank

    override fun getResource(): FluidVariant = variant

    override fun getAmount(): Long = amount

    override fun getCapacity(): Long = capacity

    //    SnapshotParticipant    //

    override fun createSnapshot(): ResourceAmount<FluidVariant> = ResourceAmount(variant, amount)

    override fun readSnapshot(snapshot: ResourceAmount<FluidVariant>) {
        this.variant = snapshot.resource
        this.amount = snapshot.amount
    }

    override fun toString(): String = "HTSingleFluidStorage[$amount $variant]"
}
