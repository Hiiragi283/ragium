package hiiragi283.lib.data.pack

import com.google.gson.JsonElement
import com.mojang.logging.LogUtils
import com.mojang.serialization.Encoder
import com.mojang.serialization.JsonOps
import com.mojang.serialization.Lifecycle
import hiiragi283.lib.data.recipe.HTRecipeExporter
import hiiragi283.lib.data.recipe.HTRecipeProviderContext
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.util.identity
import java.util.Optional
import java.util.stream.Stream
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.server.RegistryLayer
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import org.slf4j.Logger

/**
 * 動的データパックの内容の追加を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
data object HTDynamicDataRegister : HTRecipeProviderContext() {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    override val exporter: HTRecipeExporter = HTRecipeExporter { id: RecipeKey, recipe: Recipe<*>, _ -> addToData(id, Recipe.CODEC, recipe) }

    override val registries: HolderLookup.Provider = FakeLookupProvider

    /**
     * ルートテーブルを追加します。
     * @param block ルートテーブルを追加するブロック
     * @param factory ブロックから[LootTable.Builder]を作成するブロック
     */
    @JvmStatic
    fun addLootTable(block: Block, factory: (Block) -> LootTable.Builder) {
        block.lootTable.ifPresent { addToData(it, LootTable.DIRECT_CODEC, factory(block).build()) }
    }

    /**
     * [JsonElement]からデータを追加します。
     * @param id 追加するデータのパス
     * @param json 追加するデータ
     */
    @JvmStatic
    fun addToData(id: Identifier, json: JsonElement) {
        HTDynamicDatapack.addToData(id.withSuffix(".json"), json.toString().toByteArray())
    }

    /**
     * [Encoder]に基づいてデータを追加します。
     * @param id 追加するデータのパス
     * @param codec データの変換に使用するコーデック
     * @param value 追加するデータ
     */
    @JvmStatic
    fun <T : Any> addToData(id: Identifier, codec: Encoder<T>, value: T) {
        codec.encodeStart(registries.createSerializationContext(JsonOps.INSTANCE), value)
            .ifError { LOGGER.error(it.message()) }
            .ifSuccess { addToData(id, it) }
    }

    /**
     * [Encoder]に基づいてデータを追加します。
     * @param key 追加するデータのパス
     * @param codec データの変換に使用するコーデック
     * @param value 追加するデータ
     */
    @JvmStatic
    fun <T : Any> addToData(key: ResourceKey<T>, codec: Encoder<T>, value: T) {
        codec.encodeStart(registries.createSerializationContext(JsonOps.INSTANCE), value)
            .ifError { LOGGER.error(it.message()) }
            .ifSuccess { addToData(key.identifier().withPrefix("${Registries.elementsDirPath(key.registryKey())}/"), it) }
    }

    /**
     * [Encoder]に基づいてデータを追加します。
     * @param prefix パスのプレフィックス
     * @param id 追加するデータのパス
     * @param codec データの変換に使用するコーデック
     * @param value 追加するデータ
     */
    @JvmStatic
    fun <T : Any> addToData(prefix: String, id: Identifier, codec: Encoder<T>, value: T) {
        codec.encodeStart(registries.createSerializationContext(JsonOps.INSTANCE), value)
            .ifError { LOGGER.error(it.message()) }
            .ifSuccess { addToData(id.withPrefix("$prefix/"), it) }
    }

    /**
     * [Encoder]で使用される，偽の[HolderLookup.Provider]の実装クラスです。
     */
    private data object FakeLookupProvider : HolderLookup.Provider {
        override fun listRegistryKeys(): Stream<ResourceKey<out Registry<*>>> = Stream.empty()

        override fun <T : Any> lookup(registryKey: ResourceKey<out Registry<out T>>): Optional<HolderLookup.RegistryLookup<T>> {
            val staticLookup: Optional<Registry<T>> = RegistryLayer.createRegistryAccess().compositeAccess().lookup(registryKey)
            if (staticLookup.isPresent) return staticLookup.map(identity())
            return Optional.of(EmptyTagLookup(registryKey))
        }
    }

    /**
     * [FakeLookupProvider]で使用される，偽の[EmptyTagLookup]の実装クラスです。
     */
    @JvmRecord
    private data class EmptyTagLookup<T : Any>(@JvmField val registryKey: ResourceKey<out Registry<out T>>) : HolderLookup.RegistryLookup<T> {
        override fun key(): ResourceKey<out Registry<out T>> = registryKey

        override fun registryLifecycle(): Lifecycle = Lifecycle.stable()

        override fun listElements(): Stream<Holder.Reference<T>> = throw UnsupportedOperationException("Holders are not available in dynamic datapack")

        override fun listTags(): Stream<HolderSet.Named<T>> = throw UnsupportedOperationException("Tags are not available in dynamic datapack")

        override fun get(resourceKey: ResourceKey<T>): Optional<Holder.Reference<T>> = Optional.of(Holder.Reference.createStandAlone(this, resourceKey))

        @Suppress("DEPRECATION")
        override fun get(tagKey: TagKey<T>): Optional<HolderSet.Named<T>> = Optional.of(HolderSet.emptyNamed(this, tagKey))
    }
}
