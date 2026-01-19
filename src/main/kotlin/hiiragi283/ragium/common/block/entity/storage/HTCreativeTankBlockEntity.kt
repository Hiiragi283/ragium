package hiiragi283.ragium.common.block.entity.storage

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.gui.element.HTFluidSlotElement
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.gui.sync.HTDataBindingBuilder
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTCreativeTankBlockEntity(pos: BlockPos, state: BlockState) : HTTankBlockEntity(RagiumBlockEntityTypes.CREATIVE_TANK, pos, state) {
    @DescSynced
    @Persisted
    private var fluid: HTFluidResourceType? = null

    override fun createTank(): HTFluidTank.Basic = CreativeFluidTank()

    private inner class CreativeFluidTank :
        HTFluidTank.Basic(),
        HTDataSerializable.Empty {
        override fun setResource(resource: HTFluidResourceType?) {
            fluid = resource
        }

        override fun setAmount(amount: Int) {}

        override fun getAmount(): Int = Int.MAX_VALUE

        override fun getResource(): HTFluidResourceType? = fluid

        override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

        override fun isValid(resource: HTFluidResourceType): Boolean = false
    }

    override fun setupMainTab(root: UIElement) {
        root
            .addRowChild {
                alineCenter()
                addChild(
                    HTFluidSlotElement()
                        .xeiPhantom()
                        .bind(HTDataBindingBuilder.fluid(::fluid).build()),
                )
            }.addChild(
                Button()
                    .setText("Clear Contents")
                    .setOnServerClick { fluid = null },
            )
    }

    override fun enableUpgradeTab(): Boolean = false
}
