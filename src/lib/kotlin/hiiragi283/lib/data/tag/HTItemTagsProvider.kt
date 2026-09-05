package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.asKeyOrValue
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.concurrent.CompletableFuture

/**
 * [Item]向けの[HTTagBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTItemTagsProvider : HTTagsProvider<Item> {
    constructor(
        output: PackOutput,
        lookupProvider: CompletableFuture<HolderLookup.Provider>,
        parentProvider: CompletableFuture<TagLookup<Item>>,
        modId: String
    ) : super(output, Registries.ITEM, lookupProvider, parentProvider, modId)

    constructor(
        output: PackOutput,
        lookupProvider: CompletableFuture<HolderLookup.Provider>,
        modId: String
    ) : super(output, Registries.ITEM, lookupProvider, modId)

    //    Extensions    //

    /**
     * 指定した要素をタグに追加します。
     * @param item アイテムの値
     */
    protected fun HTTagBuilder<Item>.addItem(item: ItemLike): HTTagBuilder<Item> =
        this.add(item.asItem().asKeyOrValue())

    protected fun createTag(prefix: HTTagPrefix, material: HTMaterialLike): TagKey<Item> = prefix.itemTagKey(material)

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    protected fun builder(prefix: HTTagPrefix, material: HTMaterialLike): HTTagBuilder<Item> =
        builders(prefix.rawCommonTag.item, prefix.itemTagKey(material))
}
