package hiiragi283.ragium.common.transfer.holder

import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.transfer.energy.HTEnergyHandler
import hiiragi283.lib.transfer.holder.HTEnergyHandlerHolder
import hiiragi283.lib.util.HTDelegates
import net.minecraft.core.Direction
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class HTBasicEnergyHandlerHolder(
    configGetter: HTSlotInfoProvider?,
    private val handler: HTEnergyHandler?,
    private val handlerMap: Map<HTSlotInfo, HTEnergyHandler>,
) : HTConfigCapabilityHolder(configGetter),
    HTEnergyHandlerHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTSlotInfoProvider?): Builder = Builder(configGetter)
    }

    override fun getHandler(side: Direction?): HTEnergyHandler? = when {
        side == null || this.configGetter == null -> handler
        else -> configGetter.getSlotInfo(side).let(handlerMap::get)
    }

    class Builder(val configGetter: HTSlotInfoProvider?) {
        private var hasBuilt = false
        private var handler: Optional<HTEnergyHandler> by HTDelegates.optionalInitialize()
        private val handlerMap: MutableMap<HTSlotInfo, HTEnergyHandler> = mutableEnumMapOf()

        fun <T : HTEnergyHandler> addHandler(info: HTSlotInfo, handler: T): T {
            check(!hasBuilt) { "Builder has already built" }
            this.handler = Optional.of(handler)
            when (info) {
                HTSlotInfo.NONE -> return handler

                HTSlotInfo.BOTH -> {
                    handlerMap[info] = handler
                    handlerMap[HTSlotInfo.INPUT] = handler
                    handlerMap[HTSlotInfo.OUTPUT] = handler
                }

                else -> {
                    handlerMap[info] = handler
                    handlerMap[HTSlotInfo.BOTH] = handler
                }
            }
            return handler
        }

        fun build(): HTBasicEnergyHandlerHolder? {
            hasBuilt = true
            return handler.map { HTBasicEnergyHandlerHolder(configGetter, it, handlerMap) }.getOrNull()
        }
    }
}
