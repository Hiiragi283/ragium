package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.energy.HTEnergyType
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.energyNetwork
import hiiragi283.ragium.api.extension.packetCodecOf
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.component.ComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

sealed class HTMachineType private constructor(properties: HTPropertyHolder) :
    HTMachineConvertible,
    HTPropertyHolder.Delegated {
        companion object {
            @JvmField
            val COMPONENT_TYPE: ComponentType<HTMachineType> = ComponentType
                .builder<HTMachineType>()
                .codec(HTMachineTypeRegistry.CODEC)
                .packetCodec(HTMachineTypeRegistry.PACKET_CODEC)
                .build()
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

        //    HTMachineConvertible    //

        override fun asMachine(): HTMachineType = this

        //    HTPropertyHolder.Delegated    //

        override var delegated: HTPropertyHolder = properties
            internal set

        //    Default    //

        data object Default : HTMachineType(
            HTPropertyHolder.create {
                set(HTMachinePropertyKeys.FRONT_TEX) { RagiumAPI.id("block/alloy_furnace_front") }
            },
        )

        //    Generator    //

        class Generator(properties: HTPropertyHolder) : HTMachineType(properties) {
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
                    network.insert(HTEnergyType.ELECTRICITY, tier.recipeCost, transaction) > 0
                } ?: false
        }

        //    Processor    //

        class Processor(properties: HTPropertyHolder) : HTMachineType(properties)

        //    Size    //

        enum class Size(val invSize: Int) : StringIdentifiable {
            SIMPLE(5) {
                override fun createInventory(): HTSimpleInventory = HTStorageBuilder(5)
                    .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
                    .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
                    .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
                    .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                    .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                    .buildSimple()
            },
            LARGE(7) {
                override fun createInventory(): HTSimpleInventory = HTStorageBuilder(7)
                    .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
                    .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
                    .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
                    .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
                    .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                    .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                    .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                    .buildSimple()
            },
            ;

            companion object {
                @JvmField
                val CODEC: Codec<Size> = codecOf(entries)

                @JvmField
                val PACKET_CODEC: PacketCodec<RegistryByteBuf, Size> = packetCodecOf(entries)
            }

            abstract fun createInventory(): HTSimpleInventory

            override fun asString(): String = name.lowercase()
        }
    }
