package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getServerSavedData
import hiiragi283.ragium.api.storage.energy.HTEnergyNetworkManager
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.event.server.ServerStoppedEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object HTEnergyNetworkManagerImpl : HTEnergyNetworkManager {
    @JvmStatic
    private val networkMap: MutableMap<ResourceKey<Level>, IEnergyStorage> = mutableMapOf()

    override fun getNetwork(level: Level?): IEnergyStorage? = when (level) {
        is ServerLevel -> getNetworkFromServer(level)

        else -> level?.dimension()?.let(::getNetworkFromKey)
    }

    override fun getNetworkFromKey(key: ResourceKey<Level>): IEnergyStorage? {
        // キャッシュされたネットワークがある場合はそれを返す
        val cached: IEnergyStorage? = networkMap[key]
        if (cached != null) return cached
        // ない場合はキャッシュを取得する
        return RagiumAPI
            .getInstance()
            .getCurrentServer()
            ?.getLevel(key)
            ?.let(::createCache)
    }

    override fun getNetworkFromServer(level: ServerLevel): IEnergyStorage {
        val key: ResourceKey<Level> = level.dimension()
        // キャッシュされたネットワークがある場合はそれを返す
        val cached: IEnergyStorage? = networkMap[key]
        if (cached != null) return cached
        // ない場合はキャッシュを取得する
        return createCache(level)
    }

    private fun createCache(level: ServerLevel): IEnergyStorage {
        val network: HTEnergyNetwork = level.getServerSavedData(HTEnergyNetwork.DATA_FACTORY)
        networkMap.compute(level.dimension()) { key: ResourceKey<Level>, old: IEnergyStorage? -> old ?: network }
        networkMap.put(level.dimension(), network)
        return network
    }

    //    Event    //

    @SubscribeEvent
    fun onServerStarted(event: ServerStartedEvent) {
        for (level: ServerLevel in event.server.allLevels) {
            networkMap.put(level.dimension(), level.getServerSavedData(HTEnergyNetwork.DATA_FACTORY))
        }
    }

    @SubscribeEvent
    fun onServerStopped(event: ServerStoppedEvent) {
        networkMap.clear()
    }
}
