@file:Suppress("DEPRECATION")

package hiiragi283.lib.registry

import hiiragi283.lib.resource.HTKeyOrValue
import hiiragi283.lib.util.Ior
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
private class IntrusiveWrapper<R : Any, out T : R>(private val value: T, private val holderGetter: (T) -> Holder<R>) : HTKeyOrValue<R, T> {
    override fun unwrapWithKey(): Ior<ResourceKey<R>, T> = Ior.Both(holderGetter(value).getKeyOrThrow(), value)

    override fun toString(): String = "IntrusiveWrapper(value=$value)"
}

/**
 * この[Block][this]を[HTKeyOrValue]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <BLOCK : Block> BLOCK.asKeyOrValue(): HTKeyOrValue<Block, BLOCK> = IntrusiveWrapper(this, Block::builtInRegistryHolder)

/**
 * この[EntityType][this]を[HTKeyOrValue]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <ENTITY : Entity> EntityType<ENTITY>.asKeyOrValue(): HTKeyOrValue<EntityType<*>, EntityType<ENTITY>> = IntrusiveWrapper(this, EntityType<*>::builtInRegistryHolder)

/**
 * この[Fluid][this]を[HTKeyOrValue]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <FLUID : Fluid> FLUID.asKeyOrValue(): HTKeyOrValue<Fluid, FLUID> = IntrusiveWrapper(this, Fluid::builtInRegistryHolder)

/**
 * この[Item][this]を[HTKeyOrValue]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <ITEM : Item> ITEM.asKeyOrValue(): HTKeyOrValue<Item, ITEM> = IntrusiveWrapper(this, Item::builtInRegistryHolder)
