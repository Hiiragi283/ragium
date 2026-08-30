package hiiragi283.lib.data.pack

import com.google.gson.JsonElement
import com.mojang.logging.LogUtils
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Encoder
import com.mojang.serialization.JsonOps
import hiiragi283.lib.data.recipe.HTRecipeExporter
import hiiragi283.lib.recipe.RecipeKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import org.slf4j.Logger

/**
 * 動的データパックの内容の追加を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
data object HTDynamicDataRegister {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val RECIPE_EXPORTER = HTRecipeExporter { id: RecipeKey, recipe: Recipe<*>, _ -> addToData(id, Recipe.CODEC, recipe) }

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
    fun <T : Any> addToData(id: Identifier, codec: Encoder<T>, value: T, ops: DynamicOps<JsonElement> = JsonOps.INSTANCE) {
        codec.encodeStart(ops, value)
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
    fun <T : Any> addToData(key: ResourceKey<T>, codec: Encoder<T>, value: T, ops: DynamicOps<JsonElement> = JsonOps.INSTANCE) {
        codec.encodeStart(ops, value)
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
    fun <T : Any> addToData(prefix: String, id: Identifier, codec: Encoder<T>, value: T, ops: DynamicOps<JsonElement> = JsonOps.INSTANCE) {
        codec.encodeStart(ops, value)
            .ifError { LOGGER.error(it.message()) }
            .ifSuccess { addToData(id.withPrefix("$prefix/"), it) }
    }
}
