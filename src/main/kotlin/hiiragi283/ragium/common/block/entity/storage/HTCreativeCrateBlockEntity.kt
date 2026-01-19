package hiiragi283.ragium.common.block.entity.storage

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.gui.sync.HTDataBindingBuilder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTCreativeCrateBlockEntity(pos: BlockPos, state: BlockState) : HTCrateBlockEntity(RagiumBlockEntityTypes.CREATIVE_CRATE, pos, state) {
    @DescSynced
    @Persisted
    private var item: HTItemResourceType? = null

    override fun createSlot(): HTItemSlot.Basic = CreativeItemSlot()

    private inner class CreativeItemSlot :
        HTItemSlot.Basic(),
        HTDataSerializable.Empty {
        override fun setResource(resource: HTItemResourceType?) {
            item = resource
        }

        override fun setAmount(amount: Int) {}

        override fun getAmount(): Int = Int.MAX_VALUE

        override fun getResource(): HTItemResourceType? = item

        override fun getCapacity(resource: HTItemResourceType?): Int = Int.MAX_VALUE

        override fun isValid(resource: HTItemResourceType): Boolean = false
    }

    override fun setupMainTab(root: UIElement) {
        root
            .addRowChild {
                alineCenter()
                addChild(
                    HTItemSlotElement()
                        .xeiPhantom()
                        .bind(HTDataBindingBuilder.item(::item).build()),
                )
            }.addChild(
                Button()
                    .setText("Clear Contents")
                    .setOnServerClick { item = null },
            )
    }

    override fun enableUpgradeTab(): Boolean = false
}
