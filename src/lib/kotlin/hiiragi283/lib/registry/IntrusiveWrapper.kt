@file:Suppress("DEPRECATION")

package hiiragi283.lib.registry

import hiiragi283.lib.resource.SupplierWithKey
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

/**
 * @suppress
 */
private class IntrusiveWrapper<R : Any, out T : R>(private val value: T, private val holderGetter: (T) -> Holder<R>) : SupplierWithKey<R, T> {
    override fun get(): T = value

    override fun getKey(): ResourceKey<R> = holderGetter(value).getKeyOrThrow()

    override fun toString(): String = "IntrusiveWrapper(value=$value)"
}

/**
 * この[Block][this]を[SupplierWithKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <BLOCK : Block> BLOCK.toLike(): SupplierWithKey<Block, BLOCK> = IntrusiveWrapper(this, Block::builtInRegistryHolder)

/**
 * この[EntityType][this]を[SupplierWithKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <ENTITY : Entity> EntityType<ENTITY>.toLike(): SupplierWithKey<EntityType<*>, EntityType<ENTITY>> = IntrusiveWrapper(this, EntityType<*>::builtInRegistryHolder)

/**
 * この[Fluid][this]を[SupplierWithKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <FLUID : Fluid> FLUID.toLike(): SupplierWithKey<Fluid, FLUID> = IntrusiveWrapper(this, Fluid::builtInRegistryHolder)

/**
 * この[Item][this]を[SupplierWithKey]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <ITEM : Item> ITEM.toLike(): SupplierWithKey<Item, ITEM> = IntrusiveWrapper(this, Item::builtInRegistryHolder)
