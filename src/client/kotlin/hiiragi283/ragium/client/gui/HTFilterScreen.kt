package hiiragi283.ragium.client.gui

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

abstract class HTFilterScreen(title: Text) : Screen(title) {
    companion object {
        @JvmField
        val FLUID_CODEC: Codec<RegistryEntryList<Fluid>> = RegistryCodecs.entryList(RegistryKeys.FLUID)

        @JvmField
        val ITEM_CODEC: Codec<RegistryEntryList<Item>> = RegistryCodecs.entryList(RegistryKeys.ITEM)
    }

    protected lateinit var textField: TextFieldWidget
    protected lateinit var doneButton: ButtonWidget
    protected lateinit var cancelButton: ButtonWidget

    override fun init() {
        doneButton = addDrawableChild(
            ButtonWidget
                .builder(ScreenTexts.DONE) {
                    applyChange()
                    close()
                }.dimensions(width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20)
                .build(),
        )
        cancelButton = addDrawableChild(
            ButtonWidget
                .builder(ScreenTexts.CANCEL) { close() }
                .dimensions(width / 2 + 4, height / 4 + 120 + 12, 150, 20)
                .build(),
        )
        textField = TextFieldWidget(
            textRenderer,
            width / 2 - 150,
            50,
            300,
            20,
            Text.translatable(RagiumTranslationKeys.FILTER_FORMAT),
        )
        textField.setMaxLength(32500)
        addSelectableChild(textField)
    }

    protected abstract fun applyChange()

    override fun setInitialFocus() {
        setInitialFocus(textField)
    }

    override fun resize(client: MinecraftClient, width: Int, height: Int) {
        val text: String? = textField.text
        init(client, width, height)
        textField.text = text
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = super.keyPressed(keyCode, scanCode, modifiers)

    override fun render(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        super.render(context, mouseX, mouseY, delta)
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            title,
            this.width / 2,
            20,
            16777215,
        )
        context.drawTextWithShadow(
            this.textRenderer,
            Text.translatable(RagiumTranslationKeys.FILTER_FORMAT),
            this.width / 2 - 150 + 1,
            40,
            10526880,
        )
        textField.render(context, mouseX, mouseY, delta)
    }

    override fun renderBackground(
        context: DrawContext?,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        renderInGameBackground(context)
    }
}
