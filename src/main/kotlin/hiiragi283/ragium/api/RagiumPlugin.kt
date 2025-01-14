package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import net.neoforged.fml.IExtensionPoint
import java.util.function.BiConsumer
import java.util.function.Function

/**
 * Ragiumのプラグイン向けのインターフェース
 */
interface RagiumPlugin {
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
     */
    fun registerMachine(consumer: BiConsumer<HTMachineKey, HTMachineType>) {}

    /**
     * 機械のプロパティを設定します。
     */
    fun setupMachineProperties(helper: Function<HTMachineKey, HTPropertyHolderBuilder>) {}

    fun setupMaterialProperties(helper: Function<HTMaterialKey, HTPropertyHolderBuilder>) {}
    /*
    /**
     * レシピを動的に登録します。
     *
     * [net.minecraft.world.item.crafting.RecipeManager.apply]の最後にフックされます。
     */
    fun registerRuntimeRecipe(output: RecipeOutput, provider: HolderLookup.Provider, helper: RecipeHelper) {}

    /**
     * 素材データに基づいたレシピを動的に登録します。
     *
     * [net.minecraft.world.item.crafting.RecipeManager.apply]の最後にフックされます。
     */
    fun registerRuntimeMaterialRecipes(
        output: RecipeOutput,
        key: HTMaterialKey,
        entry: HTMaterialRegistry.Entry,
        helper: RecipeHelper,
    ) {}*/

    //    Provider    //

    fun interface Provider : IExtensionPoint {
        fun getPlugins(): List<RagiumPlugin>
    }

    //    RecipeHelper    //

    /*data object RecipeHelper {
        fun useItemIfPresent(key: HTMaterialKey, prefix: HTTagPrefix, action: (Holder<Item>) -> Unit) {
            val entry: HTMaterialRegistry.Entry = key.getEntryOrNull() ?: return
            useItemIfPresent(entry, prefix, action)
        }

        /**
     * 動的に完成品を取得します。
     * @param entry 素材のエントリ
     * @param prefix 完成品に紐づいたプレフィックス
     * @param action 完成品を扱うブロック
     */
        fun useItemIfPresent(entry: HTMaterialRegistry.Entry, prefix: HTTagPrefix, action: (Holder<Item>) -> Unit) {
            entry.getFirstItemOrNull(prefix)?.let(action)
        }

        fun useItemFromMainPrefix(key: HTMaterialKey, action: (Holder<Item>) -> Unit) {
            val entry: HTMaterialRegistry.Entry = key.getEntryOrNull() ?: return
            useItemFromMainPrefix(entry, action)
        }

        /**
     * [HTMaterialType.getMainPrefix]を元に動的に完成品を取得します。
     * @param entry 素材のエントリ
     * @param action 完成品を扱うブロック
     */
        fun useItemFromMainPrefix(entry: HTMaterialRegistry.Entry, action: (Holder<Item>) -> Unit) {
            entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
                useItemIfPresent(entry, prefix, action)
            }
        }

        fun useItemFromRawPrefix(key: HTMaterialKey, action: (Holder<Item>) -> Unit) {
            val entry: HTMaterialRegistry.Entry = key.getEntryOrNull() ?: return
            useItemFromRawPrefix(entry, action)
        }

        /**
     * [HTMaterialType.getRawPrefix]を元に動的に完成品を取得します。
     * @param entry 素材のエントリ
     * @param action 完成品を扱うブロック
     */
        fun useItemFromRawPrefix(entry: HTMaterialRegistry.Entry, action: (Holder<Item>) -> Unit) {
            entry.type.getRawPrefix()?.let { prefix: HTTagPrefix ->
                useItemIfPresent(entry, prefix, action)
            }
        }
    }*/
}
