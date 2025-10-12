package hiiragi283.ragium.api.storage.fluid

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.SimpleFluidContent

fun ImmutableFluidStack.isOf(fluid: Fluid): Boolean = this.stack.`is`(fluid)

fun ImmutableFluidStack.isOf(tagKey: TagKey<Fluid>): Boolean = this.stack.`is`(tagKey)

fun ImmutableFluidStack.isOf(holder: Holder<Fluid>): Boolean = this.stack.`is`(holder)

fun ImmutableFluidStack.isOf(holderSet: HolderSet<Fluid>): Boolean = this.stack.`is`(holderSet)

fun ImmutableFluidStack.toContent(): SimpleFluidContent = SimpleFluidContent.copyOf(this.stack)

fun SimpleFluidContent.storageCopy(): ImmutableFluidStack = ImmutableFluidStack.of(this.copy())

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getClientExtensions(): IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this.stack.fluidType)

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getTintColor(): Int = this.getClientExtensions().getTintColor(this.stack)
