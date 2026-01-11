package hiiragi283.ragium.common.item

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import com.lowdragmc.lowdraglib2.gui.ui.style.LayoutStyle
import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.capability.slotRange
import hiiragi283.core.util.HTModularUIHelper
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.ItemStackHandler
import org.appliedenergistics.yoga.YogaFlexDirection

class HTUniversalChestManager private constructor(private val map: MutableMap<DyeColor, ItemStackHandler>) : HTDataSerializable {
    companion object {
        @JvmStatic
        fun getHandler(server: MinecraftServer, color: DyeColor): ItemStackHandler =
            server.overworld().getData(RagiumAttachmentTypes.UNIVERSAL_CHEST).getHandler(color)

        @JvmStatic
        fun createUI(player: Player, title: Component, handler: IItemHandlerModifiable?): ModularUI =
            HTModularUIHelper.createUIWithInv(player, title) {
                val rows: List<UIElement> = List(3) {
                    UIElement()
                        .layout { style: LayoutStyle -> style.setFlexDirection(YogaFlexDirection.ROW) }
                }

                if (handler != null) {
                    for (i: Int in handler.slotRange) {
                        rows[i / 9].addChild(ItemSlot().bind(handler, i))
                    }
                }
                addChildren(*rows.toTypedArray())
            }
    }

    constructor() : this(mutableMapOf())

    fun getHandler(color: DyeColor): ItemStackHandler = map.computeIfAbsent(color) { ItemStackHandler(27) }

    override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        for (color: DyeColor in DyeColor.entries) {
            nbt.put(color.serializedName, getHandler(color).serializeNBT(provider))
        }
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        for (color: DyeColor in DyeColor.entries) {
            val tagIn: CompoundTag = nbt.getCompound(color.serializedName)
            if (tagIn.isEmpty) continue
            getHandler(color).deserializeNBT(provider, tagIn)
        }
    }
}
