package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.TriConsumer
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Rarity
import java.util.function.BiConsumer
import java.util.function.Function

/**
 * Ragiumのプラグイン向けのインターフェース
 */
@JvmDefaultWithCompatibility
interface RagiumPlugin {
    companion object {
        /**
         * サーバー側で読み込まれるエントリポイントのキー
         */
        const val SERVER_KEY = "ragium.plugin.server"

        /**
         * クライアント側で読み込まれるエントリポイントのキー
         */
        const val CLIENT_KEY = "ragium.plugin.client"
    }

    /**
     * プラグインを読み込む優先度
     *
     * 値が小さいほど優先して読み込まれます。
     */
    val priority: Int

    /**
     * プラグインを読み込むかどうか判定します。
     */
    fun shouldLoad(): Boolean = true

    /**
     * [HTMachineKey]を登録します。
     * @param consumer [HTMachineKey]をそのタイプを渡すブロック
     * @sample [hiiragi283.ragium.common.internal.RagiumDefaultPlugin.registerMachine]
     */
    fun registerMachine(consumer: BiConsumer<HTMachineKey, HTMachineType>) {}

    /**
     * [HTMaterialKey]を登録します。
     *
     *　@sample [hiiragi283.ragium.common.internal.DefaultMaterialPlugin.registerMaterial]
     */
    fun registerMaterial(helper: MaterialHelper) {}

    /**
     * 機械のプロパティを設定します。
     *
     * @sample [hiiragi283.ragium.common.internal.RagiumDefaultPlugin.setupMachineProperties]
     */
    fun setupMachineProperties(helper: Function<HTMachineKey, HTPropertyHolderBuilder>) {}

    /**
     * 素材のプロパティを設定します。
     *
     * @sample [hiiragi283.ragium.common.internal.DefaultMaterialPlugin.setupMaterialProperties]
     */
    fun setupMaterialProperties(helper: Function<HTMaterialKey, HTPropertyHolderBuilder>) {}

    /**
     * アイテムを素材とプレフィックスに紐づけます。
     *
     * @sample [hiiragi283.ragium.common.internal.DefaultMaterialPlugin.bindMaterialToItem]
     */
    fun bindMaterialToItem(consumer: TriConsumer<HTTagPrefix, HTMaterialKey, ItemConvertible>) {}

    /**
     * Ragiumが読み込まれた後に呼び出されます。
     */
    fun afterRagiumInit(instance: RagiumAPI) {}

    /**
     * レシピを動的に登録します。
     *
     * [net.minecraft.recipe.RecipeManager.apply]の最後にフックされます。
     */
    fun registerRuntimeRecipe(exporter: RecipeExporter, lookup: RegistryWrapper.WrapperLookup, helper: RecipeHelper) {}

    /**
     * 素材データに基づいたレシピを動的に登録します。
     *
     * [net.minecraft.recipe.RecipeManager.apply]の最後にフックされます。
     */
    fun registerRuntimeMaterialRecipes(
        exporter: RecipeExporter,
        key: HTMaterialKey,
        entry: HTMaterialRegistry.Entry,
        helper: RecipeHelper,
    ) {}

    //    MaterialHelper    //

    class MaterialHelper(
        private val consumer: (HTMaterialKey, HTMaterialType, Rarity) -> Unit,
        private val altConsumer: (HTMaterialKey, String) -> Unit,
    ) {
        /**
         * 指定した値から素材を登録します。
         * @param key 素材のキー
         * @param type 素材の種類
         * @param rarity 素材のレアリティ
         */
        fun register(key: HTMaterialKey, type: HTMaterialType, rarity: Rarity = Rarity.COMMON) {
            consumer(key, type, rarity)
        }

        /**
         * 素材の別名を登録します。
         * @param parent 統一する素材のキー
         * @param child 統一される素材のキー
         */
        fun addAltName(parent: HTMaterialKey, child: String) {
            altConsumer(parent, child)
        }
    }

    //    RecipeHelper    //

    data object RecipeHelper {
        fun useItemIfPresent(key: HTMaterialKey, prefix: HTTagPrefix, action: (Item) -> Unit) {
            val entry: HTMaterialRegistry.Entry = key.getEntryOrNull() ?: return
            useItemIfPresent(entry, prefix, action)
        }

        /**
         * 動的に完成品を取得します。
         * @param entry 素材のエントリ
         * @param prefix 完成品に紐づいたプレフィックス
         * @param action 完成品を扱うブロック
         */
        fun useItemIfPresent(entry: HTMaterialRegistry.Entry, prefix: HTTagPrefix, action: (Item) -> Unit) {
            entry.getFirstItemOrNull(prefix)?.let(action)
        }

        fun useItemFromMainPrefix(key: HTMaterialKey, action: (Item) -> Unit) {
            val entry: HTMaterialRegistry.Entry = key.getEntryOrNull() ?: return
            useItemFromMainPrefix(entry, action)
        }

        /**
         * [HTMaterialType.getMainPrefix]を元に動的に完成品を取得します。
         * @param entry 素材のエントリ
         * @param action 完成品を扱うブロック
         */
        fun useItemFromMainPrefix(entry: HTMaterialRegistry.Entry, action: (Item) -> Unit) {
            entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
                useItemIfPresent(entry, prefix, action)
            }
        }

        fun useItemFromRawPrefix(key: HTMaterialKey, action: (Item) -> Unit) {
            val entry: HTMaterialRegistry.Entry = key.getEntryOrNull() ?: return
            useItemFromRawPrefix(entry, action)
        }

        /**
         * [HTMaterialType.getRawPrefix]を元に動的に完成品を取得します。
         * @param entry 素材のエントリ
         * @param action 完成品を扱うブロック
         */
        fun useItemFromRawPrefix(entry: HTMaterialRegistry.Entry, action: (Item) -> Unit) {
            entry.type.getRawPrefix()?.let { prefix: HTTagPrefix ->
                useItemIfPresent(entry, prefix, action)
            }
        }
    }
}
