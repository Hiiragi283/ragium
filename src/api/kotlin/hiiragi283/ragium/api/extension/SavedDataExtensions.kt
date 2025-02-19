package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.util.HTSavedDataType
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData

//    SavedData - Client    //

/**
 * 指定した[Minecraft]から[ResourceKey]をキーとした[T]のマップを返します。
 * @param T [SavedData]を継承したクラス
 */
fun <T : SavedData> Minecraft.getClientSavedDataMap(type: HTSavedDataType<T>): Map<ResourceKey<Level>, T> =
    singleplayerServer?.getSavedDataMap(type) ?: mapOf()

//    SavedData - Server    //

/**
 * 指定した[MinecraftServer]に対して[ResourceKey]をキーとした[T]のマップを返します。
 * @param T [SavedData]を継承したクラス
 */
fun <T : SavedData> MinecraftServer.getSavedDataMap(type: HTSavedDataType<T>): Map<ResourceKey<Level>, T> =
    allLevels.associate { level: ServerLevel -> level.dimension() to level.getServerSavedData(type) }

/**
 * 指定した[ServerLevel]に対して[SavedData]を取得または作成します。
 * @param T [SavedData]を継承したクラス
 * @return 取得または作成された[SavedData]
 */
fun <T : SavedData> ServerLevel.getServerSavedData(type: HTSavedDataType<T>): T = level.dataStorage
    .computeIfAbsent(type.factory, type.saveId)
    .apply(SavedData::setDirty)
