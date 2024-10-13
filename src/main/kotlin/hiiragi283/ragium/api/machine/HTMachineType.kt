package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.util.useTransaction
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.api.world.energyNetwork
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.component.ComponentType
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMachineType private constructor(properties: HTPropertyHolder) :
    HTMachineConvertible,
    HTPropertyHolder.Delegated {
        companion object {
            @JvmField
            val DEFAULT = HTMachineType(
                HTPropertyHolder.create {
                    set(HTMachinePropertyKeys.FRONT_TEX) { RagiumAPI.id("block/alloy_furnace_front") }
                },
            )

            @JvmField
            val COMPONENT_TYPE: ComponentType<HTMachineType> = ComponentType
                .builder<HTMachineType>()
                .codec(HTMachineTypeRegistry.CODEC)
                .packetCodec(HTMachineTypeRegistry.PACKET_CODEC)
                .build()

            @JvmStatic
            fun create(properties: HTPropertyHolder, category: Category): HTMachineType = HTMachineType(
                HTPropertyHolder.builder(properties).apply {
                    set(HTMachinePropertyKeys.CATEGORY, category)
                },
            )
        }

        val id: Identifier
            get() = RagiumAPI
                .getInstance()
                .machineTypeRegistry
                .getKeyOrThrow(this)
                .id

        val translationKey: String
            get() = Util.createTranslationKey("machine_type", id)
        val text: MutableText
            get() = Text.translatable(translationKey)

        fun appendTooltip(consumer: (Text) -> Unit, tier: HTMachineTier) {
            consumer(Text.translatable(RagiumTranslationKeys.MACHINE_NAME, text.formatted(Formatting.WHITE)))
            consumer(tier.tierText)
            consumer(tier.recipeCostText)
        }

        @Environment(EnvType.CLIENT)
        fun getFrontTex(machine: HTMachineEntity?): Identifier = machine
            ?.let { getOrDefault(HTMachinePropertyKeys.DYNAMIC_FRONT_TEX)(it) }
            ?: getOrDefault(HTMachinePropertyKeys.FRONT_TEX)(id)

        fun generateEnergy(world: World, pos: BlockPos, tier: HTMachineTier) {
            if (!isGenerator()) return
            useTransaction { transaction: Transaction ->
                // Try to consumer fluid
                FluidStorage.SIDED
                    .find(world, pos, world.getBlockState(pos), world.getBlockEntity(pos), null)
                    ?.let { storage: Storage<FluidVariant> ->
                        StorageUtil
                            .extractAny(storage, FluidConstants.BUCKET, transaction)
                            ?.let { resourceAmount: ResourceAmount<FluidVariant> ->
                                if (resourceAmount.amount == FluidConstants.BUCKET &&
                                    generateEnergy(
                                        world,
                                        tier,
                                        transaction,
                                    )
                                ) {
                                    transaction.commit()
                                    return
                                }
                            }
                    }
                // check condition
                if (getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE)(world, pos) &&
                    generateEnergy(
                        world,
                        tier,
                        transaction,
                    )
                ) {
                    transaction.commit()
                } else {
                    transaction.abort()
                }
            }
        }

        private fun generateEnergy(world: World, tier: HTMachineTier, transaction: Transaction): Boolean =
            world.energyNetwork?.let { network: HTEnergyNetwork ->
                network.insert(tier.recipeCost, transaction) > 0
            } ?: false

        //    HTMachineConvertible    //

        override fun asMachine(): HTMachineType = this

        //    HTPropertyHolder.Delegated    //

        override var delegated: HTPropertyHolder = properties
            internal set

        //    Category    //

        enum class Category {
            GENERATOR,
            PROCESSOR,
        }
    }
