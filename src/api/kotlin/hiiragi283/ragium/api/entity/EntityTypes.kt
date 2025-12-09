package hiiragi283.ragium.api.entity

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

@Suppress("DEPRECATION")
val Entity.typeHolder: Holder.Reference<EntityType<*>> get() = this.type.builtInRegistryHolder()

fun EntityType<*>.isOf(other: EntityType<*>): Boolean = other == this

fun EntityType<*>.isOf(holder: Holder<EntityType<*>>): Boolean = isOf(holder.value())

fun Entity.isOf(type: EntityType<*>): Boolean = this.type.isOf(type)

fun Entity.isOf(holder: Holder<EntityType<*>>): Boolean = this.type.isOf(holder)

fun Entity.isOf(tagKey: TagKey<EntityType<*>>): Boolean = this.type.`is`(tagKey)

fun Entity.isOf(holderSet: HolderSet<EntityType<*>>): Boolean = this.type.`is`(holderSet)
