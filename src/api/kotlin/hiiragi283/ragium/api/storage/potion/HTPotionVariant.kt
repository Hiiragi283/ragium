package hiiragi283.ragium.api.storage.potion

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.storage.HTVariant
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions

@ConsistentCopyVisibility
data class HTPotionVariant private constructor(override val holder: Holder.Reference<Potion>) : HTVariant<Potion> {
    @Suppress("DEPRECATION")
    companion object {
        @JvmField
        val CODEC: Codec<HTPotionVariant> = Potion.CODEC.xmap(::of, HTPotionVariant::holder)

        @JvmStatic
        private val simpleCache: MutableMap<Holder.Reference<Potion>, HTPotionVariant> = mutableMapOf()

        @JvmField
        val EMPTY: HTPotionVariant = of(Potions.WATER)

        @JvmStatic
        fun of(holder: Holder<Potion>): HTPotionVariant {
            val reference: Holder.Reference<Potion> =
                holder as? Holder.Reference<Potion> ?: error("Given potion holder is not Holder.Reference!")
            return simpleCache.computeIfAbsent(reference, ::HTPotionVariant)
        }
    }

    override val components: DataComponentPatch = DataComponentPatch.EMPTY

    override val isEmpty: Boolean
        get() = isOf(Potions.WATER)

    fun toStack(count: Int): ItemStack = createPotionStack(holder, count)
}
