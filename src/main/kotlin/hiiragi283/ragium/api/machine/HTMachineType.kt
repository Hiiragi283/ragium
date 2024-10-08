package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.api.world.energyNetwork
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.util.useTransaction
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryWrapper
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMachineType private constructor(val id: Identifier, properties: HTPropertyHolder) :
    HTMachineConvertible,
    HTPropertyHolder by properties {
        companion object {
            @JvmField
            val DEFAULT = HTMachineType(
                RagiumAPI.id("default"),
                HTPropertyHolder.EMPTY,
            )

            @JvmStatic
            fun createGenerator(id: Identifier, builderAction: HTPropertyHolder.Mutable.() -> Unit): HTMachineType = HTMachineType(
                id,
                HTPropertyHolder.create {
                    builderAction()
                    set(HTMachinePropertyKeys.CATEGORY, Category.GENERATOR)
                },
            )

            @JvmStatic
            fun createProcessor(id: Identifier, builderAction: HTPropertyHolder.Mutable.() -> Unit): HTMachineType = HTMachineType(
                id,
                HTPropertyHolder.create {
                    builderAction()
                    set(HTMachinePropertyKeys.CATEGORY, Category.PROCESSOR)
                },
            )
        }

        val frontTexId: Identifier = getOrDefault(HTMachinePropertyKeys.FRONT_TEX_ID).apply(id)

        val style: Style = getOrDefault(HTMachinePropertyKeys.STYLE)
        val category: Category? = get(HTMachinePropertyKeys.CATEGORY)

        val translationKey: String = Util.createTranslationKey("machine_type", id)
        val text: MutableText = Text.translatable(translationKey)
        val nameText: MutableText = Text.translatable(RagiumTranslationKeys.MACHINE_NAME, text).formatted(Formatting.WHITE)

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

    /*fun createMachine(pos: BlockPos, state: BlockState, tier: HTMachineTier): HTMachineBlockEntityBase? =
        get(HTMachinePropertyKeys.MACHINE_FACTORY)?.createMachine(pos, state, this, tier)*/

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
                if (getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE).test(world, pos) &&
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

        //    Style    //

        enum class Style {
            SINGLE,
            MULTI,
        }

        //    Category    //

        enum class Category {
            GENERATOR,
            PROCESSOR,
        }
    }
