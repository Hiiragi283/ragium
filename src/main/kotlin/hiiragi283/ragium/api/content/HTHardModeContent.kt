package hiiragi283.ragium.api.content

import net.minecraft.item.Item

fun interface HTHardModeContent {
    fun getContent(hardMode: Boolean): HTContent.Material<Item>

    companion object {
        @JvmStatic
        fun of(normal: HTContent.Material<Item>, hard: HTContent.Material<Item>): HTHardModeContent = HTHardModeContent {
            when (it) {
                true -> hard
                false -> normal
            }
        }
    }
}
