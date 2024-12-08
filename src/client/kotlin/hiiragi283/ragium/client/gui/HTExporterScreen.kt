package hiiragi283.ragium.client.gui

import com.google.gson.JsonPrimitive
import com.mojang.serialization.DataResult
import com.mojang.serialization.JsonOps
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.network.HTUpdateFilterPayload
import hiiragi283.ragium.common.screen.HTExporterScreenHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

class HTExporterScreen(handler: HTExporterScreenHandler, inventory: PlayerInventory, title: Text) :
    HTScreenBase<HTExporterScreenHandler>(handler, inventory, title) {
    companion object {
        @JvmField
        val TEXTURE: Identifier = RagiumAPI.id("textures/gui/large_machine.png")
    }

    val pipeType: HTPipeType = handler.pipeType
    val player: PlayerEntity = inventory.player
    lateinit var textField: TextFieldWidget

    override fun init() {
        super.init()
        ButtonWidget
            .builder(Text.literal("Apply Change")) { button: ButtonWidget -> }
            .size(18, 18)
            .build()

        textField = TextFieldWidget(
            textRenderer,
            startX + getSlotPosX(0),
            startY + getSlotPosY(0),
            103,
            12,
            Text.translatable("container.repair"),
        )
        textField.setFocusUnlocked(false)
        textField.setEditableColor(-1)
        textField.setUneditableColor(-1)
        // textField.setDrawsBackground(false)
        textField.setMaxLength(50)
        textField.setChangedListener(::onRenamed)
        textField.text = ""
        addSelectableChild(textField)
    }

    private fun onRenamed(name: String) {
        if (pipeType.isItem) {
            RegistryCodecs
                .entryList(RegistryKeys.ITEM)
                .parse(player.world.registryManager.getOps(JsonOps.INSTANCE), JsonPrimitive(name))
                .ifSuccess { entryList: RegistryEntryList<Item> ->
                    ClientPlayNetworking.send(HTUpdateFilterPayload.ItemFilter(entryList))
                }.ifError { error: DataResult.Error<RegistryEntryList<Item>> ->
                    player.sendMessage(Text.literal(error.message()).formatted(Formatting.RED), false)
                }
            return
        }
        if (pipeType.isFluid) {
            RegistryCodecs
                .entryList(RegistryKeys.FLUID)
                .parse(player.world.registryManager.getOps(JsonOps.INSTANCE), JsonPrimitive(name))
                .ifSuccess { entryList: RegistryEntryList<Fluid> ->
                    ClientPlayNetworking.send(HTUpdateFilterPayload.FluidFilter(entryList))
                }.ifError { error: DataResult.Error<RegistryEntryList<Fluid>> ->
                    player.sendMessage(Text.literal(error.message()).formatted(Formatting.RED), false)
                }
            return
        }
    }

    override fun setInitialFocus() {
        setInitialFocus(textField)
    }

    override fun resize(client: MinecraftClient?, width: Int, height: Int) {
        val text: String = textField.text
        init(client, width, height)
        textField.text = text
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            client?.player?.closeHandledScreen()
        }
        return if (!textField.keyPressed(keyCode, scanCode, modifiers) && !textField.isActive) {
            super.keyPressed(keyCode, scanCode, modifiers)
        } else {
            true
        }
    }

    override fun render(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        super.render(context, mouseX, mouseY, delta)
        textField.render(context, mouseX, mouseY, delta)
    }

    override fun drawBackground(
        context: DrawContext,
        delta: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        context.drawTexture(TEXTURE, startX, startY, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        RagiumAPI.LOGGER.info("X: $mouseX, Y: $mouseY, Button: $button")
        return super.mouseClicked(mouseX, mouseY, button)
    }
}
