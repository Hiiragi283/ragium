package hiiragi283.ragium.common.storage

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.item.ItemStack
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

        val resourceAmount: ResourceAmount<FluidVariant>?
            get() = context.itemVariant.componentMap.get(RagiumComponentTypes.DRUM)

        //    SingleSlotStorage    //

        override fun insert(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount)
            val inserted: Long = when {
                resourceAmount != null ->
                    if (resourceAmount!!.resource == resource) min(maxAmount, capacity - resourceAmount!!.amount) else 0

                else -> min(maxAmount, capacity)
            }
            if (inserted > 0) {
                val newResourceAmount: ResourceAmount<FluidVariant> = ResourceAmount(resource, amount + inserted)
                val newStack: ItemStack = context.itemVariant.toStack().apply {
                    set(RagiumComponentTypes.DRUM, newResourceAmount)
                }
                if (context.exchange(ItemVariant.of(newStack), 1, transaction) == 1L) {
                    return inserted
                }
            }
            return 0
        }

        override fun extract(resource: FluidVariant, maxAmount: Long, transaction: TransactionContext): Long {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount)
            resourceAmount?.let {
                if (it.resource == resource) {
                    val extracted: Long = min(maxAmount, amount)
                    if (extracted > 0) {
                        val newResourceAmount: ResourceAmount<FluidVariant> = ResourceAmount(resource, amount - extracted)
                        val newStack: ItemStack = context.itemVariant.toStack().apply {
                            if (newResourceAmount.amount <= 0) {
                                remove(RagiumComponentTypes.DRUM)
                            } else {
                                set(RagiumComponentTypes.DRUM, newResourceAmount)
                            }
                        }
                        if (context.exchange(ItemVariant.of(newStack), 1, transaction) == 1L) {
                            return extracted
                        }
                    }
                }
            }
            return 0
        }

        override fun isResourceBlank(): Boolean = resource.isBlank

        override fun getResource(): FluidVariant = resourceAmount?.resource ?: FluidVariant.blank()

        override fun getAmount(): Long = resourceAmount?.amount ?: 0

        override fun getCapacity(): Long = tier.tankCapacity
    }
