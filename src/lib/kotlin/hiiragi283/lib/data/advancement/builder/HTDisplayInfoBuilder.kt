@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.advancement.builder

import hiiragi283.lib.advancment.AdvancementKey
import hiiragi283.lib.advancment.descKey
import hiiragi283.lib.advancment.titleKey
import hiiragi283.lib.item.HTItemInstanceLike
import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import hiiragi283.lib.util.HTDelegates
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.core.ClientAsset
import net.minecraft.world.item.ItemStackTemplate
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
class HTDisplayInfoBuilder {
    companion object {
        @JvmStatic
        inline fun create(key: AdvancementKey, builderAction: HTDisplayInfoBuilder.() -> Unit): DisplayInfo {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTDisplayInfoBuilder().apply {
                titleText = translatableText(key.titleKey)
                descText = translatableText(key.descKey)
                builderAction()
            }.build()
        }
    }

    @PublishedApi internal var icon: ItemStackTemplate by HTDelegates.onceInitialize()
    var titleText: Text by HTDelegates.onceInitialize()
    var descText: Text by HTDelegates.onceInitialize()
    var backGround: Optional<ClientAsset.ResourceTexture> by HTDelegates.onceInitialize { Optional.empty() }
    var type: AdvancementType by HTDelegates.onceInitialize { AdvancementType.TASK }
    var showToast: Boolean = true
    var showChat: Boolean = true
    var hidden: Boolean = false

    operator fun ClientAsset.ResourceTexture.unaryPlus() {
        backGround = Optional.of(this)
    }

    fun build(): DisplayInfo = DisplayInfo(
        icon,
        titleText,
        descText,
        backGround,
        type,
        showToast,
        showChat,
        hidden
    )

    operator fun HTItemInstanceLike.unaryPlus() {
        this.toTemplate()?.let { icon = it }
    }

    inline fun icon(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        icon = ItemInstanceBuilder.buildTemplate(builderAction)
    }
}
