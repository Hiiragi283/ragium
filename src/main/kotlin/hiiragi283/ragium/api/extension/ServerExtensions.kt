package hiiragi283.ragium.api.extension

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.world.HTBackpackManager
import hiiragi283.ragium.api.world.HTEnergyNetwork
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.entity.Entity
import net.minecraft.registry.RegistryKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

//    PlayerEntity    //

/**
 * 指定した[Entity]を[ServerPlayerEntity]にキャストします。
 * @return キャストできなかった場合はnull
 */
fun Entity.asServerPlayer(): ServerPlayerEntity? = this as? ServerPlayerEntity

/**
 * 指定した[World]を[ServerWorld]にキャストします。
 * @return キャストできなかった場合はnull
 */
fun World.asServerWorld(): ServerWorld? = this as? ServerWorld

//    World    //

/**
 * 指定した[World]が[ServerWorld]の場合のみに[action]を実行します。
 */
fun World.ifServer(action: ServerWorld.() -> Unit): World = apply {
    asServerWorld()?.let(action)
}

/**
 * 指定した[World]を別の値に変換します。
 * @param T 戻り値のクラス
 * @param transform [ServerWorld]を[T]に変換するブロック
 * @return [World]が[ServerWorld]を継承している場合は[DataResult.success]，それ以外の場合は[DataResult.error]
 */
fun <T : Any> World?.mapIfServer(transform: (ServerWorld) -> T): DataResult<T> =
    this?.asServerWorld()?.let(transform).toDataResult { "Target world is not ServerWorld!" }

//    PersistentState    //

/**
 * 指定した[world]に対して[PersistentState]を取得または作成します。
 * @param T [PersistentState]を継承したクラス
 * @param id ワールドに保存する時のID
 * @return 取得または作成された[PersistentState]
 */
fun <T : PersistentState> getState(world: ServerWorld, type: PersistentState.Type<T>, id: Identifier): T = world.persistentStateManager
    .getOrCreate(type, id.splitWith('_'))
    .apply(PersistentState::markDirty)

/**
 * 指定した[world]に対して[PersistentState]を取得または作成します。
 * @param T [PersistentState]を継承したクラス
 * @param id ワールドに保存する時のID
 * @return [World.getServer]がnullの場合はnull
 */
fun <T : PersistentState> getState(world: World, type: PersistentState.Type<T>, id: Identifier): T? {
    val key: RegistryKey<World> = world.registryKey
    val server: MinecraftServer = world.server ?: return null
    return getState(server, key, type, id)
}

/**
 * 指定した[server]と[key]に対して[PersistentState]を取得または作成します。
 * @param T [PersistentState]を継承したクラス
 * @param key 保存先のワールドの[RegistryKey]
 * @param id ワールドに保存する時のID
 * @return [MinecraftServer.getWorld]がnullの場合はnull
 */
fun <T : PersistentState> getState(
    server: MinecraftServer,
    key: RegistryKey<World>,
    type: PersistentState.Type<T>,
    id: Identifier,
): T? {
    val world: ServerWorld = server.getWorld(key) ?: return null
    return getState(world, type, id)
}

/**
 * 指定した[server]に対して[PersistentState]を取得または作成します。
 * @param T [PersistentState]を継承したクラス
 * @param id ワールドに保存する時のID
 * @return オーバーワールドに対して取得または作成
 */
fun <T : PersistentState> getStateFromOverworld(server: MinecraftServer, type: PersistentState.Type<T>, id: Identifier): T =
    getState(server.overworld, type, id)

/**
 * 指定した[world]に対して[PersistentState]を取得または作成します。
 * @param T [PersistentState]を継承したクラス
 * @param id ワールドに保存する時のID
 * @return オーバーワールドに対して取得または作成
 */
fun <T : PersistentState> getStateFromOverworld(world: ServerWorld, type: PersistentState.Type<T>, id: Identifier): T =
    getStateFromOverworld(world.server, type, id)

// Backpack

/**
 * 指定した[MinecraftServer]から[HTBackpackManager]を取得または作成します。
 */
val MinecraftServer.backpackManager: HTBackpackManager
    get() = getStateFromOverworld(this, HTBackpackManager.TYPE, HTBackpackManager.ID)

/**
 * 指定した[WorldAccess]から[HTBackpackManager]を取得または作成します。
 * @return [WorldAccess.getServer]がnullの場合はnull
 */
fun WorldAccess.getBackpackManager(): DataResult<HTBackpackManager> {
    val server: MinecraftServer = server ?: return DataResult.error { "Failed to find backpack manager!" }
    return DataResult.success(server.backpackManager)
}

// Energy Network

/**
 * 指定した[MinecraftServer]から[HTEnergyNetwork]のマップを返します。
 */
val MinecraftServer.networkMap: Map<RegistryKey<World>, HTEnergyNetwork>
    get() = worlds.associate { it.registryKey to it.energyNetwork }

/**
 * 指定した[ServerWorld]から[HTEnergyNetwork]を取得または作成します。
 */
val ServerWorld.energyNetwork: HTEnergyNetwork
    get() = getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID)

/**
 * 指定した[World]から[HTEnergyNetwork]を取得または作成します。
 */
fun World.getEnergyNetwork(): DataResult<HTEnergyNetwork> =
    getState(this, HTEnergyNetwork.TYPE, HTEnergyNetwork.ID).toDataResult { "Failed to find energy network!" }

/**
 * 指定した[World]の[HTEnergyNetwork]に干渉します。
 *
 */
fun World.processEnergy(flag: HTEnergyNetwork.Flag, amount: Long, parent: TransactionContext? = null): Boolean =
    flag.processAmount(getEnergyNetwork(), amount, parent)
