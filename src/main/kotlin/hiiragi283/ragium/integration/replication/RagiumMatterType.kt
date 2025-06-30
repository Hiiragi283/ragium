package hiiragi283.ragium.integration.replication

import com.buuz135.replication.api.IMatterType
import java.awt.Color
import java.util.function.Supplier

enum class RagiumMatterType(private val color: Color) : IMatterType {
    RAGIUM(Color(0xff0033)),
    ;

    override fun getName(): String = name.lowercase()

    override fun getColor(): Supplier<FloatArray> = Supplier {
        floatArrayOf(
            color.red / 255f,
            color.green / 255f,
            color.blue / 255f,
            1f,
        )
    }

    override fun getMax(): Int = 128
}
