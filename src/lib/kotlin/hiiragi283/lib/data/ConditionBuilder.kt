package hiiragi283.lib.data

import hiiragi283.lib.util.HTBuilderMarker
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.flag.FeatureFlag
import net.minecraft.world.flag.FeatureFlagSet
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.NeoForgeConditions

/**
 * [ICondition]の一覧を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
@HTBuilderMarker
@JvmInline
value class ConditionBuilder(private val conditions: MutableList<ICondition>) {
    /**
     * [ICondition]を追加します。
     */
    operator fun ICondition.unaryPlus() {
        conditions += this
    }

    /**
     * [ResourceKey]が登録されているか判定する[ICondition]を追加します。
     */
    operator fun <T : Any> ResourceKey<T>.unaryPlus() {
        +NeoForgeConditions.registered(this)
    }

    /**
     * [String]と同じIDを持つmodが登録されているか判定する[ICondition]を追加します。
     */
    operator fun String.unaryPlus() {
        +NeoForgeConditions.modLoaded(this)
    }

    /**
     * [TagKey]が登録されていないか判定する[ICondition]を追加します。
     */
    operator fun <T : Any> TagKey<T>.unaryMinus() {
        +NeoForgeConditions.tagEmpty(this)
    }

    /**
     * [TagKey]が登録されているか判定する[ICondition]を追加します。
     */
    operator fun <T : Any> TagKey<T>.unaryPlus() {
        +NeoForgeConditions.not(NeoForgeConditions.tagEmpty(this))
    }

    /**
     * [FeatureFlag]が登録されているか判定する[ICondition]を追加します。
     */
    operator fun FeatureFlag.unaryPlus() {
        +NeoForgeConditions.featureFlagsEnabled(this)
    }

    /**
     * [FeatureFlagSet]が登録されているか判定する[ICondition]を追加します。
     */
    operator fun FeatureFlagSet.unaryPlus() {
        +NeoForgeConditions.featureFlagsEnabled(this)
    }
}
