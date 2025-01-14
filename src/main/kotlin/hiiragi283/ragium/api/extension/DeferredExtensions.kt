@file:Suppress("DEPRECATION")

package hiiragi283.ragium.api.extension

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

fun blockHolder(block: Block): DeferredBlock<out Block> =
    block.builtInRegistryHolder().key?.let(DeferredBlock<Block>::createBlock) ?: error("Unknown block: $block")

fun blockHolder(value: String): DeferredBlock<out Block> = DeferredBlock.createBlock(ResourceLocation.parse(value))

fun itemHolder(item: Item): DeferredItem<out Item> =
    item.builtInRegistryHolder().key?.let(DeferredItem<Item>::createItem) ?: error("Unknown item: $item")

fun itemHolder(value: String): DeferredItem<out Item> = DeferredItem.createItem(ResourceLocation.parse(value))

fun fluidHolder(fluid: Fluid): DeferredHolder<Fluid, Fluid> =
    fluid.builtInRegistryHolder().key?.let(DeferredHolder<Fluid, Fluid>::create) ?: error("Unknown fluid: $fluid")

fun fluidHolder(id: ResourceLocation): DeferredHolder<Fluid, Fluid> = DeferredHolder.create(Registries.FLUID, id)

fun fluidHolder(value: String): DeferredHolder<Fluid, Fluid> = fluidHolder(ResourceLocation.parse(value))
