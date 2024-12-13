package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.extension.isIn
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class HTTieredFluidStorage(
    val tier: HTMachineTier,
    val storageIO: HTStorageIO,
    val inputTag: TagKey<Fluid>?,
    val syncIndex: Int = 0,
) : SingleFluidStorage(),
    HTFluidInteractable,
    HTFluidSyncable {
    override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

    override fun canInsert(variant: FluidVariant): Boolean = inputTag?.let(variant::isIn) != false

    fun wrapStorage(): Storage<FluidVariant> = storageIO.wrapStorage(this)

    //    HTFluidInteractable    //

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean =
        FluidStorageUtil.interactWithFluidStorage(wrapStorage(), player, Hand.MAIN_HAND)

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        sender(player, syncIndex, variant, amount)
    }
}
