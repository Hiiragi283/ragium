package hiiragi283.ragium.common.storage.experience.tank

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTExperienceCapabilities
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.storage.experience.IExperienceHandlerItem
import hiiragi283.ragium.api.util.HTContentListener
import net.neoforged.neoforge.common.extensions.IItemStackExtension

class HTExperienceHandlerItemWrapper private constructor(private val handler: IExperienceHandlerItem) :
    HTExperienceTank,
    HTContentListener.Empty,
    HTValueSerializable.Empty {
        companion object {
            @JvmStatic
            fun create(stack: IItemStackExtension): HTExperienceHandlerItemWrapper? =
                HTExperienceCapabilities.getCapability(stack)?.let(::create)

            @JvmStatic
            fun create(stack: ImmutableItemStack?): HTExperienceHandlerItemWrapper? =
                HTExperienceCapabilities.getCapability(stack)?.let(::create)

            @JvmStatic
            fun create(handler: IExperienceHandlerItem): HTExperienceHandlerItemWrapper? = when (handler.getExperienceTanks()) {
                1 -> HTExperienceHandlerItemWrapper(handler)
                else -> null
            }
        }

        val container: ImmutableItemStack? get() = handler.getContainer().toImmutable()

        override fun insert(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long =
            handler.insertExperience(0, amount, action.simulate)

        override fun extract(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long =
            handler.extractExperience(0, amount, action.simulate)

        override fun getAmount(): Long = handler.getExperienceAmount(0)

        override fun getCapacity(): Long = handler.getExperienceCapacity(0)
    }
