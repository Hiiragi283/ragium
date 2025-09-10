package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.extension.wrapOptional
import hiiragi283.ragium.api.util.HTDslMarker
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

@HTDslMarker
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
        icon = ItemStack(item)
    }

    fun setTitleFromKey(key: HTAdvancementKey): HTDisplayInfoBuilder = apply {
        title = Component.translatable(key.titleKey)
    }

    fun setDescFromKey(key: HTAdvancementKey): HTDisplayInfoBuilder = apply {
        description = Component.translatable(key.descKey)
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
        backGround.wrapOptional(),
        type,
        showToast,
        showChat,
        hidden,
    )
}
