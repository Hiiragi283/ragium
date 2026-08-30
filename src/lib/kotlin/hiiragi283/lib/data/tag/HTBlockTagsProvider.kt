package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.asSupplier
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block

/**
 * [Block]向けの[HTTagBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, modId: String) : HTTagsProvider.DataGen<Block>(output, Registries.BLOCK, lookupProvider, modId) {
    //    Extensions    //

    /**
     * 指定した要素をタグに追加します。
     * @param block ブロックの値
     */
    protected fun HTTagBuilder<Block>.addBlock(block: Block): HTTagBuilder<Block> = this.add(block.asSupplier())
}
