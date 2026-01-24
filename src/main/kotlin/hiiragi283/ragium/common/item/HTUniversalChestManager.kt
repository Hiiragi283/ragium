package hiiragi283.ragium.common.item

import hiiragi283.core.api.gui.menu.HTMenuCallback
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
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor

class HTUniversalChestManager private constructor(private val map: MutableMap<DyeColor, HTItemHandler>) : HTValueSerializable {
    companion object {
        @JvmStatic
        fun getHandler(server: MinecraftServer, color: DyeColor): HTItemHandler =
            server.overworld().getData(RagiumAttachmentTypes.UNIVERSAL_CHEST).getHandler(color)

        @JvmStatic
        fun createSlots(): List<HTBasicItemSlot> = List(27) { HTBasicItemSlot.create(null) }
    }

    constructor() : this(mutableMapOf())

    fun getHandler(color: DyeColor): HTItemHandler = map.computeIfAbsent(color) { ChestHandler() }

    override fun serialize(output: HTValueOutput) {
        for (color: DyeColor in DyeColor.entries) {
            val outputIn: HTValueOutput = output.child(color.serializedName)
            val handler: HTItemHandler = getHandler(color)
            HTCapabilityCodec.ITEM.saveTo(outputIn, handler.getItemSlots(handler.getItemSideFor()))
        }
    }

    override fun deserialize(input: HTValueInput) {
        for (color: DyeColor in DyeColor.entries) {
            val inputIn: HTValueInput = input.child(color.serializedName) ?: continue
            val handler: HTItemHandler = getHandler(color)
            HTCapabilityCodec.ITEM.loadFrom(inputIn, handler.getItemSlots(handler.getItemSideFor()))
        }
    }

    private class ChestHandler(val slots: List<HTItemSlot>) :
        HTItemHandler,
        HTMenuCallback {
        constructor() : this(createSlots())

        override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots

        override fun openMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS)
        }

        override fun closeMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.ENDER_CHEST_CLOSE, SoundSource.PLAYERS)
        }
    }
}
