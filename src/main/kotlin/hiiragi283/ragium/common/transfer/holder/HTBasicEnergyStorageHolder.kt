package hiiragi283.ragium.common.transfer.holder

import hiiragi283.lib.transfer.energy.HTEnergyStorage
import hiiragi283.lib.transfer.holder.HTEnergyStorageHolder
import hiiragi283.lib.util.HTDelegates
import net.minecraft.core.Direction
import thedarkcolour.kotlinforforge.neoforge.kotlin.enumMapOf

class HTBasicEnergyStorageHolder(
    configGetter: HTSlotInfoProvider?,
    private val storage: HTEnergyStorage?,
    private val storageMap: Map<HTSlotInfo, HTEnergyStorage>,
) : HTConfigCapabilityHolder(configGetter),
    HTEnergyStorageHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTSlotInfoProvider?): Builder = Builder(configGetter)
    }

    override fun getStorage(side: Direction?): HTEnergyStorage? = when {
        side == null || this.configGetter == null -> storage
        else -> configGetter.getSlotInfo(side).let(storageMap::get)
    }

    class Builder(val configGetter: HTSlotInfoProvider?) {
        private var hasBuilt = false
        private var storage: HTEnergyStorage? by HTDelegates.onceOrNull()
        private val storageMap: MutableMap<HTSlotInfo, HTEnergyStorage> = enumMapOf()

        fun <T : HTEnergyStorage> addStorage(info: HTSlotInfo, handler: T): T {
            check(!hasBuilt) { "Builder has already built" }
            this.storage = handler
            when (info) {
                HTSlotInfo.NONE -> return handler

                HTSlotInfo.BOTH -> {
                    storageMap[info] = handler
                    storageMap[HTSlotInfo.INPUT] = handler
                    storageMap[HTSlotInfo.OUTPUT] = handler
                }

                else -> {
                    storageMap[info] = handler
                    storageMap[HTSlotInfo.BOTH] = handler
                }
            }
            return handler
        }

        fun build(): HTBasicEnergyStorageHolder? {
            hasBuilt = true
            return storage?.let { HTBasicEnergyStorageHolder(configGetter, it, storageMap) }
        }
    }
}
