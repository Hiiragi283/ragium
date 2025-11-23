package hiiragi283.ragium.api.block.entity

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.function.identity
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.tier.HTBaseTier
import net.minecraft.world.level.ItemLike
import java.util.function.UnaryOperator

interface HTUpgradableBlockEntity {
    /**
     * 指定した[item]を保持しているかどうか判定します。
     */
    fun hasUpgrade(item: ItemLike): Boolean

    /**
     * 現在保持しているアップグレードの一覧を返します。。
     */
    fun getMachineUpgrades(): List<Pair<HTMachineUpgrade, Int>>

    /**
     * アップグレードスロットから機械の最大のティアを返します。
     * @return ティアが見つからない場合は`null`
     */
    fun getMaxMachineTier(): HTBaseTier? = getMachineUpgrades()
        .mapNotNull { (upgrade: HTMachineUpgrade, _) -> upgrade.getBaseTier() }
        .maxOrNull()

    fun collectModifier(key: HTMachineUpgrade.Key): Float {
        var result = 1f
        for ((upgrade: HTMachineUpgrade, count: Int) in getMachineUpgrades()) {
            if (upgrade.getBaseTier() == HTBaseTier.CREATIVE) continue
            val multiplier: Float = upgrade.getProperty(key) ?: continue
            result *= (multiplier * count)
        }
        return result
    }

    fun calculateValue(key: HTMachineUpgrade.Key): Either<Int, Float> {
        var result = 1f
        for ((upgrade: HTMachineUpgrade, count: Int) in getMachineUpgrades()) {
            if (upgrade.getBaseTier() == HTBaseTier.CREATIVE) {
                return Either.left(key.creativeValue)
            }
            val multiplier: Float = upgrade.getProperty(key) ?: continue
            result *= (multiplier * count)
        }
        return Either.right(result)
    }

    fun modifyValue(key: HTMachineUpgrade.Key, factory: UnaryOperator<Float>): Int =
        calculateValue(key).map(identity()) { factory.apply(it).toInt() }
}
