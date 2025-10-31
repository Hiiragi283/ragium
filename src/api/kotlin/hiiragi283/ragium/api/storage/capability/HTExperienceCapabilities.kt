package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import hiiragi283.ragium.api.storage.experience.IExperienceStorage
import hiiragi283.ragium.api.storage.experience.IExperienceStorageItem
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.common.extensions.IItemStackExtension
import net.neoforged.neoforge.common.extensions.ILevelExtension

object HTExperienceCapabilities : HTMultiCapability<IExperienceStorage, IExperienceStorageItem> {
    private val id: ResourceLocation = RagiumAPI.id("experience")
    override val block: BlockCapability<IExperienceStorage, Direction?> = BlockCapability.createSided(id, IExperienceStorage::class.java)
    override val entity: EntityCapability<IExperienceStorage, Direction?> = EntityCapability.createSided(id, IExperienceStorage::class.java)
    override val item: ItemCapability<IExperienceStorageItem, Void?> = ItemCapability.createVoid(id, IExperienceStorageItem::class.java)

    fun getStorage(level: ILevelExtension, pos: BlockPos, side: Direction?): HTExperienceStorage? =
        this.getCapability(level, pos, side)?.let(::wrapStorage)

    fun getStorage(entity: Entity, side: Direction?): HTExperienceStorage? = this.getCapability(entity, side)?.let(::wrapStorage)

    fun getStorage(stack: IItemStackExtension): HTExperienceStorage? = this.getCapability(stack)?.let(::wrapStorage)

    fun getStorage(stack: ImmutableItemStack?): HTExperienceStorage? = this.getCapability(stack)?.let(::wrapStorage)

    private fun wrapStorage(storage: IExperienceStorage): HTExperienceStorage? = when (storage) {
        is HTExperienceStorage -> storage
        else -> object : HTExperienceStorage, HTContentListener.Empty, HTValueSerializable.Empty {
            override fun getAmount(): Long = storage.getExpStored()

            override fun getCapacity(): Long = storage.getMaxExpStored()

            override fun insertExp(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long =
                storage.insertExp(amount, action.simulate)

            override fun extractExp(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long =
                storage.extractExp(amount, action.simulate)
        }
    }
}
