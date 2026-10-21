package hiiragi283.lib.data.loot

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.loot.AddTableLootModifier
import net.neoforged.neoforge.common.loot.LootTableIdCondition
import java.util.Optional
import java.util.concurrent.CompletableFuture

/**
 * Hiiragi Seriesで使用される[GlobalLootModifierProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTGlobalLootModifierProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>,
    modid: String
) : GlobalLootModifierProvider(output, registries, modid) {
    /**
     * GLMを追加します。
     * @param key 参照するルートテーブルの[ResourceKey]
     * @param condition [key]のルートテーブルを適応するかどうかの条件
     * @param priority GLMの優先度
     * @since 26.1.8
     */
    protected fun add(key: ResourceKey<LootTable>, condition: LootItemCondition, priority: Int = 0) {
        add(key, Holder.direct(condition), priority)
    }

    /**
     * GLMを追加します。
     * @param key 参照するルートテーブルの[ResourceKey]
     * @param condition [key]のルートテーブルを適応するかどうかの条件
     * @param priority GLMの優先度
     * @since 26.3.0
     */
    protected fun add(key: ResourceKey<LootTable>, condition: Holder<LootItemCondition>, priority: Int = 0) {
        add(key, Optional.of(condition), priority)
    }

    /**
     * GLMを追加します。
     * @param key 参照するルートテーブルの[ResourceKey]
     * @param condition [key]のルートテーブルを適応するかどうかの条件
     * @param priority GLMの優先度
     * @since 26.3.0
     */
    protected fun add(key: ResourceKey<LootTable>, condition: Optional<Holder<LootItemCondition>>, priority: Int = 0) {
        add(key.identifier().path, AddTableLootModifier(condition, priority, key))
    }

    /**
     * 新しい[LootItemCondition]のインスタンスを作成します。
     * @param key ルートテーブルの参照先
     */
    protected fun condition(key: ResourceKey<LootTable>): Holder<LootItemCondition> =
        Holder.direct(LootTableIdCondition.Builder(key.identifier()).build())

    /**
     * 新しい[LootItemCondition]のインスタンスを作成します。
     * @param block ルートテーブルの参照先となるブロック
     * @return [Block.getLootTable]が空の場合は[Optional.empty]
     */
    protected fun condition(block: Block): Optional<Holder<LootItemCondition>> = block.lootTable.map(::condition)

    /**
     * 新しい[LootItemCondition]のインスタンスを作成します。
     * @param entityType ルートテーブルの参照先となるエンティティの種類
     * @return [EntityType.getDefaultLootTable]が空の場合は[Optional.empty]
     */
    protected fun condition(entityType: EntityType<*>): Optional<Holder<LootItemCondition>> =
        entityType.defaultLootTable.map(::condition)
}
