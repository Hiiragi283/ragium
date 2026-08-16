@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.loot

import hiiragi283.lib.util.Option
import hiiragi283.lib.util.kotlin
import java.util.concurrent.CompletableFuture
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
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

/**
 * Hiiragi Seriesで使用される[GlobalLootModifierProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTGlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>, modid: String) : GlobalLootModifierProvider(output, registries, modid) {
    /**
     * GLMを追加します。
     * @param key 参照するルートテーブルの[ResourceKey]
     * @param conditions [key]のルートテーブルを適応するかどうかの条件の一覧
     * @param priority GLMの優先度
     */
    protected fun add(key: ResourceKey<LootTable>, conditions: Collection<LootItemCondition>, priority: Int = 0) {
        add(key.identifier().path, AddTableLootModifier(conditions.toTypedArray(), priority, key))
    }

    /**
     * GLMを追加します。
     * @param key 参照するルートテーブルの[ResourceKey]
     * @param priority GLMの優先度
     * @param builderAction [key]のルートテーブルを適応するかどうかの条件を作成するブロック
     */
    protected inline fun add(key: ResourceKey<LootTable>, priority: Int = 0, builderAction: MutableCollection<LootItemCondition>.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        add(key.identifier().path, AddTableLootModifier(buildList(builderAction).toTypedArray(), priority, key))
    }

    /**
     * 新しい[LootItemCondition]のインスタンスを作成します。
     * @param key ルートテーブルの参照先
     */
    protected fun condition(key: ResourceKey<LootTable>): LootItemCondition = LootTableIdCondition.Builder(key.identifier()).build()

    /**
     * 新しい[LootItemCondition]のインスタンスを作成します。
     * @param block ルートテーブルの参照先となるブロック
     * @return [Block.getLootTable]が空の場合は[Option.none]
     */
    protected fun condition(block: Block): Option<LootItemCondition> = block.lootTable.kotlin.map(::condition)

    /**
     * 新しい[LootItemCondition]のインスタンスを作成します。
     * @param entityType ルートテーブルの参照先となるエンティティの種類
     * @return [EntityType.getDefaultLootTable]が空の場合は[Option.none]
     */
    protected fun condition(entityType: EntityType<*>): Option<LootItemCondition> = entityType.defaultLootTable.kotlin.map(::condition)
}
