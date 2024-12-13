package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.extension.interactWithFluidStorage
import hiiragi283.ragium.api.extension.isIn
import hiiragi283.ragium.api.extension.variantStack
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity

class HTTieredFluidStorage private constructor(
    val tier: HTMachineTier,
    val storageIO: HTStorageIO,
    val inputTag: TagKey<Fluid>?,
    val callback: () -> Unit,
    val syncIndex: Int = 0,
) : SingleFluidStorage(),
    HTFluidInteractable,
    HTFluidSyncable {
    constructor(tier: HTMachineTier, definition: Settings) : this(
        tier,
        definition.storageIO,
        definition.inputTag,
        definition.callback,
        definition.syncIndex,
    )

    override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

    override fun canInsert(variant: FluidVariant): Boolean = inputTag?.let(variant::isIn) != false

    override fun onFinalCommit() {
        callback()
    }

    fun wrapStorage(): Storage<FluidVariant> = storageIO.wrapStorage(this)

    //    HTFluidInteractable    //

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = interactWithFluidStorage(player, storageIO)

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, handler: HTFluidSyncable.Handler) {
        handler.send(player, syncIndex, variantStack)
    }

    //    Definition    //

    data class Settings(
        val storageIO: HTStorageIO,
        val inputTag: TagKey<Fluid>?,
        val callback: () -> Unit,
        val syncIndex: Int = 0,
    )
}
