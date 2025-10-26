package hiiragi283.ragium.api.block.attribute

import hiiragi283.ragium.api.block.HTBlockWithType
import hiiragi283.ragium.api.tier.HTTierProvider
import net.minecraft.core.Holder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

// getter
inline fun <reified T : HTBlockAttribute> BlockState.hasAttribute(): Boolean = this.blockHolder.hasAttribute<T>()

inline fun <reified T : HTBlockAttribute> Holder<Block>.hasAttribute(): Boolean = this.value().hasAttribute<T>()

inline fun <reified T : HTBlockAttribute> Block.hasAttribute(): Boolean =
    (this as? HTBlockWithType)?.type()?.contains(T::class.java) ?: false

inline fun <reified T : HTBlockAttribute> BlockState.getAttribute(): T? = this.blockHolder.getAttribute<T>()

inline fun <reified T : HTBlockAttribute> Holder<Block>.getAttribute(): T? = this.value().getAttribute<T>()

inline fun <reified T : HTBlockAttribute> Block.getAttribute(): T? = (this as? HTBlockWithType)?.type()?.get<T>()

inline fun <reified T : HTBlockAttribute> BlockState.getAttributeOrThrow(): T = this.blockHolder.getAttributeOrThrow<T>()

inline fun <reified T : HTBlockAttribute> Holder<Block>.getAttributeOrThrow(): T = this.value().getAttributeOrThrow<T>()

inline fun <reified T : HTBlockAttribute> Block.getAttributeOrThrow(): T =
    this.getAttribute<T>() ?: error("Expected $this to have an attribute of type ${T::class.java.simpleName}")

fun BlockState.getAllAttributes(): Collection<HTBlockAttribute> = this.blockHolder.getAllAttributes()

fun Holder<Block>.getAllAttributes(): Collection<HTBlockAttribute> = this.value().getAllAttributes()

fun Block.getAllAttributes(): Collection<HTBlockAttribute> = (this as? HTBlockWithType)?.type()?.getAll() ?: listOf()

// tier
inline fun <reified TIER : HTTierProvider> Holder<Block>.getAttributeTier(): TIER =
    getAttributeOrThrow<HTTierBlockAttribute<TIER>>().provider
