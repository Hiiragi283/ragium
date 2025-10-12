package hiiragi283.ragium.api.storage.fluid

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.SimpleFluidContent

fun HTFluidStorageStack.isOf(fluid: Fluid): Boolean = this.stack.`is`(fluid)

fun HTFluidStorageStack.isOf(tagKey: TagKey<Fluid>): Boolean = this.stack.`is`(tagKey)

fun HTFluidStorageStack.isOf(holder: Holder<Fluid>): Boolean = this.stack.`is`(holder)

fun HTFluidStorageStack.isOf(holderSet: HolderSet<Fluid>): Boolean = this.stack.`is`(holderSet)

fun HTFluidStorageStack.toContent(): SimpleFluidContent = SimpleFluidContent.copyOf(this.stack)

fun SimpleFluidContent.storageCopy(): HTFluidStorageStack = HTFluidStorageStack.of(this.copy())

@OnlyIn(Dist.CLIENT)
fun HTFluidStorageStack.getClientExtensions(): IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this.stack.fluidType)

@OnlyIn(Dist.CLIENT)
fun HTFluidStorageStack.getTintColor(): Int = this.getClientExtensions().getTintColor(this.stack)
