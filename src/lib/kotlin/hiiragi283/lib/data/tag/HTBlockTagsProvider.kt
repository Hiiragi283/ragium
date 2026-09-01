package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.asKeyOrValue
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

/**
 * [Block]向けの[HTTagBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTBlockTagsProvider : HTTagsProvider<Block> {
    constructor(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, parentProvider: CompletableFuture<TagLookup<Block>>, modId: String) : super(output, Registries.BLOCK, lookupProvider, parentProvider, modId)

    constructor(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, modId: String) : super(output, Registries.BLOCK, lookupProvider, modId)

    //    Extensions    //

    /**
     * 指定した要素をタグに追加します。
     * @param block ブロックの値
     */
    protected fun HTTagBuilder<Block>.addBlock(block: Block): HTTagBuilder<Block> = this.add(block.asKeyOrValue())

    protected fun createTag(prefix: HTTagPrefix, material: HTMaterialLike): TagKey<Block> = prefix.blockTagKey(material)

    /**
     * 新しい[HTTagBuilder]のインスタンスを作成します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    protected fun builder(prefix: HTTagPrefix, material: HTMaterialLike): HTTagBuilder<Block> = builders(prefix.rawCommonTag.block, prefix.blockTagKey(material))
}
