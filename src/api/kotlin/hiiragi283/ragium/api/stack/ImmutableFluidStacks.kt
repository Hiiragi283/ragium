package hiiragi283.ragium.api.stack

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

fun FluidStack.toImmutable(): ImmutableFluidStack? = ImmutableFluidStack.of(this)

fun ImmutableFluidStack.isOf(fluid: Fluid): Boolean = this.stack.`is`(fluid)

fun ImmutableFluidStack.isOf(tagKey: TagKey<Fluid>): Boolean = this.stack.`is`(tagKey)

fun ImmutableFluidStack.isOf(holder: Holder<Fluid>): Boolean = this.stack.`is`(holder)

fun ImmutableFluidStack.isOf(holderSet: HolderSet<Fluid>): Boolean = this.stack.`is`(holderSet)

fun ImmutableFluidStack.fluidType(): FluidType = this.stack.fluidType

fun ImmutableFluidStack.isLighterThanAir(): Boolean = this.fluidType().isLighterThanAir

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getClientExtensions(): IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this.fluidType())

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getStillTexture(): ResourceLocation? = this.getClientExtensions().getStillTexture(this.stack)

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getTintColor(): Int = this.getClientExtensions().getTintColor(this.stack)
