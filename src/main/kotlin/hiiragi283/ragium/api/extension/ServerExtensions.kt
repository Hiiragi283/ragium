package hiiragi283.ragium.api.extension

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.world.HTEnergyNetwork
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData

//    SavedData    //

/**
 * 指定した[level]に対して[SavedData]を取得または作成します。
 * @param T [SavedData]を継承したクラス
 * @param id ワールドに保存する時のID
 * @return 取得または作成された[SavedData]
 */
fun <T : SavedData> getState(level: ServerLevel, type: SavedData.Factory<T>, id: ResourceLocation): T = level.dataStorage
    .computeIfAbsent(type, "${id.namespace}_${id.path}")
    .apply(SavedData::setDirty)

/**
 * 指定した[level]に対して[SavedData]を取得または作成します。
 * @param T [SavedData]を継承したクラス
 * @param id ワールドに保存する時のID
 * @return [Level.getServer]がnullの場合はnull
 */
fun <T : SavedData> getState(level: Level, type: SavedData.Factory<T>, id: ResourceLocation): T? {
    val key: ResourceKey<Level> = level.dimension()
    val server: MinecraftServer = level.server ?: return null
    return getState(server, key, type, id)
}

/**
 * 指定した[server]と[key]に対して[SavedData]を取得または作成します。
 * @param T [SavedData]を継承したクラス
 * @param key 保存先のワールドの[ResourceKey]
 * @param id ワールドに保存する時のID
 * @return [MinecraftServer.getLevel]がnullの場合はnull
 */
fun <T : SavedData> getState(
    server: MinecraftServer,
    key: ResourceKey<Level>,
    type: SavedData.Factory<T>,
    id: ResourceLocation,
): T? {
    val level: ServerLevel = server.getLevel(key) ?: return null
    return getState(level, type, id)
}

/**
 * 指定した[server]に対して[SavedData]を取得または作成します。
 * @param T [SavedData]を継承したクラス
 * @param id ワールドに保存する時のID
 * @return オーバーワールドに対して取得または作成
 */
fun <T : SavedData> getStateFromOverworld(server: MinecraftServer, type: SavedData.Factory<T>, id: ResourceLocation): T =
    getState(server.overworld(), type, id)

/**
 * 指定した[level]に対して[SavedData]を取得または作成します。
 * @param T [SavedData]を継承したクラス
 * @param id ワールドに保存する時のID
 * @return オーバーワールドに対して取得または作成
 */
fun <T : SavedData> getStateFromOverworld(level: ServerLevel, type: SavedData.Factory<T>, id: ResourceLocation): T =
    getStateFromOverworld(level.server, type, id)

// Energy Network

/**
 * 指定した[MinecraftServer]から[HTEnergyNetwork]のマップを返します。
 */
val MinecraftServer.networkMap: Map<ResourceKey<Level>, HTEnergyNetwork>
    get() = allLevels.associate { it.dimension() to it.energyNetwork }

/**
 * 指定した[ServerLevel]から[HTEnergyNetwork]を取得または作成します。
 */
val ServerLevel.energyNetwork: HTEnergyNetwork
    get() = getState(this, HTEnergyNetwork.FACTORY, HTEnergyNetwork.ID)

/**
 * 指定した[Level]から[HTEnergyNetwork]を取得または作成します。
 */
fun Level.getEnergyNetwork(): DataResult<HTEnergyNetwork> =
    getState(this, HTEnergyNetwork.FACTORY, HTEnergyNetwork.ID).toDataResult { "Failed to find energy network!" }

/**
 * 指定した[Level]の[HTEnergyNetwork]に干渉します。
 *
 */
fun Level.processEnergy(flag: HTEnergyNetwork.Flag, amount: Int, simulate: Boolean): Boolean =
    flag.processAmount(getEnergyNetwork(), amount, simulate)
