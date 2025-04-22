package hiiragi283.ragium.internal

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import com.mojang.authlib.GameProfile
import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddonCollector
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.energy.HTEnergyNetworkManager
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.network.HTBlockEntityUpdatePacket
import hiiragi283.ragium.common.storage.energy.HTEnergyNetworkManagerImpl
import hiiragi283.ragium.common.storage.energy.HTLimitedEnergyStorage
import hiiragi283.ragium.common.storage.fluid.HTFluidTankImpl
import hiiragi283.ragium.common.storage.item.HTItemSlotImpl
import hiiragi283.ragium.setup.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.fml.LogicalSide
import net.neoforged.fml.util.thread.EffectiveSide
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.server.ServerLifecycleHooks
import org.slf4j.Logger
import java.util.UUID
import net.minecraft.util.Unit as MCUnit

class InternalRagiumAPI : RagiumAPI {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        private val GAME_PROFILE = GameProfile(UUID.nameUUIDFromBytes(RagiumAPI.MOD_ID.toByteArray()), "[${RagiumAPI.MOD_NAME}]")
    }

    //    Addon    //

    private lateinit var addonCache: List<RagiumAddon>

    override fun getAddons(): List<RagiumAddon> {
        if (!::addonCache.isInitialized) {
            LOGGER.info("Collecting addons for Ragium...")
            addonCache = HTAddonCollector
                .collectInstances<RagiumAddon>()
                .sortedBy(RagiumAddon::priority)
                .onEach { addon: RagiumAddon ->
                    LOGGER.info("Loaded addon from ${addon::class.qualifiedName}!")
                }
        }
        return addonCache
    }

    //    Component    //

    override fun getActiveComponent(): DataComponentType<MCUnit> = RagiumComponentTypes.IS_ACTIVE.get()

    //    Material    //

    override fun getMaterialRegistry(): HTMaterialRegistry = HTMaterialRegistryImpl

    //    Server    //

    override fun getRagiumGameProfile(): GameProfile = GAME_PROFILE

    override fun getCurrentServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    override fun getCurrentSide(): LogicalSide = EffectiveSide.get()

    override fun getEnergyNetworkManager(): HTEnergyNetworkManager = HTEnergyNetworkManagerImpl

    //    Platform    //

    override fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V> = HTWrappedMultiMap.Mutable(multimap)

    override fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V> = HTWrappedTable.Mutable(table)

    override fun wrapItemSlot(storageIO: HTStorageIO, slotIn: HTItemSlot): IItemHandlerModifiable = object : HTItemSlotHandler {
        override fun getItemIoFromSlot(slot: Int): HTStorageIO = storageIO

        override fun getItemSlot(slot: Int): HTItemSlot? = slotIn

        override fun getSlots(): Int = 1
    }

    override fun wrapFluidTank(storageIO: HTStorageIO, tankIn: HTFluidTank): IFluidHandler = object : HTFluidTankHandler {
        override fun getFluidIoFromSlot(tank: Int): HTStorageIO = storageIO

        override fun getFluidTank(tank: Int): HTFluidTank? = tankIn

        override fun getTanks(): Int = 1
    }

    override fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage =
        HTLimitedEnergyStorage(storageIO, storage)

    override fun buildItemSlot(
        nbtKey: String,
        capacity: Int,
        validator: (HTItemVariant) -> Boolean,
        callback: () -> Unit,
    ): HTItemSlot = HTItemSlotImpl(
        nbtKey,
        capacity,
        validator,
        callback,
    )

    override fun buildFluidTank(
        nbtKey: String,
        capacity: Int,
        validator: (HTFluidVariant) -> Boolean,
        callback: () -> Unit,
    ): HTFluidTank = HTFluidTankImpl(
        nbtKey,
        capacity,
        validator,
        callback,
    )

    override fun sendUpdatePayload(blockEntity: BlockEntity, serverLevel: ServerLevel) {
        val pos: BlockPos = blockEntity.blockPos
        PacketDistributor.sendToPlayersTrackingChunk(
            serverLevel,
            ChunkPos(pos),
            HTBlockEntityUpdatePacket(pos, blockEntity.getUpdateTag(serverLevel.registryAccess())),
        )
    }
}
