package hiiragi283.ragium.common.item

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.item.HTItemHandler
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import net.minecraft.core.Direction
import net.minecraft.server.MinecraftServer

class HTUniversalChestManager private constructor(private val map: MutableMap<HTDefaultColor, HTItemHandler>) : HTValueSerializable {
    companion object {
        @JvmStatic
        fun getHandler(server: MinecraftServer, color: HTDefaultColor): HTItemHandler =
            server.overworld().getData(RagiumAttachmentTypes.UNIVERSAL_CHEST).getHandler(color)

        @JvmStatic
        fun createSlots(): List<HTBasicItemSlot> = List(27) { HTBasicItemSlot.create(null) }
    }

    constructor() : this(mutableMapOf())

    fun getHandler(color: HTDefaultColor): HTItemHandler = map.computeIfAbsent(color) { ChestHandler() }

    override fun serialize(output: HTValueOutput) {
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            val outputIn: HTValueOutput = output.child(color.serializedName)
            val handler: HTItemHandler = getHandler(color)
            HTCapabilityCodec.ITEM.saveTo(outputIn, handler.getItemSlots(handler.getItemSideFor()))
        }
    }

    override fun deserialize(input: HTValueInput) {
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            val inputIn: HTValueInput = input.child(color.serializedName) ?: continue
            val handler: HTItemHandler = getHandler(color)
            HTCapabilityCodec.ITEM.loadFrom(inputIn, handler.getItemSlots(handler.getItemSideFor()))
        }
    }

    private class ChestHandler(val slots: List<HTItemSlot>) : HTItemHandler {
        constructor() : this(createSlots())

        override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots
    }
}
