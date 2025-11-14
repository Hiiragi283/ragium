package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.Supplier

typealias HTFlowingFluidContent<TYPE> = HTFluidContent<TYPE, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing>

typealias HTSimpleFluidContent = HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing>

interface HTFluidContent<TYPE : FluidType, STILL : Fluid, FLOW : Fluid> :
    Supplier<STILL>,
    HTHolderLike {
    companion object {
        @JvmField
        val WATER: HTFluidContent<FluidType, FlowingFluid, FlowingFluid> =
            object : HTFluidContent<FluidType, FlowingFluid, FlowingFluid> {
                override val commonTag: TagKey<Fluid> = Tags.Fluids.WATER
                override val bucketTag: TagKey<Item> = Tags.Items.BUCKETS_WATER

                override fun getType(): FluidType = NeoForgeMod.WATER_TYPE.value()

                override fun getStill(): FlowingFluid = Fluids.WATER

                override fun getFlow(): FlowingFluid = Fluids.FLOWING_WATER

                override fun getBlock(): Block = Blocks.WATER

                override fun getBucket(): Item = Items.WATER_BUCKET

                override fun getId(): ResourceLocation = vanillaId("water")
            }

        @JvmField
        val LAVA: HTFluidContent<FluidType, FlowingFluid, FlowingFluid> =
            object : HTFluidContent<FluidType, FlowingFluid, FlowingFluid> {
                override val commonTag: TagKey<Fluid> = Tags.Fluids.LAVA
                override val bucketTag: TagKey<Item> = Tags.Items.BUCKETS_LAVA

                override fun getType(): FluidType = NeoForgeMod.LAVA_TYPE.value()

                override fun getStill(): FlowingFluid = Fluids.LAVA

                override fun getFlow(): FlowingFluid = Fluids.FLOWING_LAVA

                override fun getBlock(): Block = Blocks.LAVA

                override fun getBucket(): Item = Items.LAVA_BUCKET

                override fun getId(): ResourceLocation = vanillaId("lava")
            }

        @JvmField
        val MILK: HTFluidContent<FluidType, Fluid, Fluid> =
            object : HTFluidContent<FluidType, Fluid, Fluid> {
                override val commonTag: TagKey<Fluid> = Tags.Fluids.MILK
                override val bucketTag: TagKey<Item> = Tags.Items.BUCKETS_MILK

                override fun getType(): FluidType = NeoForgeMod.MILK_TYPE.value()

                override fun getStill(): Fluid = NeoForgeMod.MILK.get()

                override fun getFlow(): Fluid = NeoForgeMod.FLOWING_MILK.get()

                override fun getBlock(): Block = Blocks.AIR

                override fun getBucket(): Item = Items.MILK_BUCKET

                override fun getId(): ResourceLocation = vanillaId("milk")
            }
    }

    val commonTag: TagKey<Fluid>
    val bucketTag: TagKey<Item>

    fun getType(): TYPE

    fun getStill(): STILL

    fun getFlow(): FLOW

    fun getBlock(): Block

    fun getBucket(): Item

    override fun get(): STILL = getStill()

    fun isOf(stack: FluidStack): Boolean = stack.`is`(commonTag)

    fun isOf(stack: ImmutableFluidStack?): Boolean = stack?.isOf(commonTag) ?: false

    fun toStack(amount: Int): FluidStack = FluidStack(get(), amount)

    fun toStorageStack(amount: Int): ImmutableFluidStack? = toStack(amount).toImmutable()
}
