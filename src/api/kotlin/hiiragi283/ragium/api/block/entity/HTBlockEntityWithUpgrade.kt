package hiiragi283.ragium.api.block.entity

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.function.identity
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.tier.HTBaseTier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import org.apache.commons.lang3.math.Fraction
import java.util.function.UnaryOperator

interface HTBlockEntityWithUpgrade {
    /**
     * 指定した[item]を保持しているかどうか判定します。
     */
    fun hasUpgrade(item: ItemLike): Boolean

    /**
     * 現在保持しているアップグレードの一覧を返します。。
     */
    fun getMachineUpgrades(): List<Pair<HTMachineUpgrade, Int>>

    fun canApplyUpgrade(stack: ItemStack): Boolean

    /**
     * アップグレードスロットから機械の最大のティアを返します。
     * @return ティアが見つからない場合は`null`
     */
    fun getMaxMachineTier(): HTBaseTier? = getMachineUpgrades()
        .mapNotNull { (upgrade: HTMachineUpgrade, _) -> upgrade.getBaseTier() }
        .maxOrNull()

    fun collectAllModifier(): Map<HTMachineUpgrade.Key, Fraction> = HTMachineUpgrade.Key.entries.associateWith(::collectModifier)

    fun collectModifier(key: HTMachineUpgrade.Key): Fraction {
        var result: Fraction = Fraction.ONE
        for ((upgrade: HTMachineUpgrade, count: Int) in getMachineUpgrades()) {
            if (upgrade.getBaseTier() == HTBaseTier.CREATIVE) continue
            val multiplier: Fraction = upgrade.getProperty(key) ?: continue
            result *= (multiplier * count)
        }
        return result
    }

    fun calculateValue(key: HTMachineUpgrade.Key): Either<Int, Fraction> {
        var result: Fraction = Fraction.ONE
        for ((upgrade: HTMachineUpgrade, count: Int) in getMachineUpgrades()) {
            if (upgrade.getBaseTier() == HTBaseTier.CREATIVE) {
                return Either.left(key.creativeValue)
            }
            val multiplier: Fraction = upgrade.getProperty(key) ?: continue
            result *= (multiplier * count)
        }
        return Either.right(result)
    }

    fun modifyValue(key: HTMachineUpgrade.Key, factory: UnaryOperator<Fraction>): Int =
        calculateValue(key).map(identity()) { factory.apply(it).toInt() }
}
