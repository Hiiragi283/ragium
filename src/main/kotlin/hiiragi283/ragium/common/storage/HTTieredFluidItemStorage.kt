package hiiragi283.ragium.common.storage

import hiiragi283.ragium.api.extension.modifyComponent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.util.MutableComponentMap
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import kotlin.math.min

class HTTieredFluidItemStorage private constructor(val context: ContainerItemContext, val tier: HTMachineTier) :
    SingleSlotStorage<FluidVariant> {
        companion object {
            @JvmStatic
            fun find(context: ContainerItemContext): HTTieredFluidItemStorage? = context
                .itemVariant
                .componentMap
                .get(HTMachineTier.COMPONENT_TYPE)
                ?.let { tier: HTMachineTier -> HTTieredFluidItemStorage(context, tier) }
        }

        val resourceAmount: HTFluidVariantStack?
            get() = context.itemVariant.componentMap.get(RagiumComponentTypes.DRUM)

        //    SingleSlotStorage    //

        override fun insert(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount)
            val inserted: Long = when {
                resourceAmount != null ->
                    if (resourceAmount!!.variant == resource) min(maxAmount, capacity - resourceAmount!!.amount) else 0

                else -> min(maxAmount, capacity)
            }
            if (inserted > 0) {
                val newResourceAmount = HTFluidVariantStack(resource, amount + inserted)
                val changed: Long = context.modifyComponent(transaction) { map: MutableComponentMap ->
                    map.set(RagiumComponentTypes.DRUM, newResourceAmount)
                }
                if (changed == 1L) {
                    return inserted
                }
            }
            return 0
        }

        override fun extract(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount)
            resourceAmount?.let {
                if (it.variant == resource) {
                    val extracted: Long = min(maxAmount, amount)
                    if (extracted > 0) {
                        val newResourceAmount = HTFluidVariantStack(resource, amount - extracted)
                        val changed: Long = context.modifyComponent(transaction) { map: MutableComponentMap ->
                            if (newResourceAmount.amount <= 0) {
                                map.remove(RagiumComponentTypes.DRUM)
                            } else {
                                map.set(RagiumComponentTypes.DRUM, newResourceAmount)
                            }
                        }
                        if (changed == 1L) {
                            return extracted
                        }
                    }
                }
            }
            return 0
        }

        override fun isResourceBlank(): Boolean = resource.isBlank

        override fun getResource(): FluidVariant = resourceAmount?.variant ?: FluidVariant.blank()

        override fun getAmount(): Long = resourceAmount?.amount ?: 0

        override fun getCapacity(): Long = tier.tankCapacity
    }
