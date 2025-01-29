package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock

interface HTBlockContent : HTContent<Block> {
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
     * `block/`で前置された[id]
     */
    val blockId: ResourceLocation get() = id.withPrefix("block/")

    override val holder: DeferredBlock<out Block>

    interface Material :
        HTBlockContent,
        HTMaterialProvider

    interface Tier :
        HTBlockContent,
        HTMachineTierProvider {
        val translationKey: String
    }
}
