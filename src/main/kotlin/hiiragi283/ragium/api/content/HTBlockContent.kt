package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.extension.keyOrThrow
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

interface HTBlockContent :
    Supplier<Block>,
    ItemLike {
    companion object {
        /**
         * 指定した[holder]から[HTBlockContent]を返します。
         */
        @JvmStatic
        fun of(holder: DeferredBlock<*>): HTBlockContent = object : HTBlockContent {
            override val holder: DeferredBlock<out Block> = holder
        }
    }

    /**
     * [HTBlockContent]の値を移譲する[DeferredHolder]
     */
    val holder: DeferredBlock<out Block>

    val key: ResourceKey<Block> get() = holder.keyOrThrow
    val id: ResourceLocation get() = holder.id

    /**
     * `block/`で前置された[id]
     */
    val blockId: ResourceLocation get() = id.withPrefix("block/")

    override fun get(): Block = holder.get()

    override fun asItem(): Item = get().asItem()

    interface Tier : HTBlockContent {
        val machineTier: HTMachineTier

        val translationKey: String
    }
}
