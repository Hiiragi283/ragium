package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.extension.descKey
import hiiragi283.ragium.api.extension.titleKey
import hiiragi283.ragium.api.util.HTDslMarker
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.Optional

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

    fun setTitleFromKey(key: ResourceKey<Advancement>): HTDisplayInfoBuilder = apply {
        title = Component.translatable(key.titleKey())
    }

    fun setDescFromKey(key: ResourceKey<Advancement>): HTDisplayInfoBuilder = apply {
        description = Component.translatable(key.descKey())
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
