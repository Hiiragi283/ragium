package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.toStack
import net.minecraft.Util
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.*

class HTDisplayInfoBuilder {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTDisplayInfoBuilder.() -> Unit): DisplayInfo = HTDisplayInfoBuilder().apply(builderAction).build()
    }

    lateinit var icon: ItemStack
    lateinit var title: Component
    var description: Component = Component.empty()
    var backGround: ResourceLocation? = null
    private var type: AdvancementType = AdvancementType.TASK
    var showToast: Boolean = true
    var showChat: Boolean = true
    var hidden: Boolean = false

    fun setIcon(item: ItemLike) {
        icon = item.toStack()
    }

    fun setTitleFromKey(key: String): HTDisplayInfoBuilder = apply {
        title = Component.translatable(key)
    }

    fun setTitleFromItem(item: ItemLike): HTDisplayInfoBuilder = apply {
        title = item.toStack().hoverName
    }

    fun setDescFromItem(item: ItemLike): HTDisplayInfoBuilder = apply {
        description = Component.translatable(
            Util.makeDescriptionId(
                "advancements",
                item.asItemHolder().idOrThrow.withSuffix(".desc"),
            ),
        )
    }

    fun setDescFromKey(key: String): HTDisplayInfoBuilder = apply {
        description = Component.translatable(key)
    }

    fun setGoal(): HTDisplayInfoBuilder = apply {
        type = AdvancementType.GOAL
    }

    fun setChallenge(): HTDisplayInfoBuilder = apply {
        type = AdvancementType.CHALLENGE
    }

    fun build(): DisplayInfo = DisplayInfo(
        icon,
        title,
        description,
        Optional.ofNullable(backGround),
        type,
        showToast,
        showChat,
        hidden,
    )
}
