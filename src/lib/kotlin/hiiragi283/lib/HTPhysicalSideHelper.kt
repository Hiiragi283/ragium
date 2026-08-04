package hiiragi283.lib

import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.registry.lookupResult
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.flatMap
import hiiragi283.lib.util.toTextResult
import net.minecraft.client.Minecraft
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.server.MinecraftServer
import net.minecraft.world.flag.FeatureElement
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.crafting.RecipeMap
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RecipesReceivedEvent
import net.neoforged.neoforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

/**
 * 物理サイドに関する処理を扱うクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@EventBusSubscriber
data object HTPhysicalSideHelper {
    /**
     * 現在の物理サイドに応じて処理を行います。
     * @param T 戻り値のクラス
     * @param client 物理クライアント側の処理
     * @param server 物理サーバー側の処理
     * @return 各ブロックでの戻り値
     */
    @JvmStatic
    inline fun <T> runForSide(client: (Level) -> T, server: (MinecraftServer) -> T): T? = runForDist(
        { Minecraft.getInstance().level?.let(client) },
        { ServerLifecycleHooks.getCurrentServer()?.let(server) },
    )

    //    Registry    //

    /**
     * 現在の[レジストリへのアクセス][RegistryAccess]を取得します。
     * @return クライアント側でワールドを読み込んでいない，またはサーバーのインスタンスが作成されていない場合は`null`
     */
    @JvmStatic
    fun getRegistryAccess(): HTTextResult<RegistryAccess> = runForSide(Level::registryAccess, MinecraftServer::registryAccess).toTextResult { "Could not get active registry access" }

    @JvmStatic
    fun <T : Any> registry(registryKey: RegistryKey<T>): HTTextResult<Registry<T>> = getRegistryAccess().flatMap { it.lookupResult(registryKey) }

    //    Feature Flag    //

    /**
     * 現在の[FeatureFlagSet]を取得します。
     * @return クライアント側でワールドを読み込んでいない，またはサーバーのインスタンスが作成されていない場合は`null`
     **/
    @JvmStatic
    fun getFeatureFlags(): FeatureFlagSet = runForSide(Level::enabledFeatures) { it.worldData.enabledFeatures() } ?: FeatureFlags.DEFAULT_FLAGS

    @JvmStatic
    fun <T : FeatureElement> filteredLookup(registryKey: RegistryKey<T>): HTTextResult<HolderLookup.RegistryLookup<T>> = registry(registryKey).map { it.filterFeatures(getFeatureFlags()) }

    //    RecipeMap    //

    @JvmStatic
    var cachedRecipes: RecipeMap = RecipeMap.EMPTY
        private set

    @SubscribeEvent
    fun onRecipeSync(event: RecipesReceivedEvent) {
        cachedRecipes = event.recipeMap
    }
}
