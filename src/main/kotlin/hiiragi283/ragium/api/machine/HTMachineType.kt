package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.api.world.energyNetwork
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.util.useTransaction
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiPredicate

sealed class HTMachineType(builder: Builder) :
    HTMachineConvertible,
    HTMachineFactory by builder.factory {
    val id: Identifier = builder.id
    val frontTexId: Identifier = builder.frontTexId
    private val frontType: FrontType = builder.frontType

    val translationKey: String = Util.createTranslationKey("machine_type", id)
    val text: MutableText = Text.translatable(translationKey)
    val nameText: MutableText = Text.translatable(RagiumTranslationKeys.MACHINE_NAME, text).formatted(Formatting.WHITE)

    fun getFrontTexDir(facing: Direction): Direction = when (frontType) {
        FrontType.TOP -> Direction.UP
        FrontType.SIDE -> facing
    }

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

    data object Default : HTMachineType(
        Builder(RagiumAPI.id("default"))
            .frontTexId(RagiumAPI.id("block/alloy_furnace_front"))
            .frontType(FrontType.SIDE)
            .factory { _: BlockPos, _: BlockState, _: HTMachineType, _: HTMachineTier -> throw AssertionError("") },
    )

    //    Generator    //

    class Generator(
        builder: Builder,
        val fluidTag: TagKey<Fluid>?,
        private val predicate: BiPredicate<World, BlockPos>,
    ) : HTMachineType(builder) {
        fun process(world: World, pos: BlockPos, tier: HTMachineTier) {
            useTransaction { transaction: Transaction ->
                // Try to consumer fluid
                FluidStorage.SIDED.find(world, pos, world.getBlockState(pos), world.getBlockEntity(pos), null)
                    ?.let { storage: Storage<FluidVariant> ->
                        StorageUtil.extractAny(storage, FluidConstants.BUCKET, transaction)
                            ?.let { resourceAmount: ResourceAmount<FluidVariant> ->
                                if (resourceAmount.amount == FluidConstants.BUCKET && generateEnergy(world, tier, transaction)) {
                                    transaction.commit()
                                    return
                                }
                            }
                    }
                // check condition
                if (predicate.test(world, pos) && generateEnergy(world, tier, transaction)) {
                    transaction.commit()
                } else transaction.abort()
            }
        }
        
        private fun generateEnergy(world: World, tier: HTMachineTier, transaction: Transaction): Boolean =
            world.energyNetwork?.let { network: HTEnergyNetwork ->
                network.insert(tier.recipeCost, transaction) > 0
            } ?: false
    }

    //    Processor    //

    class Processor(builder: Builder, private val condition: HTMachineCondition) : HTMachineType(builder) {
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

    //    Builder    //

    class Builder(val id: Identifier) {
        lateinit var frontType: FrontType
        lateinit var factory: HTMachineFactory
        var frontTexId: Identifier = id.withPath { "block/${it}_front" }

        fun frontType(frontType: FrontType): Builder = apply {
            this.frontType = frontType
        }

        fun factory(factory: HTMachineFactory): Builder = apply {
            this.factory = factory
        }

        fun factory(factory: (BlockPos, BlockState, HTMachineTier) -> HTMachineBlockEntityBase): Builder =
            factory { pos: BlockPos, state: BlockState, _: HTMachineType, tier: HTMachineTier ->
                factory(pos, state, tier)
            }

        fun frontTexId(id: Identifier): Builder = apply {
            this.frontTexId = id
        }

        fun buildGenerator(fluidTag: TagKey<Fluid>?, predicate: BiPredicate<World, BlockPos>): Generator =
            Generator(this, fluidTag, predicate)

        fun buildProcessor(condition: HTMachineCondition): Processor = Processor(this, condition)
    }

    //    FrontType    //

    enum class FrontType {
        TOP,
        SIDE,
    }
}
