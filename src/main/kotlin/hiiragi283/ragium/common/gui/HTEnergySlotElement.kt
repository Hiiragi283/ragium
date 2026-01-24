package hiiragi283.ragium.common.gui

import com.lowdragmc.lowdraglib2.gui.sync.SyncValue
import com.lowdragmc.lowdraglib2.gui.sync.bindings.IBinding
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource
import com.lowdragmc.lowdraglib2.gui.sync.rpc.RPCEvent
import com.lowdragmc.lowdraglib2.gui.sync.rpc.RPCEventBuilder
import com.lowdragmc.lowdraglib2.gui.texture.ColorRectTexture
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.Style
import com.lowdragmc.lowdraglib2.gui.ui.data.FillDirection
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal
import com.lowdragmc.lowdraglib2.gui.ui.data.Vertical
import com.lowdragmc.lowdraglib2.gui.ui.elements.BindableUIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext
import com.lowdragmc.lowdraglib2.gui.ui.style.Property
import com.lowdragmc.lowdraglib2.gui.ui.style.PropertyRegistry
import com.lowdragmc.lowdraglib2.gui.util.DrawerHelper
import com.lowdragmc.lowdraglib2.gui.util.TextFormattingUtil
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister
import com.lowdragmc.lowdraglib2.syncdata.ISubscription
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.text.toText
import hiiragi283.core.common.capability.HTEnergyCapabilities
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import kotlin.math.max

@LDLRegister(name = "energy-slot", group = "inventory", registry = "ldlib2:ui_element")
class HTEnergySlotElement : BindableUIElement<Int>() {
    companion object {
        @JvmStatic
        private val PROPERTIES: Array<Property<*>> = arrayOf(
            PropertyRegistry.HOVER_OVERLAY,
            PropertyRegistry.FILL_DIRECTION,
        )
    }

    inner class SlotStyle : Style(this) {
        init {
            setDefault(PropertyRegistry.HOVER_OVERLAY, ColorRectTexture(-0x7f000001))
        }

        override fun getProperties(): Array<Property<*>> = PROPERTIES

        fun hoverOverlay(): IGuiTexture = getValueSave(PropertyRegistry.HOVER_OVERLAY)

        fun hoverOverlay(texture: IGuiTexture): SlotStyle = apply {
            set(PropertyRegistry.HOVER_OVERLAY, texture)
        }

        fun fillDirection(): FillDirection = getValueSave(PropertyRegistry.FILL_DIRECTION)

        fun fillDirection(fillDirection: FillDirection): SlotStyle = apply {
            set(PropertyRegistry.FILL_DIRECTION, fillDirection)
        }
    }

    val amountLabel = Label()
    val slotStyle = SlotStyle()
    var allowClickInserted = true
    var allowClickExtracted = true

    var amount: Int = 0
        private set
    var capacity: Int = 0
    private val clickEvent: RPCEvent

    private var battery: HTEnergyBattery? = null
    private var energyBatterySubscription: ISubscription? = null

    init {
        layout.width(18f)
        layout.height(18f)
        layout.paddingAll(1f)

        addEventListener(UIEvents.MOUSE_DOWN, ::onMouseDown)
        clickEvent = RPCEventBuilder.simple(::tryClickContainer)
        addRPCEvent(clickEvent)

        amountLabel.layout { it.widthPercent(100f).heightPercent(100f) }
        amountLabel.textStyle {
            it.textAlignVertical(Vertical.BOTTOM).textAlignHorizontal(Horizontal.RIGHT).fontSize(4.5f)
        }
        amountLabel.bindDataSource(SupplierDataSource.of(::getAmountText))
        addChild(amountLabel)
        internalSetup()
    }

    inline fun slotStyle(action: (SlotStyle) -> Unit): HTEnergySlotElement = apply {
        action(slotStyle)
    }

    fun bind(battery: HTEnergyBattery?): HTEnergySlotElement {
        if (energyBatterySubscription != null) {
            energyBatterySubscription?.unsubscribe()
        }
        if (battery == null) return this
        this.battery = battery
        val amountBinding: IBinding<Int> = DataBindingBuilder.intValS2C(battery::getAmount).build()
        val capacitySyncValue: SyncValue<Int> =
            DataBindingBuilder
                .intValS2C(battery::getCapacity)
                .remoteSetter(::capacity::set)
                .build()
                .syncValue
        bind(amountBinding)
        addSyncValue(capacitySyncValue)
        energyBatterySubscription = ISubscription {
            unbind(amountBinding)
            removeSyncValue(capacitySyncValue)
            energyBatterySubscription = null
        }
        return this
    }

    /**
     * @see com.lowdragmc.lowdraglib2.gui.ui.elements.FluidSlot.tryClickContainer
     */
    private fun tryClickContainer() {
        val battery: HTEnergyBattery = this.battery ?: return
        val modularUI: ModularUI = modularUI ?: return
        val player: Player = modularUI.player ?: return
        val menu: AbstractContainerMenu = modularUI.menu ?: return
        val carried: ItemStack = menu.carried
        val batteryIn: HTEnergyBattery = HTEnergyCapabilities.getBattery(carried) ?: return
        val initialAmount: Int = battery.getAmount()
        if (allowClickInserted && initialAmount > 0) {
        }
    }

    private fun onMouseDown(event: UIEvent) {
        sendEvent(clickEvent)
    }

    fun setAmount(amount: Int, notify: Boolean = true): HTEnergySlotElement = setValue(amount, notify)

    fun getAmountText(): Component {
        val amount: Int = this.value
        if (amount <= 0) return Component.empty()
        return TextFormattingUtil.formatLongToCompactString(amount.toLong(), 3).toText()
    }

    override fun getValue(): Int = amount

    override fun setValue(value: Int?, notify: Boolean): HTEnergySlotElement {
        val value1: Int = value ?: 0
        if (this.amount == value1) return this
        this.amount = value1
        if (notify) notifyListeners()
        return this
    }

    override fun drawBackgroundAdditional(guiContext: GUIContext) {
        val amount: Int = this.value
        val hovered: Boolean = isHover || isSelfOrChildHover
        if (amount <= 0 && !isHover) return
        val contentX: Float = getContentX()
        val contentY: Float = getContentY()
        val contentWidth: Float = getContentWidth()
        val contentHeight: Float = getContentHeight()
        if (amount > 0) {
            val fillDirection: FillDirection = slotStyle.fillDirection()
            val progress: Double = amount * 1.0 / max(max(amount, capacity), 1)
            val drawnU: Float = fillDirection.getDrawnU(progress).toFloat()
            val drawnV: Float = fillDirection.getDrawnV(progress).toFloat()
            val drawnWidth: Float = fillDirection.getDrawnWidth(progress).toFloat()
            val drawnHeight: Float = fillDirection.getDrawnHeight(progress).toFloat()
            DrawerHelper.drawSolidRect(
                guiContext.graphics,
                contentX + drawnU * contentWidth,
                contentY + drawnV * contentHeight,
                contentWidth * drawnWidth,
                contentHeight * drawnHeight,
                0xff3366,
            )
        }
        if (hovered) {
            guiContext.drawTexture(slotStyle.hoverOverlay(), contentX, contentY, contentWidth, contentHeight)
        }
    }
}
