package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.serialization.component.DataComponentGetter
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.ragium.setup.RagiumDataComponents
import java.util.function.IntSupplier
import net.minecraft.core.component.DataComponentMap

class HTStorageCapacityComponent(owner: HTBlockEntity) : HTBlockEntityComponent {
    init {
        owner.addComponent(this)
    }

    var scale: Int = 1

    fun getCapacity(base: IntSupplier): Int = RagiumDataComponents.getCapacity(base, scale)

    override fun serialize(output: HTValueOutput) {
        output.putInt("scale", scale)
    }

    override fun deserialize(input: HTValueInput) {
        input.getInt("scale")?.let(::scale::set)
    }

    override fun applyComponents(input: DataComponentGetter) {
        input.use(RagiumDataComponents.CAPACITY_SCALE, ::scale::set)
    }

    override fun collectComponents(builder: DataComponentMap.Builder) {
        builder.set(RagiumDataComponents.CAPACITY_SCALE, scale)
    }
}
