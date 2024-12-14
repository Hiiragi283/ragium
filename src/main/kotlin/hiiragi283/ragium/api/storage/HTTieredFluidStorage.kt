package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.extension.interactWithFluidStorage
import hiiragi283.ragium.api.extension.isIn
import hiiragi283.ragium.api.extension.variantStack
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.registry.tag.TagKey

/**
 * A simple [SingleFluidStorage] implementation with tiered capacity
 */
class HTTieredFluidStorage(
    val tier: HTMachineTier,
    val storageIO: HTStorageIO,
    val inputTag: TagKey<Fluid>?,
    val callback: () -> Unit,
    val syncIndex: Int = 0,
) : SingleFluidStorage(),
    HTFluidInteractable,
    HTScreenFluidProvider {
    override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

    override fun canInsert(variant: FluidVariant): Boolean = inputTag?.let(variant::isIn) != false

    override fun onFinalCommit() {
        callback()
    }

    /**
     * @see [HTStorageIO.wrapStorage]
     */
    fun wrapStorage(): Storage<FluidVariant> = storageIO.wrapStorage(this)

    /**
     * Create a new [hiiragi283.ragium.api.storage.HTTieredFluidStorage] instance and copy before containment
     */
    fun updateTier(newTier: HTMachineTier): HTTieredFluidStorage {
        val stack: HTFluidVariantStack = this.variantStack
        val newStorage = HTTieredFluidStorage(newTier, storageIO, inputTag, callback, syncIndex)
        newStorage.variantStack = stack
        return newStorage
    }

    //    HTFluidInteractable    //

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = interactWithFluidStorage(player, storageIO)

    //    HTScreenFluidProvider    //

    override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = mapOf(syncIndex to variantStack)
}
