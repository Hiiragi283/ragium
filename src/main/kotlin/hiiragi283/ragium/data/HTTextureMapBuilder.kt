package hiiragi283.ragium.data

import net.minecraft.block.Block
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.item.Item
import net.minecraft.util.Identifier

class HTTextureMapBuilder private constructor() {
    companion object {
        @JvmStatic
        fun of(key: TextureKey, block: Block, suffix: String = ""): TextureMap = create {
            put(key, block, suffix)
        }

        @JvmStatic
        fun of(key: TextureKey, item: Item, suffix: String = ""): TextureMap = create {
            put(key, item, suffix)
        }

        @JvmStatic
        fun create(action: HTTextureMapBuilder.() -> Unit): TextureMap = HTTextureMapBuilder().apply(action).parent
    }

    private val parent: TextureMap = TextureMap()

    fun put(key: TextureKey, block: Block, suffix: String = ""): HTTextureMapBuilder = apply {
        put(key, TextureMap.getSubId(block, suffix))
    }

    fun put(key: TextureKey, item: Item, suffix: String = ""): HTTextureMapBuilder = apply {
        put(key, TextureMap.getSubId(item, suffix))
    }

    fun put(key: TextureKey, id: Identifier): HTTextureMapBuilder = apply {
        parent.put(key, id)
    }
}
