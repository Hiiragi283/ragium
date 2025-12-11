package hiiragi283.ragium.common.block.entity

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.function.identity
import hiiragi283.ragium.api.item.component.HTItemContents
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.item.component.HTStackContents
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.serialization.component.HTComponentInput
import hiiragi283.ragium.api.serialization.component.HTComponentSerializable
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.world.sendBlockUpdated
import hiiragi283.ragium.common.block.entity.HTBlockEntity.Companion.getBlockEntityType
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import org.apache.commons.lang3.math.Fraction
import java.util.function.UnaryOperator

class HTMachineUpgradeComponent(
    private val blockHolder: Holder<Block>,
    private val pos: BlockPos,
    private val levelGetter: () -> Level?,
    private val listener: HTContentListener,
) : HTComponentSerializable,
    HTValueSerializable {
    private val access: RegistryAccess? get() = levelGetter()?.registryAccess()

    val upgradeSlots: List<HTBasicItemSlot> = (0..3).map { i: Int ->
        val filter: (ImmutableItemStack) -> Boolean = filter@{ stack: ImmutableItemStack ->
            canApplyUpgrade(stack.unwrap()) && !hasUpgrade(stack.value())
        }
        HTBasicItemSlot.create(
            listener.andThen { levelGetter()?.sendBlockUpdated(pos) },
            HTSlotHelper.getSlotPosX(8),
            HTSlotHelper.getSlotPosY(i - 0.5),
            canExtract = HTPredicates.manualOnly(),
            canInsert = HTPredicates.manualOnly(),
            filter = filter,
        )
    }

    /**
     * 指定した[item]を保持しているかどうか判定します。
     */
    fun hasUpgrade(item: ItemLike): Boolean = upgradeSlots.any { slot: HTBasicItemSlot ->
        slot.getStack()?.isOf(item.asItem()) ?: false
    }

    /**
     * 現在保持しているアップグレードの一覧を返します。。
     */
    fun getMachineUpgrades(): List<Pair<HTMachineUpgrade, Int>> = upgradeSlots.mapNotNull { slot: HTBasicItemSlot ->
        val upgrade: HTMachineUpgrade = slot
            .getStack()
            ?.unwrap()
            ?.let { RagiumPlatform.INSTANCE.getMachineUpgrade(access, it) }
            ?: return@mapNotNull null
        upgrade to slot.getAmount()
    }

    fun canApplyUpgrade(stack: ItemStack): Boolean {
        if (RagiumPlatform.INSTANCE.getMachineUpgrade(access, stack) == null) return false
        val filter: HTKeyOrTagEntry<BlockEntityType<*>> = stack.get(RagiumDataComponents.MACHINE_UPGRADE_FILTER) ?: return true
        return filter.isOf(getBlockEntityType(this.blockHolder))
    }

    /**
     * アップグレードスロットから機械の最大のティアを返します。
     * @return ティアが見つからない場合は`null`
     */
    fun getMaxMachineTier(): HTBaseTier? = getMachineUpgrades()
        .mapNotNull { (upgrade: HTMachineUpgrade, _) -> upgrade.getBaseTier() }
        .maxOrNull()

    fun collectModifier(key: HTMachineUpgrade.Key): Fraction {
        var result: Fraction = Fraction.ONE
        for ((upgrade: HTMachineUpgrade, count: Int) in getMachineUpgrades()) {
            if (upgrade.getBaseTier() == HTBaseTier.CREATIVE) continue
            val multiplier: Fraction = upgrade.getProperty(key) ?: continue
            result *= (multiplier * count)
        }
        return result
    }

    fun collectAllModifier(): Map<HTMachineUpgrade.Key, Fraction> = HTMachineUpgrade.Key.entries.associateWith(::collectModifier)

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

    //    HTComponentSerializable    //

    override fun applyComponents(input: HTComponentInput) {
        input.use(RagiumDataComponents.MACHINE_UPGRADES) { contents: HTItemContents ->
            for (i: Int in contents.indices) {
                upgradeSlots.getOrNull(i)?.setStackUnchecked(contents[i])
            }
        }
    }

    override fun collectComponents(builder: DataComponentMap.Builder) {
        builder.set(
            RagiumDataComponents.MACHINE_UPGRADES,
            upgradeSlots
                .map(HTBasicItemSlot::getStack)
                .let(HTStackContents.Companion::fromNullable),
        )
    }

    //    HTValueSerializable    //

    override fun serialize(output: HTValueOutput) {
        HTCapabilityCodec.ITEM.saveTo(output.child("upgrades"), upgradeSlots)
    }

    override fun deserialize(input: HTValueInput) {
        HTCapabilityCodec.ITEM.loadFrom(input.childOrEmpty("upgrades"), upgradeSlots)
    }
}
