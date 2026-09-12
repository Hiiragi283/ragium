package hiiragi283.lib.data

import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import hiiragi283.lib.HTComparators
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.resource.HTValueWithId
import hiiragi283.lib.resource.debugPath
import hiiragi283.lib.resource.toId
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.WithConditions
import net.neoforged.neoforge.registries.holdersets.OrHolderSet
import java.util.Optional
import java.util.concurrent.CompletableFuture

/**
 * [ConditionalExporter]に基づいだ[DataProvider]の実装クラスです。
 * @param R 登録する値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
abstract class ExporterDataProvider<R : Any>(
    packOutput: PackOutput,
    private val future: CompletableFuture<HolderLookup.Provider>,
    registryKey: RegistryKey<R>,
    protected val modId: String,
    private val codec: Codec<Optional<WithConditions<R>>>
) : DataProvider {
    private val pathProvider: PackOutput.PathProvider = packOutput.createRegistryElementsPathProvider(registryKey)

    /**
     * 値の登録先
     */
    protected lateinit var exporter: ConditionalExporter<R>
        private set

    /**
     * レジストリへのアクセス
     */
    protected lateinit var registries: HolderLookup.Provider
        private set

    final override fun run(cache: CachedOutput): CompletableFuture<*> = future
        .thenCompose { registries: HolderLookup.Provider ->
            val map: MutableMap<ResourceKey<R>, WithConditions<R>> = Object2ObjectOpenHashMap()
            this.registries = registries
            this.exporter = ConditionalExporter { id: ResourceKey<R>, value: R, conditions: List<ICondition> ->
                val fixedId: ResourceKey<R> = modifyId(id)
                val oldValue: WithConditions<R>? = map.put(fixedId, WithConditions(conditions, value))
                if (oldValue != null) {
                    error("Duplicate registration for $fixedId, new=$value, old=$oldValue")
                }
            }
            exportValues()

            val dynamicOps: RegistryOps<JsonElement> = registries.createSerializationContext(JsonOps.INSTANCE)
            DataProvider.saveAll(
                cache,
                { conditions: WithConditions<R> -> codec.encodeStart(dynamicOps, Optional.of(conditions)).orThrow },
                pathProvider::json,
                map
            )
        }

    /**
     * 値を登録します。
     */
    protected abstract fun exportValues()

    protected fun modifyId(key: ResourceKey<R>): ResourceKey<R> =
        ResourceKey.create(key.registryKey(), key.identifier().let(::modifyId))

    /**
     * 受け取った[id]を[exporter]内で変換します。
     */
    protected open fun modifyId(id: Identifier): Identifier = modId.toId(id.path)

    //    Extensions    //

    /**
     * 指定した[パス][path]から[ID][Identifier]を作成します。
     * @return [modId]を[名前空間][Identifier.getNamespace]とする[ID][Identifier]
     */
    protected fun id(path: String): Identifier = modId.toId(path)

    /**
     * 指定した[パス][path]から[ID][Identifier]を作成します。
     * @return [modId]を[名前空間][Identifier.getNamespace]とする[ID][Identifier]
     */
    protected fun id(vararg path: String): Identifier = modId.toId(*path)

    protected fun getHasName(id: Identifier): String = "has_${id.debugPath}"

    protected fun getHasName(value: HTValueWithId<*>): String = getHasName(value.idOrThrow)

    protected fun getHasName(tagKey: TagKey<*>): String = getHasName(tagKey.location())

    protected fun getHasName(prefix: HTTagPrefix, material: HTMaterialLike): String =
        getHasName(prefix.itemTagKey(material))

    // Registry

    /**
     * [HolderSet]を取得します。
     * @param tagKey 対応するタグ
     */
    protected fun <T : Any> holderSet(tagKey: TagKey<T>): HolderSet<T> = this.registries.getOrThrow(tagKey)

    protected fun <T : Any> holderSet(tagKeys: Iterable<TagKey<T>>): HolderSet<T> = when (tagKeys.count()) {
        0 -> HolderSet.empty()
        1 -> holderSet(tagKeys.first())
        else -> HTComparators.sortTagKeys(tagKeys).map(::holderSet).let(::OrHolderSet)
    }

    protected fun <T : Any> holderSet(vararg tagKeys: TagKey<T>): HolderSet<T> = holderSet(tagKeys.toSet())

    /**
     * [HolderSet]を取得します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    protected fun holderSet(prefix: HTTagPrefix, material: HTMaterialLike): HolderSet<Item> =
        holderSet(prefix.itemTagKey(material))

    protected fun holderSet(prefix: HTTagPrefix, vararg materials: HTMaterialLike): HolderSet<Item> =
        materials.map(prefix::itemTagKey).let(::holderSet)

    /**
     * [HolderSet]を取得します。
     * @param content 液体タグの提供元
     */
    protected fun holderSet(content: HTFluidContent): HolderSet<Fluid> = holderSet(content.fluidTag)

    protected fun waterSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.WATER)

    protected fun lavaSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.LAVA)

    protected fun milkSet(): HolderSet<Fluid> = holderSet(Tags.Fluids.MILK)
}
