package hiiragi283.ragium.api.stack

import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

fun FluidStack.toImmutable(): ImmutableFluidStack? = ImmutableFluidStack.of(this)

fun FluidStack.toImmutableOrThrow(): ImmutableFluidStack = this.toImmutable() ?: error("FluidStack must not be empty")

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getClientExtensions(): IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this.fluidType())

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getStillTexture(): ResourceLocation? = this.getClientExtensions().getStillTexture(this.unwrap())

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getTintColor(): Int = this.getClientExtensions().getTintColor(this.unwrap())
