package hiiragi283.lib

import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.registry.RegistryKey
import java.util.Optional
import net.minecraft.client.Minecraft
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.server.MinecraftServer
import net.minecraft.world.flag.FeatureElement
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.alchemy.PotionBrewing
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
    fun getRegistryAccess(): RegistryAccess = runForSide(Level::registryAccess, MinecraftServer::registryAccess) ?: RegistryAccess.EMPTY

    @JvmStatic
    fun <T : Any> registry(registryKey: RegistryKey<T>): Optional<Registry<T>> = getRegistryAccess().lookup(registryKey)

    //    Feature Flag    //

    /**
     * 現在の[FeatureFlagSet]を取得します。
     * @return クライアント側でワールドを読み込んでいない，またはサーバーのインスタンスが作成されていない場合は`null`
     **/
    @JvmStatic
    fun getFeatureFlags(): FeatureFlagSet = runForSide(Level::enabledFeatures) { it.worldData.enabledFeatures() } ?: FeatureFlags.DEFAULT_FLAGS

    @JvmStatic
    fun <T : FeatureElement> filteredLookup(registryKey: RegistryKey<T>): Optional<HolderLookup.RegistryLookup<T>> = registry(registryKey).map { it.filterFeatures(getFeatureFlags()) }

    @JvmStatic
    fun <T : FeatureElement> filteredLookup(registry: Registry<T>): HolderLookup.RegistryLookup<T> = registry.filterFeatures(getFeatureFlags())

    //    RecipeMap    //

    @JvmStatic
    var cachedRecipes: RecipeMap = RecipeMap.EMPTY
        private set

    /**
     * 現在の[HTRecipeLookup.Context]を作成します。
     * @since 21.1.1
     */
    @JvmStatic
    fun createLookupContext(): HTRecipeLookup.Context = runForSide(
        { level: Level -> HTRecipeLookup.Context(cachedRecipes, level.registryAccess()) },
        { server: MinecraftServer -> HTRecipeLookup.Context(server.recipeManager.recipeMap(), server.registryAccess()) },
    ) ?: HTRecipeLookup.Context.EMPTY

    @SubscribeEvent
    fun onRecipeSync(event: RecipesReceivedEvent) {
        cachedRecipes = event.recipeMap
    }

    //    PotionBrewing    //

    /**
     * 現在の[PotionBrewing]を取得します。
     * @since 21.1.1
     */
    @JvmStatic
    fun getPotionBrewing(): PotionBrewing? = runForSide(Level::potionBrewing, MinecraftServer::potionBrewing)
}
