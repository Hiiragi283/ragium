package hiiragi283.ragium.common.machine

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.util.useTransaction
import hiiragi283.ragium.common.world.HTEnergyNetwork
import hiiragi283.ragium.common.world.energyNetwork
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryWrapper
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiPredicate

sealed class HTMachineType(val id: Identifier) : HTMachineConvertible {
    open val frontTexId: Identifier = id.withPath { "block/${it}_front" }

    val translationKey: String = Util.createTranslationKey("machine_type", id)
    val text: MutableText = Text.translatable(translationKey)
    val nameText: MutableText = Text.translatable(RagiumTranslationKeys.MACHINE_NAME, text).formatted(Formatting.WHITE)

    abstract fun getFrontTexDir(facing: Direction): Direction

    fun appendTooltip(
        stack: ItemStack,
        lookup: RegistryWrapper.WrapperLookup?,
        consumer: (Text) -> Unit,
        tier: HTMachineTier,
    ) {
        consumer(nameText)
        consumer(tier.tierText)
        consumer(tier.recipeCostText)
        consumer(tier.energyCapacityText)
    }

    override fun asMachine(): HTMachineType = this

    //    Default    //

    data object Default : HTMachineType(Ragium.id("default")) {
        override val frontTexId: Identifier = id.withPath { "block/alloy_furnace_front" }

        override fun getFrontTexDir(facing: Direction): Direction = facing
    }

    //    Generator    //

    class Generator(id: Identifier, val predicate: BiPredicate<World, BlockPos>) : HTMachineType(id) {
        override fun getFrontTexDir(facing: Direction): Direction = Direction.UP

        fun process(
            world: World,
            pos: BlockPos,
            type: HTMachineType,
            tier: HTMachineTier,
        ) {
            if (predicate.test(world, pos)) {
                world.energyNetwork?.let { network: HTEnergyNetwork ->
                    useTransaction { transaction: Transaction ->
                        val inserted: Long = network.insert(tier.recipeCost, transaction)
                        when {
                            inserted > 0 -> transaction.commit()
                            else -> transaction.abort()
                        }
                    }
                }
            }
        }
    }

    //    Processor    //

    class Processor(id: Identifier, val condition: HTMachineCondition) : HTMachineType(id) {
        override fun getFrontTexDir(facing: Direction): Direction = facing

        fun match(
            world: World,
            pos: BlockPos,
            type: HTMachineType,
            tier: HTMachineTier,
        ): Boolean = condition.condition.match(world, pos, type, tier)

        fun onSucceeded(
            world: World,
            pos: BlockPos,
            type: HTMachineType,
            tier: HTMachineTier,
        ) {
            condition.succeeded.onSucceeded(world, pos, type, tier)
        }

        fun onFailed(
            world: World,
            pos: BlockPos,
            type: HTMachineType,
            tier: HTMachineTier,
        ) {
            condition.failed.onFailed(world, pos, type, tier)
        }
    }
}
