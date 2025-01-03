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
 * [HTMachineTier]によって容量を変化できる[SingleFluidStorage]の実装
 * @param tier ストレージのティア
 * @param storageIO ストレージの搬入出の制限
 * @param inputTag 搬入可能な液体のタグ
 * @param callback ストレージの中身が変化したときのみ呼ばれる
 * @param syncIndex [HTScreenFluidProvider.getFluidsToSync]に用いられるインデックス
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
     * 指定した[newTier]で[tier]を更新します。
     * @return 更新された[HTTieredFluidStorage]
     */
    fun updateTier(newTier: HTMachineTier): HTTieredFluidStorage {
        val stack: HTFluidVariantStack = this.variantStack
        val newStorage = HTTieredFluidStorage(newTier, storageIO, inputTag, callback, syncIndex)
        newStorage.variantStack = stack
        return newStorage
    }

    //    HTFluidInteractable    //

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = player.interactWithFluidStorage(this, storageIO)

    //    HTScreenFluidProvider    //

    override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = mapOf(syncIndex to variantStack)
}
