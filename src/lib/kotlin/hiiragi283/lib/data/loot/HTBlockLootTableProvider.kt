package hiiragi283.lib.data.loot

import hiiragi283.lib.resource.SupplierWithId
import java.util.function.Supplier
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

/**
 * Hiiragi Seriesで使用される[BlockLootSubProvider]の拡張クラスです。
 * @param rawBlocks この[HTBlockLootTableProvider]で登録するブロックの一覧
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTBlockLootTableProvider(
    registries: HolderLookup.Provider,
    protected val modId: String,
    private val rawBlocks: Sequence<SupplierWithId<Block>>,
) : BlockLootSubProvider(emptySet(), FeatureFlags.REGISTRY.allFlags(), registries) {
    final override fun getKnownBlocks(): Iterable<Block> = rawBlocks
        .filter { holder: SupplierWithId<Block> -> holder.namespace == modId }
        .map(SupplierWithId<Block>::get)
        .filter { block: Block -> block.lootTable.isPresent }
        .toList()

    //    Extensions    //

    /**
     * 幸運エンチャントのインスタンス
     */
    val fortune: Holder<Enchantment> by lazy { registries.holderOrThrow(Enchantments.FORTUNE) }

    protected fun dropSelf(like: Supplier<out Block>) {
        dropSelf(like.get())
    }

    protected fun add(like: Supplier<out Block>, table: LootTable.Builder) {
        add(like.get(), table)
    }

    protected inline fun <BLOCK : Block> add(like: Supplier<BLOCK>, factory: (BLOCK) -> LootTable.Builder) {
        val block: BLOCK = like.get()
        add(block, factory(block))
    }
}
