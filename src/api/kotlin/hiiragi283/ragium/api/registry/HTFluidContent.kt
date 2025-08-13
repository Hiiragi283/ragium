package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.vanillaId
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
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.function.Supplier

interface HTFluidContent<TYPE : FluidType, STILL : Fluid, FLOW : Fluid> : Supplier<STILL> {
    companion object {
        @JvmField
        val WATER: HTFluidContent<FluidType, FlowingFluid, FlowingFluid> =
            object : HTFluidContent<FluidType, FlowingFluid, FlowingFluid> {
                override val id: ResourceLocation = vanillaId("water")
                override val commonTag: TagKey<Fluid> = Tags.Fluids.WATER
                override val bucketTag: TagKey<Item> = Tags.Items.BUCKETS_WATER

                override fun getType(): FluidType = NeoForgeMod.WATER_TYPE.value()

                override fun getStill(): FlowingFluid = Fluids.WATER

                override fun getFlow(): FlowingFluid = Fluids.FLOWING_WATER

                override fun getBlock(): Block = Blocks.WATER

                override fun getBucket(): Item = Items.WATER_BUCKET
            }

        @JvmField
        val LAVA: HTFluidContent<FluidType, FlowingFluid, FlowingFluid> =
            object : HTFluidContent<FluidType, FlowingFluid, FlowingFluid> {
                override val id: ResourceLocation = vanillaId("lava")
                override val commonTag: TagKey<Fluid> = Tags.Fluids.LAVA
                override val bucketTag: TagKey<Item> = Tags.Items.BUCKETS_LAVA

                override fun getType(): FluidType = NeoForgeMod.LAVA_TYPE.value()

                override fun getStill(): FlowingFluid = Fluids.LAVA

                override fun getFlow(): FlowingFluid = Fluids.FLOWING_LAVA

                override fun getBlock(): Block = Blocks.LAVA

                override fun getBucket(): Item = Items.LAVA_BUCKET
            }
    }

    val id: ResourceLocation
    val commonTag: TagKey<Fluid>
    val bucketTag: TagKey<Item>

    fun getType(): TYPE

    fun getStill(): STILL

    fun getFlow(): FLOW

    fun getBlock(): Block

    fun getBucket(): Item

    override fun get(): STILL = getStill()

    fun toStack(amount: Int): FluidStack = FluidStack(get(), amount)

    fun toIngredient(): FluidIngredient = FluidIngredient.tag(commonTag)

    fun toIngredient(amount: Int): SizedFluidIngredient = SizedFluidIngredient(toIngredient(), amount)
}
